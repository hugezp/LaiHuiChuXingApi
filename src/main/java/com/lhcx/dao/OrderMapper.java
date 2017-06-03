package com.lhcx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.Order;

public interface OrderMapper {

    int insertSelective(Order record);

    Order selectByOrderId(@Param("orderId") String orderId);
    
    int updateByOrderIdSelective(Order record);
    
    Order selectNewOrderByPassengerIdentityToken(@Param("passengerIdentityToken") String passengerIdentityToken);
    
    Order selectNewOrderByDriverIdentityToken(@Param("driverIdentityToken") String driverIdentityToken);
    
    List<Order> selectOrderByPassengerIdentityToken(@Param("passengerIdentityToken") String passengerIdentityToken,@Param("startPro") int startPro,@Param("pageSize") int pageSize);
    
    List<Order> selectOrderByDriverIdentityToken(@Param("driverIdentityToken") String driverIdentityToken,@Param("startPro") int startPro,@Param("pageSize") int pageSize);

    int selectTotalCountByDriverIdentityToken(@Param("driverIdentityToken") String driverIdentityToken,@Param("status") Integer status);
}