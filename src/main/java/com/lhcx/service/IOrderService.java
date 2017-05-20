package com.lhcx.service;

import java.text.ParseException;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.Order;
import com.lhcx.model.ResultBean;

public interface IOrderService {
	int insertSelective(Order record);
	Order selectByOrderId(String orderId);
	ResultBean<?> match(JSONObject jsonRequest) throws Exception;
	String create(JSONObject jsonRequest) throws ParseException ;
	int updateByOrderIdSelective(Order record);
}
