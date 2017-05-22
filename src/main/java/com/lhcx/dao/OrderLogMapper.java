package com.lhcx.dao;

import java.util.List;

import com.lhcx.model.OrderLog;

public interface OrderLogMapper {
 
    int insertSelective(OrderLog record);

    List<OrderLog> selectByOrderId(String orderid);
}