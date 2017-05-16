package com.lhcx.service;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.Order;

public interface IOrderService {
	int insertSelective(Order record);
	int updateByPrimaryKeySelective(Order record);
	Order selectByOrderId(String orderId);
	boolean create(JSONObject jsonRequest);

}
