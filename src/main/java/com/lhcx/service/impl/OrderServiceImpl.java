package com.lhcx.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.dao.OrderMapper;
import com.lhcx.model.Order;
import com.lhcx.model.OrderType;
import com.lhcx.service.IDriverLocationService;
import com.lhcx.service.IOrderService;
import com.lhcx.utils.MD5Kit;
import com.lhcx.utils.Utils;

@Transactional(rollbackFor=Exception.class)
@Service
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private IDriverLocationService driverLocationService;
	
	public int insertSelective(Order order) {
		return orderMapper.insertSelective(order);
	}
	
	public int updateByPrimaryKeySelective(Order order) {
		return orderMapper.updateByPrimaryKeySelective(order);
	}
	
	public int updateByOrderIdSelective(Order order){
		return orderMapper.updateByOrderIdSelective(order);
	}
	
	public Order selectByOrderId(String orderId) {
		return orderMapper.selectByOrderId(orderId);
	}
	
	public String create(JSONObject jsonRequest) throws ParseException {
		String result = "";
		Order order = new Order(jsonRequest);
		String orderId = MD5Kit.encode(String.valueOf(System.currentTimeMillis()));
		order.setOrderid(orderId);
		if (insertSelective(order) > 0) {
			result = orderId;
		}
		
		return result;
	}
	
	public Map<String,Object> match(JSONObject jsonRequest) throws ParseException {
		Map<String,Object> result = new HashMap<String, Object>();
		String orderId = jsonRequest.getString("OrderId");

		String driverPhone = jsonRequest.getString("DriverPhone");

		Date distributeTime = new Date();
		
		Order order = selectByOrderId(orderId);
		order.setDriverphone(driverPhone);
		order.setDistributetime(distributeTime);
		order.setStatus(OrderType.Receiving.value());
		updateByOrderIdSelective(order);
		
		result.put("OrderId", orderId);
		result.put("DriverPhone", driverPhone);
		result.put("VehicleNo", order.getVehicleno());
		result.put("DistributeTime", distributeTime);
		
		return result;
	}
}
