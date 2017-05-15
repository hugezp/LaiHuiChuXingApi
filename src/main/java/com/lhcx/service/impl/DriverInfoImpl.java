package com.lhcx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhcx.dao.DriverInfoMapper;
import com.lhcx.model.DriverInfo;
import com.lhcx.service.IDriverInfoService;

@Transactional(rollbackFor=Exception.class)
@Service
public class DriverInfoImpl implements IDriverInfoService {
	
	@Autowired
	private DriverInfoMapper driverInfoMapper;
	
	public DriverInfo selectByPhone(String phone) {
		return driverInfoMapper.selectByPhone(phone);
	}
	
	public int updateByPrimaryKey(DriverInfo record){
		return driverInfoMapper.updateByPrimaryKey(record);
	}
	
	public int insert(DriverInfo record) {
		return driverInfoMapper.insert(record);
	}
	
	public int updateByPhone(DriverInfo record){
		return driverInfoMapper.updateByPhone(record);
	}
	
	public int updateByPhoneSelective(DriverInfo record) {
		return driverInfoMapper.updateByPhoneSelective(record);
	}

}
