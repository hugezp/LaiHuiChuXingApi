package com.lhcx.service;

import com.lhcx.model.DriverInfo;

public interface IDriverInfoService {
	public DriverInfo selectByPhone(String phone);
}
