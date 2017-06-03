package com.lhcx.dao;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.PassengerInfo;

public interface PassengerInfoMapper {

    int insertSelective(PassengerInfo record);

    int updateByIdentityTokenSelective(PassengerInfo record);

    PassengerInfo selectByIdentityToken(@Param("identityToken") String identityToken);
}