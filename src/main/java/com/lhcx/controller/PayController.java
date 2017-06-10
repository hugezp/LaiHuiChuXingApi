package com.lhcx.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.Order;
import com.lhcx.model.OrderLog;
import com.lhcx.model.OrderStatus;
import com.lhcx.model.PushNotification;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.service.IOrderLogService;
import com.lhcx.service.IOrderService;
import com.lhcx.service.IPayService;
import com.lhcx.service.IPushNotificationService;
import com.lhcx.utils.ConfigUtils;
import com.lhcx.utils.JpushClientUtil;
import com.lhcx.utils.Utils;
import com.lhcx.utils.VerificationUtils;

/**
 * 
 * @author Jiawl
 *
 */
@Controller
@RequestMapping(value = "/pay")
public class PayController {
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IOrderLogService orderLogService;
	@Autowired
	private IPayService payService;
	@Autowired
	private IPushNotificationService pushNotificationService;
	
	@Autowired
	private HttpSession session;
	
	/**
	 * 支付订单
	 * 
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/unfiedorder", method = RequestMethod.POST)
	public ResponseEntity<String> unfiedorder(@RequestBody JSONObject jsonRequest) {
		ResultBean<?> resultBean = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String orderId = jsonRequest.getString("orderId");
	        int payType = jsonRequest.getInteger("payType");
 
	        if (!VerificationUtils.payValidation(jsonRequest)) {
	        	resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						"支付请求参数错误！");
	        	return Utils.resultResponseJson(resultBean, null);
			}
	        
	        Order order = orderService.selectByOrderId(orderId);
	        if (order == null || order.getStatus() == OrderStatus.PAY.value()) {
	        	resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						"支付失败，支付订单不存在或订单已支付！");
	        	return Utils.resultResponseJson(resultBean, null);
			}
	        
	        if (payType == 0) {
				//微信支付
	        	result = payService.payForWX(order);
	        	resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
						"微信下单成功！",result);
			} else if (payType == 1){
				//支付宝支付
				String json = payService.payForAlipay(order);
				result.put("AlipayData", json);
				resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
						"支付宝下单成功！",result);
			}else{
				resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						"支付失败，支付类型错误！");
			}
	        
		} catch (Exception e) {
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"订单支付失败！");
		}
		
		return Utils.resultResponseJson(resultBean, null);
	}
	
	/**
	 * 支付订单
	 * 
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/cashPayments", method = RequestMethod.POST)
	public ResponseEntity<String> cashPayments(@RequestBody JSONObject jsonRequest) {
		ResultBean<?> resultBean = null;
		String orderId = jsonRequest.getString("orderId");
		try {
			//更新订单状态
			Order order = orderService.selectByOrderId(orderId);
			order.setOldstatus(order.getStatus());
			order.setStatus(OrderStatus.PAY.value());
			orderService.updateByOrderIdSelective(order);
			
			//订单更新日志
			OrderLog orderLog = new OrderLog();
			orderLog.setOrderid(orderId);
			orderLog.setOperatorphone(order.getPassengerphone());
			orderLog.setIdentityToken(order.getPassengerIdentityToken());
			orderLog.setOperatortime(new Date());
			orderLog.setOperatorstatus(OrderStatus.PAY.value());
			orderLog.setOperatordescription("现金支付");
			orderLogService.insertSelective(orderLog);
			
			// 3：推送给司机
			String driverPhone = order.getDriverphone();
			String passengerPhone = order.getPassengerphone();
			String driverIdentityToken = order.getDriverIdentityToken();
			String passengerIdentityToken = order.getPassengerIdentityToken();
			BigDecimal price = order.getFee();
			String content = "【来回出行】手机号为"
					+ passengerPhone
					+ "的乘客通过现金支付了"
					+ price
					+ "元。请查验，订单编号为：" + orderId;

			Map<String, String> extrasParam = new HashMap<String, String>();
			extrasParam.put("OrderId", orderId);
			extrasParam.put("orderStatus", "6");

			int count = JpushClientUtil.getInstance(
					ConfigUtils.JPUSH_APP_KEY,
					ConfigUtils.JPUSH_MASTER_SECRET)
					.sendToRegistrationId("11", driverPhone,
							content, content, content, extrasParam);

			if (count == 1) {
				PushNotification pushNotification = new PushNotification();
				pushNotification.setPushPhone(driverPhone);
				pushNotification.setReceivePhone(passengerPhone);
				pushNotification.setPushIdentityToken(passengerIdentityToken);
				pushNotification.setReceiveIdentityToken(driverIdentityToken);
				pushNotification.setOrderId(orderId);
				pushNotification.setAlert(content);
				pushNotification.setPushType(1);
				pushNotification.setData(extrasParam.toString());
				pushNotification.setFlag(3);
				pushNotificationService.insertSelective(pushNotification);
			}
			
			resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
					ResponseCode.SUCCESS.message());
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					ResponseCode.ERROR.message());
		}
		return Utils.resultResponseJson(resultBean, null);
	}
	
}
