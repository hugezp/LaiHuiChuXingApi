package com.lhcx.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhcx.dao.AlipayLogMapper;
import com.lhcx.model.AlipayLog;
import com.lhcx.model.Order;
import com.lhcx.model.OrderLog;
import com.lhcx.model.OrderStatus;
import com.lhcx.model.PayActionType;
import com.lhcx.model.PayCashLog;
import com.lhcx.service.IAlipayLogService;
import com.lhcx.service.IOrderLogService;
import com.lhcx.service.IOrderService;
import com.lhcx.service.IPayCashLogService;

@Transactional(rollbackFor=Exception.class)
@Service
public class AlipayLogServiceImpl implements IAlipayLogService{
	@Autowired
	private AlipayLogMapper alipayLogMapper;
	@Autowired
	private IOrderLogService orderLogService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IPayCashLogService payCashLogService;

	@Override
	public int insertSelective(AlipayLog record) {
		return alipayLogMapper.insertSelective(record);
	}

	@Override
	public AlipayLog selectByOutTradeNo(String outTradeNo) {
		return alipayLogMapper.selectByOutTradeNo(outTradeNo);
	}
	
	@Override
	public Order alipayNotify(Map<String, String> parameterMap){
		Order order = null;
		//1：查询是否已经收到异步通知,如果已收到则停止执行       
		String out_trade_no = parameterMap.get("out_trade_no");
		AlipayLog alipayLog = selectByOutTradeNo(out_trade_no);
		if (alipayLog == null) {
			//2：未收到通知开始创建支付log
			//2.1:创建网上支付log
			alipayLog = new AlipayLog();
			BigDecimal price = new BigDecimal(Integer.parseInt(parameterMap.get("total_fee"))/100d);
			BigDecimal buyerPayAmount = new BigDecimal(Integer.parseInt(parameterMap.get("cash_fee"))/100d);
			
			alipayLog.setPrice(price);
			alipayLog.setBuyerPayAmount(buyerPayAmount);
			alipayLog.setTradeNo(parameterMap.get("transaction_id"));
			alipayLog.setOutTradeNo(out_trade_no);
			alipayLog.setTradeStatus(parameterMap.get("result_code"));
			alipayLog.setBuyerId(parameterMap.get("openid"));
			alipayLog.setSellerId(parameterMap.get("mch_id"));
			insertSelective(alipayLog);
			//2.2：订单更新日志
			order = orderService.selectByOrderId(out_trade_no);
			order.setOldstatus(order.getStatus());
			order.setStatus(OrderStatus.PAY.value());
			orderService.updateByOrderIdSelective(order);
			OrderLog orderLog = new OrderLog();
			
			orderLog.setOrderid(out_trade_no);
			orderLog.setOperatorphone(order.getPassengerphone());
			orderLog.setOperatortime(new Date());
			orderLog.setOperatorstatus(OrderStatus.PAY.value());
			orderLog.setOperatordescription(OrderStatus.PAY.message());
			orderLogService.insertSelective(orderLog);
			//2.3：创建账户流水线日志：乘客支出，司机收入
			PayCashLog cashLog = new PayCashLog();
			cashLog.setOrderid(out_trade_no);
			cashLog.setPassengerphone(order.getPassengerphone());
			cashLog.setDriverphone(order.getDriverphone());
			cashLog.setCash(price);
			cashLog.setPaytype(1);//微信支付
			cashLog.setStatus(2);//支付完成
			
			cashLog.setActiontype(PayActionType.spending.value());//乘客支出
			cashLog.setDescription("乘客微信支付记录");
			payCashLogService.insertSelective(cashLog);
			
			cashLog.setActiontype(PayActionType.income.value());//司机收入
			cashLog.setDescription("司机微信收入记录");
			payCashLogService.insertSelective(cashLog);
			
		}
		return order;
	}

	@Override
	public List<AlipayLog> selectList(AlipayLog alipayLog) {
		return alipayLogMapper.selectList(alipayLog);
	}

}
