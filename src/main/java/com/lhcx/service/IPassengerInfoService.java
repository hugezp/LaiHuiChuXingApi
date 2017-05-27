package com.lhcx.service;

import com.lhcx.model.PassengerInfo;

public interface IPassengerInfoService {
	 PassengerInfo selectByPhone(String phone);

	 int insertSelective(PassengerInfo passengerInfo);
	 
	 int updateByPhoneSelective(PassengerInfo record);
 }
