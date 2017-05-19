package com.lhcx.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhcx.model.PushNotification;
import com.lhcx.service.PushNotificationService;

@Transactional(rollbackFor=Exception.class)
@Service
public class PushNotificationImpl implements PushNotificationService {

	public List<PushNotification> selectAll() {
		// TODO Auto-generated method stub
		return null;
	}


}
