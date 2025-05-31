package com.akatsuki.newsum.domain.notification.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.akatsuki.newsum.domain.notification.domain.Notification;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long>, NotificationQueryRepository {
}
