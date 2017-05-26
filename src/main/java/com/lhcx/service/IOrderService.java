package com.lhcx.service;

import java.text.ParseException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.Order;
import com.lhcx.model.ResultBean;

public interface IOrderService {
	int insertSelective(Order record);
	Order selectByOrderId(String orderId);
	ResultBean<?> match(JSONObject jsonRequest) throws Exception;
	String create(JSONObject jsonRequest) throws ParseException ;
	int updateByOrderIdSelective(Order record);
	ResultBean<?> cancel(JSONObject jsonRequest) throws Exception;
	ResultBean<?> reached(JSONObject jsonRequest);
	ResultBean<?> depart(JSONObject jsonRequest);
	ResultBean<?> arrive(JSONObject jsonRequest);
	Order info(String orderId);
	Order selectNewOrderByPhone(String passengerPhone);
	Order selectNewOrderByDriverPhone(String driverPhone);
	List<Order> selectOrderByPassengerPhone(String passengerPhone,int page,int pageSize);
	List<Order> selectOrderByDriverPhone(String driverPhone,int page,int pageSize);
}
