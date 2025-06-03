package com.akatsuki.newsum.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akatsuki.newsum.domain.user.entity.User;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Keyword;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.KeywordFavorite;
import com.akatsuki.newsum.domain.webtoon.repository.KeywordFavoriteRepository;
import com.akatsuki.newsum.domain.webtoon.repository.KeywordRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class KeywordService {
	private final KeywordFavoriteRepository keywordFavoriteRepository;
	private final KeywordRepository keywordRepository;

	@Transactional(readOnly = false)
	public void subscribeKeyword(Long userId, String keywordContent) {
		User user = new User(userId);

		Keyword keyword = keywordRepository.findByContent(keywordContent)
			.orElseGet(() -> keywordRepository.save(new Keyword(keywordContent)));

		KeywordFavorite favorite = user.subscribeKeyword(keyword);
		keywordFavoriteRepository.save(favorite);
	}

}
