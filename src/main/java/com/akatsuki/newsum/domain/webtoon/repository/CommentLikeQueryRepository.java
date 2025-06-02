package com.akatsuki.newsum.domain.webtoon.repository;

import java.util.List;
import java.util.Set;

public interface CommentLikeQueryRepository {

	Set<Long> findLikedCommentIdsByUserIdAndCommentIds(Long userId, List<Long> commentIds);
}
