package com.akatsuki.newsum.domain.webtoon.entity.webtoon;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "webtoon_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebtoonDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "webtoon_id", nullable = false)
	private Webtoon webtoon;

	@Column(name = "image_url", nullable = false, length = 1000)
	private String imageUrl;

	@Column(nullable = false, length = 255)
	private String content;

	@Column(name = "image_seq", nullable = false)
	private Byte imageSeq;

	@Builder
	public WebtoonDetail(String imageUrl, String content, Byte imageSeq) {
		this.imageUrl = imageUrl;
		this.content = content;
		this.imageSeq = imageSeq;
	}
}
