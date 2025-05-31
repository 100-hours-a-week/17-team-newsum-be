package com.akatsuki.newsum.domain.user.generator;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.exception.BusinessException;
import com.akatsuki.newsum.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NicknameGenerator {

	private final List<String> BASE_NAMES = List.of("춘식이", "라이언", "무지", "어피치");
	private final UserRepository userRepository;

	public String generate() {
		Random random = new Random(System.nanoTime());

		int attempts = 0;
		int maxAttempts = 20;

		while (attempts++ < maxAttempts) {
			String base = BASE_NAMES.get(random.nextInt(BASE_NAMES.size()));
			int suffix = random.nextInt(5000);
			String nickname = base + suffix;

			if (!userRepository.existsByNickname(nickname)) {
				return nickname;
			}
		}
		throw new BusinessException(ErrorCodeAndMessage.DUPLICATE_NICKNAME_GENERATION_FAILED);
	}

}
