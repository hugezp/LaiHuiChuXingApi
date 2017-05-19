package com.lhcx.service;

import java.util.List;

import com.lhcx.model.DriverLocation;


/**
 * 驾驶员定位信息service
 */
public interface IDriverLocationService {

	List<DriverLocation> selectList(DriverLocation driverLocation);
	
}
