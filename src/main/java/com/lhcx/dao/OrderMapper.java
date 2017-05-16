package com.lhcx.dao;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.Order;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);
    
    Order selectByOrderId(@Param("orderId") String orderId);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}