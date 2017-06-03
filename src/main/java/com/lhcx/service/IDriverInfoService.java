package com.lhcx.service;

import com.lhcx.model.DriverInfo;

public interface IDriverInfoService {
	DriverInfo selectByIdentityToken(String identityToken);
	int insertSelective(DriverInfo record);
	int updateByIdentityToken(DriverInfo record);
	int updateByIdentityTokenSelective(DriverInfo record);
}
