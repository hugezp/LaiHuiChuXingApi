package com.lhcx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.DriverLocation;

public interface DriverLocationMapper {

    int insertSelective(DriverLocation record);

    int updateByPhoneSelective(DriverLocation record);

    List<DriverLocation> selectList(DriverLocation driverLocation);
    
    DriverLocation selectOnlineByPhone(@Param("phone") String phone);
    
}