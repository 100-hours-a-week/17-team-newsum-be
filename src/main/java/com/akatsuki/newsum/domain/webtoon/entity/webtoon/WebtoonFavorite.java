package com.akatsuki.newsum.domain.webtoon.entity.webtoon;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "webtoon_favorite", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"user_id", "webtoon_id"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebtoonFavorite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "webtoon_id", nullable = false)
	private Webtoon webtoon;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	public WebtoonFavorite(User user, Webtoon webtoon) {
		this.user = user;
		this.webtoon = webtoon;
	}
}
