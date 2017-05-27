package com.lhcx.controller;

import java.util.Date;
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
import com.lhcx.model.PassengerInfo;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;
import com.lhcx.service.IOrderService;
import com.lhcx.service.IPassengerInfoService;
import com.lhcx.utils.Utils;
/**
 * 乘客模块
 * @author pangzhenpeng
 *
 */
@Controller
@RequestMapping(value = "/passenger")
public class PassengerController {
	private static Logger log = Logger.getLogger(PassengerController.class);
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IPassengerInfoService passengerInfoService;
	@Autowired
	private HttpSession session;

	/**
	 * 获取乘客未完成的订单
	 * 
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/order/noFinished", method = RequestMethod.POST)
	public ResponseEntity<String> noFinished() {
		// 取得参数值
		String jsonpCallback = "";
		ResultBean<?> resultBean = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			User user = (User) session.getAttribute("CURRENT_USER");
			Order newOrder = orderService.selectNewOrderByPhone(user
					.getUserphone());
			if (newOrder != null) {
				result.put("OrderId", newOrder.getOrderid());
				result.put("Status", newOrder.getStatus());
			}

			resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
					"获取成功！", result);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"获取失败！");
		}

		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
	
	/**
	 * 获取基本信息
	 * 
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/info", method = RequestMethod.POST)
	public ResponseEntity<String> info() {
		// 取得参数值
		// 取得参数值
		String jsonpCallback = "";
		ResultBean<?> resultBean = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			User user = (User) session.getAttribute("CURRENT_USER");
			PassengerInfo info = passengerInfoService.selectByPhone(user.getUserphone());
			if (info != null) {
				result.put("passengerName", info.getPassengername());
				result.put("passengerGeender", info.getPassengergeender());
			}else {
				result.put("passengerName", "");
				result.put("passengerGeender", "");
			}
			result.put("phone", user.getUserphone());
						
			resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
					"获取成功！",result);
			
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"获取失败！");
		}
		
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
	
	/**
	 * 司机端更新
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<String> update(@RequestBody JSONObject jsonRequest) {
		ResultBean<?> resultBean = null;
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		try {
			String passengerName = jsonRequest.getString("passengerName");
			String passengerGeender = jsonRequest.getString("passengerGeender");
			User user = (User) session.getAttribute("CURRENT_USER");
			PassengerInfo info = passengerInfoService.selectByPhone(user.getUserphone());
			if (info == null) {
				info = new PassengerInfo();
				info.setPassengername(passengerName);
				info.setPassengergeender(passengerGeender);
				info.setRegisterdate(new Date());
				info.setCreatetime(new Date());
				info.setPassengerphone(user.getUserphone());
				info.setFlag(1);
				info.setState(0);
				info.setUpdatetime(new Date());
				passengerInfoService.insertSelective(info);
			} else {
				info.setPassengername(passengerName);
				info.setPassengergeender(passengerGeender);
				info.setFlag(2);
				info.setUpdatetime(new Date());
				passengerInfoService.updateByPhoneSelective(info);
			}
			resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
					"乘客信息提交成功！");

		} catch (Exception e) {
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"提交失败！");
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
	
}
