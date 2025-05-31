package com.akatsuki.newsum.extern.service;

import org.springframework.stereotype.Service;

import com.akatsuki.newsum.extern.dto.CreateWebtoonApiRequest;
import com.akatsuki.newsum.extern.properties.AiServerProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServerApiService {

	private final ExternApiService externApiService;
	private final AiServerProperties aiServerProperties;

	private final String CREATE_WEBTOON_API_ENDPOINT = "/v1/comics";

	public void createWebtoonApi(CreateWebtoonApiRequest request) {
		externApiService.sendPostRequest(request, aiServerProperties.baseUrl() + CREATE_WEBTOON_API_ENDPOINT)
			.subscribe(response -> {
				log.info("응답결과 {} ", response);
			}, error -> {
				log.error(error.getMessage());
			});
	}
}
