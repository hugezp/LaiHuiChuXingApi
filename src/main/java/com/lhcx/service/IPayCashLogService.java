package com.lhcx.service;

import java.math.BigDecimal;
import java.util.List;

import com.lhcx.model.PayCashLog;

public interface IPayCashLogService {
	int insertSelective(PayCashLog record);
	BigDecimal selectCashByDriverIdentityTokenToday(String driverIdentityToken);
	List<PayCashLog> selectByDriverIdentityToken(String driverIdentityToken,int page,int pageSize);
}
