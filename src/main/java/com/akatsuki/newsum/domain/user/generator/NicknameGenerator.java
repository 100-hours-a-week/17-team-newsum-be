package com.akatsuki.newsum.domain.user.generator;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.akatsuki.newsum.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NicknameGenerator {

	private final List<String> BASE_NAMES = List.of("춘식이", "라이언", "무지", "어피치");
	private final UserRepository userRepository;
	private final Random random = new Random();

	public String generate() {
		Random random = new Random(System.nanoTime());
		String nickname;
		do {
			String base = BASE_NAMES.get(random.nextInt(BASE_NAMES.size()));
			int suffix = random.nextInt(5000);
			nickname = base + suffix;

		} while (userRepository.existsByNickname(nickname));
		return nickname;
	}
}
