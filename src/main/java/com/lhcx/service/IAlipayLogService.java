package com.lhcx.service;

import java.util.Map;

import com.lhcx.model.AlipayLog;

public interface IAlipayLogService {
    int insertSelective(AlipayLog record);

    AlipayLog selectByOutTradeNo(String outTradeNo);
    
    boolean alipayNotify(Map<String, String> parameterMap);
}
