package com.lhcx.service;

import com.lhcx.model.PassengerInfo;

public interface IPassengerInfoService {
	 PassengerInfo selectByIdentityToken(String identityToken);

	 int insertSelective(PassengerInfo passengerInfo);
	 
	 int updateByIdentityTokenSelective(PassengerInfo record);
 }
