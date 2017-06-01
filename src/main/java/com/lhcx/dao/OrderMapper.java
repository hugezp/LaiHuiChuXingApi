package com.lhcx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.Order;

public interface OrderMapper {

    int insertSelective(Order record);

    Order selectByOrderId(@Param("orderId") String orderId);
    
    int updateByOrderIdSelective(Order record);
    
    Order selectNewOrderByPhone(@Param("passengerPhone") String passengerPhone);
    
    Order selectNewOrderByDriverPhone(@Param("driverPhone") String driverPhone);
    
    List<Order> selectOrderByPassengerPhone(@Param("passengerPhone") String passengerPhone,@Param("startPro") int startPro,@Param("pageSize") int pageSize);
    
    List<Order> selectOrderByDriverPhone(@Param("driverPhone") String driverPhone,@Param("startPro") int startPro,@Param("pageSize") int pageSize);

    int selectTotalCountByDriverPhone(@Param("driverPhone") String driverPhone,@Param("status") int status);
}