package com.lhcx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhcx.dao.OrderLogMapper;
import com.lhcx.model.OrderLog;
import com.lhcx.service.IOrderLogService;

@Service
public class OrderLogServiceImpl implements IOrderLogService {
	@Autowired
	private OrderLogMapper orderLogMapper;
	
	 public int insertSelective(OrderLog record){
		 return orderLogMapper.insertSelective(record);
	 }

	 public List<OrderLog> selectByOrderId(String orderid){
		 return orderLogMapper.selectByOrderId(orderid);
	 }

	@Override
	public OrderLog selectByOrderPhone(String operatorPhone) {
		return orderLogMapper.selectByOrderPhone(operatorPhone);
	}
}
