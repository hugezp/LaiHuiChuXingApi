package com.lhcx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhcx.dao.PassengerInfoMapper;
import com.lhcx.model.PassengerInfo;
import com.lhcx.service.IPassengerInfoService;

@Transactional(rollbackFor=Exception.class)
@Service
public class PassengerInfoImpl implements IPassengerInfoService {

	@Autowired
	private PassengerInfoMapper passengerInfoMapper;
	
	public PassengerInfo selectByIdentityToken(String identityToken){
		return passengerInfoMapper.selectByIdentityToken(identityToken);
	}

	@Override
	public int insertSelective(PassengerInfo passengerInfo) {
		return passengerInfoMapper.insertSelective(passengerInfo);
	}

	@Override
	public int updateByIdentityTokenSelective(PassengerInfo record) {
		return passengerInfoMapper.updateByIdentityTokenSelective(record);
	}
}
