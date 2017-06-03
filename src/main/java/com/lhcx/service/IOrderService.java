package com.lhcx.service;

import java.text.ParseException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.Order;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;

public interface IOrderService {
	int insertSelective(Order record);
	Order selectByOrderId(String orderId);
	ResultBean<?> match(JSONObject jsonRequest) throws Exception;
	String create(JSONObject jsonRequest, User user) throws ParseException ;
	int updateByOrderIdSelective(Order record);
	ResultBean<?> cancel(JSONObject jsonRequest) throws Exception;
	ResultBean<?> reached(JSONObject jsonRequest);
	ResultBean<?> depart(JSONObject jsonRequest);
	ResultBean<?> arrive(JSONObject jsonRequest);
	Order info(String orderId);
	Order selectNewOrderByPassengerIdentityToken(String passengerIdentityToken);
	Order selectNewOrderByDriverIdentityToken(String driverIdentityToken);
	List<Order> selectOrderByPassengerIdentityToken(String passengerIdentityToken,int page,int pageSize);
	List<Order> selectOrderByDriverIdentityToken(String driverIdentityToken,int page,int pageSize);
	int selectTotalCountByDriverIdentityToken(String driverIdentityToken,Integer status);
}
