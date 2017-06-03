package com.lhcx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.DriverLocation;

public interface DriverLocationMapper {

    int insertSelective(DriverLocation record);

    int updateByIdentityTokenSelective(DriverLocation record);

    List<DriverLocation> selectList(DriverLocation driverLocation);
    
    DriverLocation selectByIdentityToken(@Param("identityToken") String identityToken);
    
    DriverLocation selectOnTimeDistance(@Param("identityToken") String identityToken,@Param("lon") long lon,@Param("lat") long lat);

	int updateLocation(DriverLocation record);
    
}