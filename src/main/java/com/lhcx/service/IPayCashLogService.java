package com.lhcx.service;

import java.math.BigDecimal;
import java.util.List;

import com.lhcx.model.PayCashLog;

public interface IPayCashLogService {
	int insertSelective(PayCashLog record);
	BigDecimal selectCashByDriverPhoneToday(String driverphone);
	List<PayCashLog> selectByDriverPhone(String driverphone,int page,int pageSize);
}
