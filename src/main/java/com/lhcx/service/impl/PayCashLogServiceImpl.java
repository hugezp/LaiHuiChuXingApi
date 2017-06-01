package com.lhcx.service.impl;

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

}
