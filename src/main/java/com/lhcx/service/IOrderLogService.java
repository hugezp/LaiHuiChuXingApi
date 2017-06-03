package com.lhcx.service;

import java.util.List;

import com.lhcx.model.OrderLog;

public interface IOrderLogService {
 	int insertSelective(OrderLog record);

 	List<OrderLog> selectByOrderId(String orderid);
 	
}
