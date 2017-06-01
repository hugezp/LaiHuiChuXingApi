package com.lhcx.service.impl;

import io.netty.handler.codec.http.HttpContentEncoder.Result;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhcx.dao.AlipayLogMapper;
import com.lhcx.model.AlipayLog;
import com.lhcx.service.IAlipayLogService;

@Transactional(rollbackFor=Exception.class)
@Service
public class AlipayLogServiceImpl implements IAlipayLogService{
	@Autowired
	private AlipayLogMapper alipayLogMapper;

	@Override
	public int insertSelective(AlipayLog record) {
		return alipayLogMapper.insertSelective(record);
	}

	@Override
	public AlipayLog selectByOutTradeNo(String outTradeNo) {
		return alipayLogMapper.selectByOutTradeNo(outTradeNo);
	}
	
	@Override
	public boolean alipayNotify(Map<String, String> parameterMap){
		boolean result = false; 
		//1：查询是否已经收到异步通知,如果已收到则停止执行       
		String out_trade_no = parameterMap.get("out_trade_no");
		AlipayLog alipayLog = selectByOutTradeNo(out_trade_no);
    	//2：未收到通知开始创建支付log
		
		return result;
	}

}
