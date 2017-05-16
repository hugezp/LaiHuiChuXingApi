package com.lhcx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.dao.OrderMapper;
import com.lhcx.model.Order;
import com.lhcx.service.IOrderService;

@Transactional(rollbackFor=Exception.class)
@Service
public class OrderService implements IOrderService {

	@Autowired
	private OrderMapper orderMapper;
	
	public int insertSelective(Order order) {
		return orderMapper.insertSelective(order);
	}
	
	public int updateByPrimaryKeySelective(Order order) {
		return orderMapper.updateByPrimaryKeySelective(order);
	}
	
	public Order selectByOrderId(String orderId) {
		return orderMapper.selectByOrderId(orderId);
	}
	
	public boolean create(JSONObject jsonRequest) {
		boolean result = false;
		
		return result;
	}
}
