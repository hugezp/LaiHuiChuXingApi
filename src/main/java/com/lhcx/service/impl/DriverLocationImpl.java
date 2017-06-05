package com.lhcx.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.dao.DriverLocationMapper;
import com.lhcx.model.DriverLocation;
import com.lhcx.model.User;
import com.lhcx.service.IDriverLocationService;

/**
 * 驾驶员定位信息service
 */
@Transactional(rollbackFor=Exception.class)
@Service
public class DriverLocationImpl implements IDriverLocationService {

	@Autowired
	private  DriverLocationMapper driverLocationMapper;
	@Autowired
	private HttpSession session;
	
	public List<DriverLocation> selectList(DriverLocation driverLocation) {
		return driverLocationMapper.selectList(driverLocation);
	}
	
	public DriverLocation selectByIdentityToken(String identityToken) {
		return driverLocationMapper.selectByIdentityToken(identityToken);
	}

	public int updateByIdentityTokenSelective(DriverLocation record) {
		return driverLocationMapper.updateByIdentityTokenSelective(record);
	}

	public int insertSelective(DriverLocation record) {
		return driverLocationMapper.insertSelective(record);
	}
	
	public boolean setButton(JSONObject jsonRequest) {
		boolean result = false;
		User user = (User) session.getAttribute("CURRENT_USER");
		String identityToken = user.getIdentityToken();
		DriverLocation driverLocation = selectByIdentityToken(identityToken);
		if (driverLocation == null) {
			driverLocation = new DriverLocation();
			driverLocation.setPositiontime(new Date());
			driverLocation.setLongitude(jsonRequest.getString("Longitude").trim());
			driverLocation.setLatitude(jsonRequest.getString("Latitude").trim());
			driverLocation.setPhone(user.getUserphone());
			driverLocation.setIdentityToken(identityToken);
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
			updateByIdentityTokenSelective(driverLocation);
			result=true;
		}

		return result;
	}
	
	public DriverLocation selectOnTimeDistance(String identityToken,long lon,long lat){
		return driverLocationMapper.selectOnTimeDistance(identityToken, lon, lat);
	}

	@Override
	public int updateLocation(DriverLocation record) {
		return driverLocationMapper.updateLocation(record);
	}

	@Override
	public int updatePush(DriverLocation record) {
		return driverLocationMapper.updatePush(record);
	}
	

}
