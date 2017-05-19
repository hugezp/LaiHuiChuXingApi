package com.lhcx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhcx.dao.PushNotificationMapper;
import com.lhcx.model.PushNotification;
import com.lhcx.service.PushNotificationService;

@Transactional(rollbackFor=Exception.class)
@Service
public class PushNotificationImpl implements PushNotificationService {
	
	@Autowired
	private PushNotificationMapper pushNotificationMapper;
	
	@Override
	public List<PushNotification> selectAll(String receive_phone) {
		return pushNotificationMapper.selectAll(receive_phone);
	}
	
	public int insertSelective(PushNotification record) {
		return pushNotificationMapper.insertSelective(record);
	}


}
