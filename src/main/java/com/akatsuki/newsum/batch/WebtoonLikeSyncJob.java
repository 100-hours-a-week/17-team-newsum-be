package com.akatsuki.newsum.batch;

import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.akatsuki.newsum.cache.RedisService;
import com.akatsuki.newsum.domain.user.entity.User;
import com.akatsuki.newsum.domain.user.repository.UserRepository;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonLike;
import com.akatsuki.newsum.domain.webtoon.exception.WebtoonNotFoundException;
import com.akatsuki.newsum.domain.webtoon.repository.WebtoonLikeRepository;
import com.akatsuki.newsum.domain.webtoon.repository.WebtoonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebtoonLikeSyncJob {

	private final RedisService redisService;
	private final WebtoonRepository webtoonRepository;
	private final WebtoonLikeRepository webtoonLikeRepository;
	private final UserRepository userRepository;

	@Scheduled(cron = "0 */10 * * * *") // 1분마다 실행
	public void syncLikesFromRedisToDB() {
		log.info("🔄 [Batch] Redis 좋아요 → RDB 반영 시작");

		Set<String> keys = redisService.getKeys("webtoon:likes:*");

		for (String key : keys) {
			try {
				Long webtoonId = Long.parseLong(key.split(":")[2]);
				Set<Object> userIds = redisService.getSetMembers(key);

				// 웹툰 조회
				Webtoon webtoon = webtoonRepository.findById(webtoonId)
					.orElseThrow(() -> new WebtoonNotFoundException());

				// likeCount 업데이트
				webtoon.updateLikeCount(userIds.size());
				webtoonRepository.save(webtoon);

				// 좋아요 이력 테이블에 insert (❗ 삭제 없이)
				for (Object userIdObj : userIds) {
					try {
						Long userId = Long.valueOf(userIdObj.toString());
						User user = userRepository.findById(userId).orElse(null);
						if (user != null) {
							webtoonLikeRepository.save(new WebtoonLike(user, webtoon));
						}
					} catch (Exception e) {
						log.warn("⚠️ userId={} insert 실패: {}", userIdObj, e.getMessage());
					}
				}

				log.info("✅ 웹툰 ID={} → 좋아요 수={} / 이력 {}건 저장 완료", webtoonId, userIds.size(), userIds.size());

			} catch (Exception e) {
				log.error("❌ 좋아요 반영 실패 - key={}, reason={}", key, e.getMessage(), e);
			}
		}
	}
}
