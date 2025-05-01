package com.akatsuki.newsum.domain.webtoon.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WebtoonDetailResponse(
	List<WebtoonSourceDto> sourceNews,
	List<WebtoonCardDto> relatedNews,
	LocalDateTime createdAt,
	Long commentCount

) {

}
