package com.lhcx.service;

import java.text.ParseException;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.Order;

public interface IOrderService {
	int insertSelective(Order record);
	int updateByPrimaryKeySelective(Order record);
	Order selectByOrderId(String orderId);
	boolean create(JSONObject jsonRequest) throws ParseException ;
	Map<String,Object> match(JSONObject jsonRequest);

}
