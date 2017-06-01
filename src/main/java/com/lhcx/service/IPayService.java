package com.lhcx.service;

import java.util.Map;

import com.lhcx.model.Order;

public interface IPayService {

	Map<String, Object> payForWX(Order order);
	
	String payForAlipay(Order order);
}
