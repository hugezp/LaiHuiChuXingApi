package com.lhcx.dao;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.VerificationCode;

public interface VerificationCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VerificationCode record);

    int insertSelective(VerificationCode record);

    VerificationCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VerificationCode record);

    int updateByPrimaryKey(VerificationCode record);
    
    int getCountByPhonePerDay(@Param("phone") String phone, @Param("userType") String userType);
}