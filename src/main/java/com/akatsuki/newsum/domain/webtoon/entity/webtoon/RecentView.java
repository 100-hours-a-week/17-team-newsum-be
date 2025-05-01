package com.akatsuki.newsum.domain.webtoon.entity.webtoon;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recent_view")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecentView {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "webtoon_id", nullable = false)
	private Long webtoonId;

	@Column(name = "viewed_at", nullable = false)
	private LocalDateTime viewedAt;

	@Builder
	public RecentView(Long userId, Long webtoonId) {
		this.userId = userId;
		this.webtoonId = webtoonId;
		this.viewedAt = LocalDateTime.now();
	}
}
