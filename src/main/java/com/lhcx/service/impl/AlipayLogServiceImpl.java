package com.lhcx.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.lhcx.model.User;
import com.lhcx.service.IAlipayLogService;
import com.lhcx.service.IOrderLogService;
import com.lhcx.service.IOrderService;
import com.lhcx.service.IPayCashLogService;
import com.lhcx.service.IUserService;

@Transactional(rollbackFor = Exception.class)
@Service
public class AlipayLogServiceImpl implements IAlipayLogService {
	@Autowired
	private AlipayLogMapper alipayLogMapper;
	@Autowired
	private IOrderLogService orderLogService;
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IPayCashLogService payCashLogService;
	@Autowired
	private HttpSession session;
	@Autowired
	private IUserService userService;

	@Override
	public int insertSelective(AlipayLog record) {
		return alipayLogMapper.insertSelective(record);
	}

	@Override
	public AlipayLog selectByOutTradeNo(String outTradeNo) {
		return alipayLogMapper.selectByOutTradeNo(outTradeNo);
	}

	@Override
	public Order wxNotify(Map<String, String> parameterMap) {
		Order order = null;
		// 1：查询是否已经收到异步通知,如果已收到则停止执行
		String out_trade_no = parameterMap.get("out_trade_no");
		AlipayLog alipayLog = selectByOutTradeNo(out_trade_no);
		if (alipayLog == null) {
			// 2：未收到通知开始创建支付log
			// 2.1:创建网上支付log
			alipayLog = new AlipayLog();
			BigDecimal price = new BigDecimal(Integer.parseInt(parameterMap
					.get("total_fee")) / 100d);
			BigDecimal buyerPayAmount = new BigDecimal(
					Integer.parseInt(parameterMap.get("cash_fee")) / 100d);
			alipayLog.setPrice(price);
			alipayLog.setBuyerPayAmount(buyerPayAmount);
			alipayLog.setTradeNo(parameterMap.get("transaction_id"));
			alipayLog.setOutTradeNo(out_trade_no);
			alipayLog.setTradeStatus(parameterMap.get("result_code"));
			alipayLog.setBuyerId(parameterMap.get("openid"));
			alipayLog.setSellerId(parameterMap.get("mch_id"));
			insertSelective(alipayLog);
			// 2.2：订单更新日志
			order = orderService.selectByOrderId(out_trade_no);
			order.setOldstatus(order.getStatus());
			order.setStatus(OrderStatus.PAY.value());
			orderService.updateByOrderIdSelective(order);

			OrderLog orderLog = new OrderLog();
			orderLog.setOrderid(out_trade_no);
			orderLog.setOperatorphone(order.getPassengerphone());
			orderLog.setIdentityToken(order.getPassengerIdentityToken());
			orderLog.setOperatortime(new Date());
			orderLog.setOperatorstatus(OrderStatus.PAY.value());
			orderLog.setOperatordescription(OrderStatus.PAY.message());
			orderLogService.insertSelective(orderLog);
			// 2.3：创建账户流水线日志：乘客支出，司机收入
			PayCashLog driverCashLog = new PayCashLog();
			driverCashLog.setOrderid(out_trade_no);
			driverCashLog.setIdentityToken(order.getDriverIdentityToken());
			driverCashLog.setCash(price);
			driverCashLog.setPaytype(1);// 微信支付
			driverCashLog.setStatus(2);// 支付完成
			String passengerPhone = order.getPassengerphone();
			String description = "收到车费" + price + "元，来自手机尾号"
					+ passengerPhone.substring(7);
			driverCashLog.setActiontype(PayActionType.income.value());
			driverCashLog.setDescription(description);
			payCashLogService.insertSelective(driverCashLog);
			User user = userService.selectUserByPhone(order.getDriverphone(),
					"driver");
			user.setWallet(user.getWallet() + price.doubleValue());
			user.setIdentityToken(order.getDriverIdentityToken());
			userService.updateWalletByIdToken(user);
			PayCashLog passengerCashLog = new PayCashLog();
			passengerCashLog.setOrderid(out_trade_no);
			passengerCashLog
					.setIdentityToken(order.getPassengerIdentityToken());
			passengerCashLog.setCash(price);
			passengerCashLog.setPaytype(1);
			passengerCashLog.setStatus(2);
			String driverPhone = order.getDriverphone();
			String description1 = "支付车费" + price + "元，给手机尾号"
					+ driverPhone.substring(7) + "的车主";
			passengerCashLog.setActiontype(PayActionType.spending.value());
			passengerCashLog.setDescription(description1);
			payCashLogService.insertSelective(passengerCashLog);
		}
		return order;
	}

	@Override
	public List<AlipayLog> selectList(AlipayLog alipayLog) {
		return alipayLogMapper.selectList(alipayLog);
	}

	@Override
	public Order alipayNotify(HttpServletRequest request) {
		Order order = null;
		// 1：查询是否已经收到异步通知,如果已收到则停止执行
		String out_trade_no = request.getParameter("out_trade_no");
		AlipayLog alipayLog = selectByOutTradeNo(out_trade_no);
		if (alipayLog == null) {
			order = orderService.selectByOrderId(out_trade_no);
			order.setOldstatus(order.getStatus());
			order.setStatus(OrderStatus.PAY.value());
			orderService.updateByOrderIdSelective(order);
			
			OrderLog orderLog = new OrderLog();
			orderLog.setOrderid(out_trade_no);
			orderLog.setOperatorphone(order.getPassengerphone());
			orderLog.setIdentityToken(order.getPassengerIdentityToken());
			orderLog.setOperatortime(new Date());
			orderLog.setOperatorstatus(OrderStatus.PAY.value());
			orderLog.setOperatordescription(OrderStatus.PAY.message());
			orderLogService.insertSelective(orderLog);
			//2.3：创建账户流水线日志：乘客支出，司机收入
			String passengerPhone = order.getPassengerphone();
			String price = request.getParameter("total_amount");
			String description = "收到车费" + new BigDecimal(price) + "元，来自手机尾号" + passengerPhone.substring(7);
			PayCashLog driverCashLog = new PayCashLog();
			driverCashLog.setOrderid(out_trade_no);
			driverCashLog.setIdentityToken(order.getDriverIdentityToken());
			driverCashLog.setCash(new BigDecimal(price));
			driverCashLog.setPaytype(0);//支付宝支付
			driverCashLog.setStatus(2);//支付完成
			driverCashLog.setActiontype(PayActionType.income.value());
			driverCashLog.setDescription(description);
			payCashLogService.insertSelective(driverCashLog);
			User user = userService.selectUserByPhone(order.getDriverphone(),
					"driver");
			user.setWallet(user.getWallet() + Double.valueOf(price));
			user.setIdentityToken(order.getDriverIdentityToken());
			userService.updateWalletByIdToken(user);
			PayCashLog passengerCashLog = new PayCashLog();
			passengerCashLog.setOrderid(out_trade_no);
			passengerCashLog.setIdentityToken(order.getPassengerIdentityToken());
			passengerCashLog.setCash(new BigDecimal(price));
			passengerCashLog.setPaytype(1);
			passengerCashLog.setStatus(2);
			String driverPhone = order.getDriverphone();
			String description1 = "支付车费" + price + "元，给手机尾号" + driverPhone.substring(7) + "的车主";
			passengerCashLog.setActiontype(PayActionType.spending.value());
			passengerCashLog.setDescription(description1);
			payCashLogService.insertSelective(passengerCashLog);
		}
		return order;
	}

}
