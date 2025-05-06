package com.akatsuki.newsum.domain.user.dto;

import java.util.List;
import java.util.Map;

import com.akatsuki.newsum.domain.webtoon.dto.WebtoonCardDto;

public record RecentViewWebtoonListResponse(
	Map<String, List<WebtoonCardDto>> webtoons
) {
}
