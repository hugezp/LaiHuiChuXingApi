package com.lhcx.service;

import java.util.List;

import com.lhcx.model.PushNotification;

public interface IPushNotificationService {
	
	List<PushNotification> selectAll(String receive_phone);
	
	int insertSelective(PushNotification record);
}
