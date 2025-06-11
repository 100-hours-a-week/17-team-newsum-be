package com.akatsuki.newsum.extern.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.akatsuki.newsum.domain.webtoon.entity.webtoon.GenerationStatus;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.ImageGenerationQueue;
import com.akatsuki.newsum.extern.dto.CreateWebtoonApiRequest;
import com.akatsuki.newsum.extern.dto.ImageGenerationApiRequest;
import com.akatsuki.newsum.extern.properties.AiServerProperties;
import com.akatsuki.newsum.extern.repository.ImageGenerationQueueRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServerApiService {

	private final ExternApiService externApiService;
	private final AiServerProperties aiServerProperties;
	private final ImageGenerationQueueRepository imageGenerationQueueRepository;

	private final String CREATE_WEBTOON_API_ENDPOINT = "/v1/comics";

	public void createWebtoonApi(CreateWebtoonApiRequest request) {
		externApiService.sendPostRequest(request, aiServerProperties.baseUrl() + CREATE_WEBTOON_API_ENDPOINT)
			.subscribe(response -> {
				log.info("응답결과 {} ", response);
			}, error -> {
				log.error(error.getMessage());
			});
	}

	public void saveimageprompts(ImageGenerationApiRequest request) {
		ImageGenerationQueue entity = ImageGenerationQueue.builder()
			.aiAuthorId(request.personaId())
			.title(request.title())
			.reportUrl(request.reportUrl())
			.content(request.content())
			.referenceUrl(request.referenceUrl())
			.description1(request.description1())
			.description2(request.description2())
			.description3(request.description3())
			.description4(request.description4())
			.imagePrompts(request.imagePrompts())
			.status(GenerationStatus.PENDING)
			.createdAt(LocalDateTime.now())
			.build();

		imageGenerationQueueRepository.save(entity);
	}

}
