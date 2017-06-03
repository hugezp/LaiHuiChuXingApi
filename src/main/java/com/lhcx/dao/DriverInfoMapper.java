package com.lhcx.dao;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.DriverInfo;

public interface DriverInfoMapper {

    int insertSelective(DriverInfo record);
    
    DriverInfo selectByIdentityToken(@Param("identityToken") String identityToken);
    
    int updateByIdentityToken(DriverInfo record);
    
    int updateByIdentityTokenSelective(DriverInfo record);
}