package com.akatsuki.newsum.batch;

import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.akatsuki.newsum.cache.RedisService;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;
import com.akatsuki.newsum.domain.webtoon.exception.WebtoonNotFoundException;
import com.akatsuki.newsum.domain.webtoon.repository.WebtoonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebtoonLikeSyncJob {

	private final RedisService redisService;
	private final WebtoonRepository webtoonRepository;

	@Scheduled(cron = "0 */10 * * * *") // 1분마다 실행
	public void syncLikesFromRedisToDB() {
		log.info("🔄 [Batch] Redis 좋아요 → RDB 반영 시작");

		Set<String> keys = redisService.getKeys("webtoon:likes:*");

		for (String key : keys) {
			try {
				Long webtoonId = Long.parseLong(key.split(":")[2]);
				int likeCount = redisService.getSetMembers(key).size();

				Webtoon webtoon = webtoonRepository.findById(webtoonId)
					.orElseThrow(() -> new WebtoonNotFoundException());

				webtoon.updateLikeCount(likeCount);
				webtoonRepository.save(webtoon);

				log.info("✅ 웹툰 ID={} → 좋아요 {}개 반영 완료", webtoonId, likeCount);

			} catch (Exception e) {
				log.error("❌ 좋아요 반영 실패 - key={}: {}", key, e.getMessage());
			}
		}
	}
}
