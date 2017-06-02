package com.lhcx.service;

import java.util.List;

import com.lhcx.model.PushNotification;

public interface IPushNotificationService {
	
	List<PushNotification> selectAll(PushNotification pushNotification);
	
	int insertSelective(PushNotification record);
}
