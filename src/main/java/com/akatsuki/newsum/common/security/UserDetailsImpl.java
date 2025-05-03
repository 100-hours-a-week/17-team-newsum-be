package com.akatsuki.newsum.common.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.akatsuki.newsum.domain.user.entity.User;

import lombok.Getter;

@Getter
public class UserDetailsImpl implements UserDetails {

	private final User user;

	public UserDetailsImpl(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList(); // 권한이 필요하면 여기에 ROLE 추가
	}

	@Override
	public String getPassword() {
		return null; // 소셜 로그인만 한다면 null
	}

	@Override
	public String getUsername() {
		return user.getEmail(); // 로그인 ID
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
