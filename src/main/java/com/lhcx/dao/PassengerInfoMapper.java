package com.lhcx.dao;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.PassengerInfo;

public interface PassengerInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PassengerInfo record);

    int insertSelective(PassengerInfo record);

    PassengerInfo selectByPrimaryKey(Integer id);

    int updateByPhoneSelective(PassengerInfo record);

    int updateByPhone(PassengerInfo record);
    
    PassengerInfo selectByPhone(@Param("phone") String phone);
}