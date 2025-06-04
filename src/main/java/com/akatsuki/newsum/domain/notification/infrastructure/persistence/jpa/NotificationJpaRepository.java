package com.akatsuki.newsum.domain.notification.infrastructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.akatsuki.newsum.domain.notification.domain.Notification;
import com.akatsuki.newsum.domain.user.entity.User;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long>, NotificationQueryRepository {

	void deleteAllByUser(User user);

	@Query("select n from Notification n where n.id = :id and n.user.id=:userId")
	Optional<Notification> findByIdAndUserId(Long id, Long userId);

	Boolean existsByUserId(Long userId);
}
