package com.lhcx.service;

import java.text.ParseException;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.Order;

public interface IOrderService {
	int insertSelective(Order record);
	Order selectByOrderId(String orderId);
	Map<String,Object> match(JSONObject jsonRequest,String  driverPhone) throws ParseException;
	String create(JSONObject jsonRequest) throws ParseException ;
	int updateByOrderIdSelective(Order record);
}
