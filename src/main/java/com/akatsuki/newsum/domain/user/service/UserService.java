package com.akatsuki.newsum.domain.user.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akatsuki.newsum.converter.DateConverter;
import com.akatsuki.newsum.domain.webtoon.dto.WebtoonCardDto;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.RecentView;
import com.akatsuki.newsum.domain.webtoon.repository.WebtoonRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
	private final WebtoonRepository webtoonRepository;

	public Map<String, List<WebtoonCardDto>> findRecentWebtoonList(Long id) {
		List<RecentView> recentViews = webtoonRepository.findRecentWebtoons(id);
		return recentViews.stream()
			.collect(Collectors.groupingBy(
				recentView -> DateConverter.localDateTimeToDateString(recentView.getViewedAt()),
				() -> new TreeMap<String, List<WebtoonCardDto>>(Comparator.reverseOrder()),
				Collectors.mapping(this::mapToWebtoonCardDto, Collectors.toList())
			));
	}

	private WebtoonCardDto mapToWebtoonCardDto(RecentView recentView) {
		return new WebtoonCardDto(
			recentView.getWebtoon().getId(),
			recentView.getWebtoon().getTitle(),
			recentView.getWebtoon().getThumbnailImageUrl(),
			recentView.getWebtoon().getCreatedAt(),
			recentView.getWebtoon().getViewCount()
		);
	}

}
