package com.lhcx.service;

import com.lhcx.model.DriverInfo;

public interface IDriverInfoService {
	DriverInfo selectByPhone(String phone);
	int insert(DriverInfo record);
	int insertSelective(DriverInfo record);
	int updateByPrimaryKey(DriverInfo record);
	int updateByPhone(DriverInfo record);
	int updateByPhoneSelective(DriverInfo record);
}
