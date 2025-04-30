package com.akatsuki.newsum.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social_login")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialLogin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "provider_id", nullable = false)
    private Long providerId;
    
    @Column(name = "provider", nullable = false, length = 20)
    private String provider;

    @Builder
    public SocialLogin(User user, Long providerId, String provider) {
        this.user = user;
        this.providerId = providerId;
        this.provider = provider;
    }

    public void update(Long providerId, String provider) {
        this.providerId = providerId;
        this.provider = provider;
    }
}
