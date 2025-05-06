package com.akatsuki.newsum.domain.webtoon.entity.webtoon;

import java.time.LocalDateTime;

import com.akatsuki.newsum.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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

	@JoinColumn(name = "user_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@JoinColumn(name = "webtoon_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Webtoon webtoon;

	@Column(name = "viewed_at", nullable = false)
	private LocalDateTime viewedAt;
}
