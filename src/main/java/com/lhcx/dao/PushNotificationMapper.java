package com.lhcx.dao;

import java.util.List;

import com.lhcx.model.PushNotification;

public interface PushNotificationMapper {

    int insertSelective(PushNotification record);

    List<PushNotification> selectAll(PushNotification pushNotification);
}