package com.lhcx.dao;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.DriverInfo;

public interface DriverInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DriverInfo record);

    int insertSelective(DriverInfo record);

    DriverInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DriverInfo record);

    int updateByPrimaryKey(DriverInfo record);
    
    DriverInfo selectByPhone(@Param("phone") String phone);
}