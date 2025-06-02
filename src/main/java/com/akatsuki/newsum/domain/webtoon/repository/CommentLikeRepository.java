package com.akatsuki.newsum.domain.webtoon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.domain.webtoon.entity.comment.entity.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>, CommentLikeQueryRepository {
	Optional<CommentLike> findByUserIdAndCommentId(Long userId, Long commentId);

	boolean existsByUserIdAndCommentId(Long userId, Long commentId);

	void deleteByUserIdAndCommentId(Long userId, Long commentId);

	long countByCommentId(Long commentId);

}
