package com.lhcx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.OrderLog;

public interface OrderLogMapper {
 
    int insertSelective(OrderLog record);

    List<OrderLog> selectByOrderId(String orderid);
    
    OrderLog selectByIdentityToken(@Param("identityToken") String identityToken);
}