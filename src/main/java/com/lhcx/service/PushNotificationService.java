package com.lhcx.service;

import java.util.List;

import com.lhcx.model.PushNotification;

public interface PushNotificationService {
	
	List<PushNotification> selectAll(String receive_phone);
	
	int insertSelective(PushNotification record);
}
