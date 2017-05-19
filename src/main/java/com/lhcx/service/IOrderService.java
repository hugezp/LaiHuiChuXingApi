package com.lhcx.service;

import java.text.ParseException;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.Order;

public interface IOrderService {
	int insertSelective(Order record);
	int updateByPrimaryKeySelective(Order record);
	Order selectByOrderId(String orderId);
	String create(JSONObject jsonRequest) throws ParseException ;

}
