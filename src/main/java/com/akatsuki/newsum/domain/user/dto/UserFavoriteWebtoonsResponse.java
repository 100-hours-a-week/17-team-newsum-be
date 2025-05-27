package com.akatsuki.newsum.domain.user.dto;

import java.util.List;

import com.akatsuki.newsum.common.pagination.model.page.PageInfo;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonCardDto;

public record UserFavoriteWebtoonsResponse(
	List<WebtoonCardDto> webtoons,
	PageInfo pageInfo
) {
}
