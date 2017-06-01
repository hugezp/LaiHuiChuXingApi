package com.lhcx.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.Order;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.service.IOrderService;
import com.lhcx.service.IPayService;
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
	private static Logger log = Logger.getLogger(PayController.class);
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IPayService payService;
	
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
	        String flag = jsonRequest.getString("flag");
	        
	        if (!VerificationUtils.payValidation(jsonRequest)) {
	        	resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						"支付请求参数错误！");
	        	return Utils.resultResponseJson(resultBean, null);
			}
	        
	        Order order = orderService.selectByOrderId(orderId);
	        if (order == null ) {
	        	resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						"支付失败，支付订单不存在！");
	        	return Utils.resultResponseJson(resultBean, null);
			}
	        
	        if (payType == 0) {
				//微信支付
	        	result = payService.payForWX(order);
	        	resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						"微信下单成功！",result);
			} else if (payType == 1){
				//支付宝支付
				
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
}
