package com.akatsuki.newsum.extern.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExternApiService {
	private final WebClient webClient;

	public <T> Mono<String> sendPostRequest(T requestDto, String url) {
		return webClient.post()
			.uri(url)
			.bodyValue(requestDto)
			.retrieve()
			.bodyToMono(String.class);
	}
}
