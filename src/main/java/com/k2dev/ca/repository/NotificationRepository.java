package com.k2dev.ca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.k2dev.ca.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long>{
	List<Notification> findAllByUserId(String userId);
}
