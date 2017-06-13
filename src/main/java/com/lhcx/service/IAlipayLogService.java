package com.lhcx.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lhcx.model.AlipayLog;
import com.lhcx.model.Order;

public interface IAlipayLogService {
    int insertSelective(AlipayLog record);

    AlipayLog selectByOutTradeNo(String outTradeNo);
    
    Order wxNotify(Map<String, String> parameterMap);
    
    Order alipayNotify(HttpServletRequest request);
    
    List<AlipayLog> selectList(AlipayLog alipayLog);
}
