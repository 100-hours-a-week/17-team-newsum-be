package com.akatsuki.newsum.domain.webtoon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.domain.user.entity.User;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.Webtoon;
import com.akatsuki.newsum.domain.webtoon.entity.webtoon.WebtoonLike;

public interface WebtoonLikeRepository extends JpaRepository<WebtoonLike, Long> {
	Optional<WebtoonLike> findByWebtoonAndUser(Webtoon webtoon, User user);
}
