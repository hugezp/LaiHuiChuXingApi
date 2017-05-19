package com.lhcx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public DriverLocation selectOnlineByPhone(String phone) {
		return driverLocationMapper.selectOnlineByPhone(phone);
	}

	public int updateByPrimaryKeySelective(DriverLocation record) {
		return driverLocationMapper.updateByPrimaryKeySelective(record);
	}

}
