package com.akatsuki.newsum.domain.webtoon.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akatsuki.newsum.common.dto.ErrorCodeAndMessage;
import com.akatsuki.newsum.common.pagination.CursorPaginationService;
import com.akatsuki.newsum.common.pagination.model.cursor.Cursor;
import com.akatsuki.newsum.common.pagination.model.page.CursorPage;
import com.akatsuki.newsum.domain.webtoon.dto.CommentAndSubComments;
import com.akatsuki.newsum.domain.webtoon.dto.CommentCreateRequest;
import com.akatsuki.newsum.domain.webtoon.dto.CommentEditRequest;
import com.akatsuki.newsum.domain.webtoon.dto.CommentLikeResponseDto;
import com.akatsuki.newsum.domain.webtoon.dto.CommentListResult;
import com.akatsuki.newsum.domain.webtoon.dto.CommentReadDto;
import com.akatsuki.newsum.domain.webtoon.dto.CommentResult;
import com.akatsuki.newsum.domain.webtoon.entity.comment.entity.Comment;
import com.akatsuki.newsum.domain.webtoon.entity.comment.entity.CommentLike;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;
import com.akatsuki.newsum.domain.webtoon.exception.CommentForbiddenException;
import com.akatsuki.newsum.domain.webtoon.exception.CommentNotFoundException;
import com.akatsuki.newsum.domain.webtoon.exception.WebtoonNotFoundException;
import com.akatsuki.newsum.domain.webtoon.repository.CommentLikeRepository;
import com.akatsuki.newsum.domain.webtoon.repository.CommentRepository;
import com.akatsuki.newsum.domain.webtoon.repository.WebtoonRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final WebtoonRepository webtoonRepository;
	private final CommentLikeRepository commentLikeRepository;
	private final CursorPaginationService cursorPaginationService;

	public CommentListResult findCommentsByWebtoon(Long webtoonId, Cursor cursor, Integer size,
		Long id) {
		//2. Cursor 기반 부모 댓글 조회
		List<CommentReadDto> allParentComments = commentRepository.findParentCommentsByCursorAndSize(webtoonId, cursor,
			size);

		//3. 부모댓글 기반 자녀 댓글 전체 조회
		List<Long> parentCommentIds = getParentCommentIds(allParentComments);
		List<CommentReadDto> allSubComments = commentRepository.findByWebtoonIdAndParentCommentIdIn(webtoonId,
			parentCommentIds);

		//TODO : isLiked 체크하는 부분 추가 필요
		//4. 부모 댓글 CommentResult 매핑 및 좋아요 유무, 댓글 작성자 유무 확인
		List<CommentResult> parentCommentResult = getParentCommentResults(id, allParentComments);

		//TODO : isLiked 체크하는 부분 추가 필요
		//5. 자식 댓글을 parentCommentId 기준으로 그룹핑
		Map<Long, List<CommentResult>> subCommentsGroupByParentId = collectSubCommentResultByParentId(allSubComments,
			id);

		//6. 부모 + 자식 댓글을 하나의 CommentAndSubComments로 조립
		List<CommentAndSubComments> commentAndSubComments = mergeParentAndSubComments(parentCommentResult,
			subCommentsGroupByParentId);

		//7. 댓글 총 개수 조회
		Long commentCount = commentRepository.countCommentsByWebtoonId(webtoonId);

		CursorPage<CommentAndSubComments> cursorPage = cursorPaginationService.create(commentAndSubComments,
			size,
			cursor);
		return new CommentListResult(cursorPage, commentCount);
	}

	@Transactional
	public void addComment(CommentCreateRequest request, Long webtoonId, Long id) {
		Webtoon webtoon = findWebtoonById(webtoonId);
		if (!webtoon.getId().equals(webtoonId)) {
			throw new WebtoonNotFoundException(ErrorCodeAndMessage.WEBTOON_NOT_FOUND);
		}
		Long parentId = setParentId(request.parentId());
		Comment newComment = new Comment(id, webtoonId, parentId, request.content());
		commentRepository.save(newComment);
	}

	private Long setParentId(Long id) {
		return id == null || id == 0 ? null : id;
	}

	@Transactional
	public void editComment(CommentEditRequest request, Long webtoonId, Long commentId, Long id) {
		Comment comment = findCommentById(commentId);

		checkAuthorOfComment(id, comment);
		checkWebtoonOfComment(webtoonId, comment);

		comment.editComment(request.content());
	}

	@Transactional
	public void deleteComment(Long webtoonId, Long commentId, Long id) {
		Comment comment = findCommentById(commentId);

		checkAuthorOfComment(id, comment);
		checkWebtoonOfComment(webtoonId, comment);

		commentRepository.delete(comment);
	}

	private List<CommentAndSubComments> mergeParentAndSubComments(List<CommentResult> parentCommentResult,
		Map<Long, List<CommentResult>> subCommentsGroupByParentId) {
		return parentCommentResult.stream()
			.map(parent -> collectCommentAndSubCommentsByParent(parent, subCommentsGroupByParentId))
			.toList();
	}

	private Map<Long, List<CommentResult>> collectSubCommentResultByParentId(List<CommentReadDto> allSubComments,
		Long userId) {
		return allSubComments.stream()
			.collect(Collectors.groupingBy(
				CommentReadDto::getParentId,
				Collectors.mapping(sub -> mapToCommentResultWithOwnerAndLiked(userId, sub),
					Collectors.toList())
			));
	}

	private List<CommentResult> getParentCommentResults(Long id, List<CommentReadDto> parentComments) {
		return parentComments.stream()
			.map(commentReadDto -> mapToCommentResultWithOwnerAndLiked(id, commentReadDto))
			.toList();
	}

	private List<Long> getParentCommentIds(List<CommentReadDto> parentComments) {
		return parentComments.stream()
			.map(CommentReadDto::getId)
			.toList();
	}

	private CommentResult mapToCommentResultWithOwnerAndLiked(Long id, CommentReadDto commentReadDto) {
		boolean isOwner = commentReadDto.getAuthorId().equals(id);
		boolean isliked = commentLikeRepository.existsByUserIdAndCommentId(id, commentReadDto.getId());
		long likecount = commentLikeRepository.countByCommentId(commentReadDto.getId());
		return CommentResult.of(commentReadDto, isliked, isOwner, likecount);
	}

	private CommentAndSubComments collectCommentAndSubCommentsByParent(CommentResult parent,
		Map<Long, List<CommentResult>> subCommentsGroupByParentId) {
		List<CommentResult> childComments = subCommentsGroupByParentId.getOrDefault(parent.id(), List.of());
		return CommentAndSubComments.from(parent, childComments);
	}

	private void checkWebtoonOfComment(Long webtoonId, Comment comment) {
		if (!comment.getWebtoonId().equals(webtoonId)) {
			throw new CommentNotFoundException();
		}
	}

	private void checkAuthorOfComment(Long id, Comment comment) {
		if (!comment.getUserId().equals(id)) {
			throw new CommentForbiddenException();
		}
	}

	public Comment findCommentById(Long id) {
		return commentRepository.findById(id)
			.orElseThrow(CommentNotFoundException::new);
	}

	private Webtoon findWebtoonById(Long webtoonId) {
		return webtoonRepository.findById(webtoonId)
			.orElseThrow(WebtoonNotFoundException::new);
	}

	@Transactional
	public boolean toggleCommentLike(Long userId, Long commentId) {
		AtomicBoolean liked = new AtomicBoolean(false);
		Comment comment = findCommentById(commentId);

		commentLikeRepository.findByUserIdAndCommentId(userId, commentId)
			.ifPresentOrElse(
				existing -> {
					commentLikeRepository.delete(existing);
					comment.decrementLikeCount();
				},
				() -> {
					commentLikeRepository.save(new CommentLike(userId, commentId));
					comment.incrementLikeCount();
					liked.set(true);
				}
			);

		return liked.get();
	}

	private long getCommentLikeCount(Long commentId) {
		return commentLikeRepository.countByCommentId(commentId);
	}

	@Transactional(readOnly = true)
	public CommentLikeResponseDto getCommentLikeStatus(Long userId, Long commentId) {
		boolean liked = commentLikeRepository.existsByUserIdAndCommentId(userId, commentId);
		long count = getCommentLikeCount(commentId);

		return new CommentLikeResponseDto(liked, count);
	}

}
