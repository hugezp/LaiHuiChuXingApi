package com.lhcx.dao;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.Order;

public interface OrderMapper {

    int insertSelective(Order record);

    Order selectByOrderId(@Param("orderId") String orderId);
    
    int updateByOrderIdSelective(Order record);
    
    Order selectNewOrderByPhone(@Param("passengerPhone") String passengerPhone);
    
    Order selectNewOrderByDriverPhone(@Param("driverPhone") String driverPhone);

}