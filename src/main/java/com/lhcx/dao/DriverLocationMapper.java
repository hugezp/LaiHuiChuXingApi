package com.lhcx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.DriverLocation;

public interface DriverLocationMapper {

    int insertSelective(DriverLocation record);

    int updateByPhoneSelective(DriverLocation record);

    List<DriverLocation> selectList(DriverLocation driverLocation);
    
    DriverLocation selectByPhone(@Param("phone") String phone);
    
    DriverLocation selectOnTimeDistance(@Param("phone") String phone,@Param("lon") long lon,@Param("lat") long lat);
    
}