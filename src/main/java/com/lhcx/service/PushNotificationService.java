package com.lhcx.service;

import java.util.List;

import com.lhcx.model.PushNotification;

public interface PushNotificationService {
	
	List<PushNotification> selectAll(int receive_id);
}
