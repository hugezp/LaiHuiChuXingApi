package com.lhcx.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhcx.dao.PayCashLogMapper;
import com.lhcx.model.PayCashLog;
import com.lhcx.service.IPayCashLogService;
import com.lhcx.utils.DateUtils;

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
	public BigDecimal selectCashByIdentityTokenToday(String driverIdentityToken) {
		return payCashLogMapper.selectCashByIdentityTokenToday(driverIdentityToken);
	}

	@Override
	public List<PayCashLog> selectByIdentityToken(String driverIdentityToken,int page,int pageSize) {
		return payCashLogMapper.selectByIdentityToken(driverIdentityToken, (page-1)*pageSize, pageSize);
	}
	@Override
	public List<PayCashLog> selectByIdentityTokenAndTime(String identityToken,
			int page, int size, String startTime,String endTime) {
		List<PayCashLog> list = payCashLogMapper.selectByIdentityTokenAndTime(identityToken, page, size, startTime,endTime);
		for (PayCashLog payCashLog : list) {
			payCashLog.setCtime(DateUtils.dateFormat(payCashLog.getCreatetime()));
			payCashLog.setUtime(DateUtils.dateFormat(payCashLog.getUpdatetime()));
		}
		return list;
		
	}

	@Override
	public List<PayCashLog> selectByIdentityTokenAndActiontype(String identityToken,
			int page, int size, int actiontype) {
		List<PayCashLog> list = payCashLogMapper.selectByIdentityTokenAndActiontype(identityToken, page, size, actiontype);
		for (PayCashLog payCashLog : list) {
			payCashLog.setCtime(DateUtils.dateFormat(payCashLog.getCreatetime()));
			payCashLog.setUtime(DateUtils.dateFormat(payCashLog.getUpdatetime()));
		}
		return list;
	}

}
