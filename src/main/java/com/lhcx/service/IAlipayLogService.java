package com.lhcx.service;

import java.util.Map;

import com.lhcx.model.AlipayLog;
import com.lhcx.model.Order;

public interface IAlipayLogService {
    int insertSelective(AlipayLog record);

    AlipayLog selectByOutTradeNo(String outTradeNo);
    
    Order alipayNotify(Map<String, String> parameterMap);
}
