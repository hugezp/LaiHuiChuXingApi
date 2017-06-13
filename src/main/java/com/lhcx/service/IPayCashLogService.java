package com.lhcx.service;

import java.math.BigDecimal;
import java.util.List;

import com.lhcx.model.PayCashLog;

public interface IPayCashLogService {
	int insertSelective(PayCashLog record);
	BigDecimal selectCashByIdentityTokenToday(String driverIdentityToken);
	List<PayCashLog> selectByIdentityToken(String driverIdentityToken,int page,int pageSize);
	List<PayCashLog> selectByIdentityTokenAndTime(String identityToken, int page,
			int size, String startTime,String endTime);
	List<PayCashLog> selectByIdentityTokenAndActiontype(String identityToken,
			int page, int size, int actiontype);
}
