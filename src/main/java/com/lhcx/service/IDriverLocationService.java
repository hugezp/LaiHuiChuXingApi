package com.lhcx.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.DriverLocation;


/**
 * 驾驶员定位信息service
 */
public interface IDriverLocationService {

	List<DriverLocation> selectList(DriverLocation driverLocation);
	
    DriverLocation selectByIdentityToken(String identityToken);

	int updateByIdentityTokenSelective(DriverLocation record);
	
	int updateLocation(DriverLocation record);
	
	int insertSelective(DriverLocation record);
	
	boolean setButton(JSONObject jsonRequest);
	
	DriverLocation selectOnTimeDistance(String identityToken,long lon,long lat);
	
	

}
