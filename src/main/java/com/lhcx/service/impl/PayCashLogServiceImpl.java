package com.lhcx.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhcx.dao.PayCashLogMapper;
import com.lhcx.model.PayCashLog;
import com.lhcx.service.IPayCashLogService;

@Transactional(rollbackFor=Exception.class)
@Service
public class PayCashLogServiceImpl implements IPayCashLogService{
	@Autowired
	private PayCashLogMapper payCashLogMapper;

	@Override
	public int insertSelective(PayCashLog record) {
		return payCashLogMapper.insertSelective(record);
	}

	@Override
	public BigDecimal selectCashByDriverPhoneToday(String driverphone) {
		return payCashLogMapper.selectCashByDriverPhoneToday(driverphone);
	}

	@Override
	public List<PayCashLog> selectByDriverPhone(String driverphone,int page,int pageSize) {
		return payCashLogMapper.selectByDriverPhone(driverphone, (page-1)*pageSize, pageSize);
	}

}
