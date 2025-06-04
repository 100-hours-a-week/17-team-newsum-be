package com.akatsuki.newsum.domain.webtoon.entity.webtoon;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "keyword_favorite")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordFavorite {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "keyword_id", nullable = false)
	private Long keywordId;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	public KeywordFavorite(Long userId, Long keywordId) {
		this.userId = userId;
		this.keywordId = keywordId;
		this.createdAt = LocalDateTime.now();
	}

}
