package com.akatsuki.newsum.domain.webtoon.entity.webtoon;

import com.akatsuki.newsum.domain.user.entity.User;

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

@Entity
@Table(name = "webtoon_like", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"user_id", "webtoon_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebtoonLike {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "webtoon_id", nullable = false)
	private Webtoon webtoon;
}
