package com.lhcx.dao;

import java.util.List;

import com.lhcx.model.AlipayLog;

public interface AlipayLogMapper {

    int insertSelective(AlipayLog record);

    AlipayLog selectByOutTradeNo(String outTradeNo);

    int updateByOutTradeNoSelective(AlipayLog record);
    
    List<AlipayLog> selectList(AlipayLog alipayLog);

}