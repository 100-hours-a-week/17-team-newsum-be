package com.akatsuki.newsum.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.domain.user.entity.Provider;
import com.akatsuki.newsum.domain.user.entity.SocialLogin;
import com.akatsuki.newsum.domain.user.entity.User;

public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {
	boolean existsByUserAndProvider(User user, Provider provider);
}
