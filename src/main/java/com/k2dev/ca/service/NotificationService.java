package com.k2dev.ca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.k2dev.ca.model.Notification;
import com.k2dev.ca.repository.NotificationRepository;

@Service
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;
	
	public List<Notification> getNotifications(String userid){
		return notificationRepository.findAllByUserId(userid);
	}

	public void deleteNotification(long nId) {
		notificationRepository.deleteById(nId);
	}
}
