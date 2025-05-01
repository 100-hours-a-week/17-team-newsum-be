package com.akatsuki.newsum.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.user.domain.ProviderType;
import com.akatsuki.newsum.user.domain.SocialLogin;
import com.akatsuki.newsum.user.domain.User;

public interface SocialLoginRepository extends JpaRepository<SocialLogin, Long> {
	boolean existsByUserAndProviderType(User user, ProviderType providerType);
}
