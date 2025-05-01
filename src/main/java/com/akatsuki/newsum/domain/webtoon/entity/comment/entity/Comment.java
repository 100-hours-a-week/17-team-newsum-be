package com.akatsuki.newsum.domain.webtoon.entity.comment.entity;

import java.time.LocalDateTime;

import com.akatsuki.newsum.common.entity.BaseTimeEntity;

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
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(name = "webtoon_id", nullable = false)
	private Long webtoonId;

	@Column(name = "parent_comment_id")
	private Long parentCommentId;

	@Column(nullable = false, length = 400)
	private String content;

	@Column(name = "like_count", nullable = false)
	private Long likeCount = 0L;

	private LocalDateTime deletedAt;
}
