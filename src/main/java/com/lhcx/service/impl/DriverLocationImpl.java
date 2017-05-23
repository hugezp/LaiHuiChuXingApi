package com.lhcx.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.dao.DriverLocationMapper;
import com.lhcx.model.DriverLocation;
import com.lhcx.service.IDriverLocationService;

/**
 * 驾驶员定位信息service
 */
@Transactional(rollbackFor=Exception.class)
@Service
public class DriverLocationImpl implements IDriverLocationService {

	@Autowired
	private  DriverLocationMapper driverLocationMapper;
	
	public List<DriverLocation> selectList(DriverLocation driverLocation) {
		return driverLocationMapper.selectList(driverLocation);
	}
	
	public DriverLocation selectByPhone(String phone) {
		return driverLocationMapper.selectByPhone(phone);
	}

	public int updateByPhoneSelective(DriverLocation record) {
		return driverLocationMapper.updateByPhoneSelective(record);
	}

	public int insertSelective(DriverLocation record) {
		return driverLocationMapper.insertSelective(record);
	}
	
	public boolean setButton(JSONObject jsonRequest,String phone) {
		boolean result = false;
		DriverLocation driverLocation = selectByPhone(phone);
		if (driverLocation == null) {
			driverLocation = new DriverLocation();
			driverLocation.setPositiontime(new Date());
			driverLocation.setLongitude(jsonRequest.getString("Longitude").trim());
			driverLocation.setLatitude(jsonRequest.getString("Latitude").trim());
			driverLocation.setPhone(phone);
			driverLocation.setLoginTime(new Date());
			insertSelective(driverLocation);
			result=true;
		} else {
			driverLocation.setPositiontime(new Date());
			driverLocation.setLongitude(jsonRequest.getString("Longitude").trim());
			driverLocation.setLatitude(jsonRequest.getString("Latitude").trim());
			Integer isDel = Integer.parseInt(jsonRequest.getString("isDel"));
			if (isDel == 1) {
				driverLocation.setLoginTime(new Date());
			} else {
				driverLocation.setLogoutTime(new Date());
			}
			driverLocation.setIsdel(isDel);
			updateByPhoneSelective(driverLocation);
			result=true;
		}

		return result;
	}
	
	public DriverLocation selectOnTimeDistance(String phone,long lon,long lat){
		return driverLocationMapper.selectOnTimeDistance(phone, lon, lat);
	}
	

}
