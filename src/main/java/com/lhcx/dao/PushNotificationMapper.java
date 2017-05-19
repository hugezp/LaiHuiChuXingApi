package com.lhcx.dao;

import java.util.List;

import com.lhcx.model.PushNotification;

public interface PushNotificationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PushNotification record);

    int insertSelective(PushNotification record);

    PushNotification selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PushNotification record);

    int updateByPrimaryKey(PushNotification record);
    
    List<PushNotification> selectAll();
}