package com.akatsuki.newsum.domain.user.entity;

import com.akatsuki.newsum.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@SQLDelete(sql = "UPDATE user SET deleted_at = now() WHERE id = ?")
@SQLRestriction(value = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "profile_image_url", nullable = false, length = 1000)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    private LocalDateTime deletedAt;

    @Builder
    public User(String email, String nickname, String profileImageUrl, UserRole userRole) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "BASIC_PROFILE_IMAGE_URL";
        this.userRole = userRole != null ? userRole : UserRole.USER_BASIC;
        this.status = Status.ACTIVATE;
    }
}
