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
	private HttpSession session;

	/**
	 * 获取乘客未完成的订单
	 * 
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/order/noFinished", method = RequestMethod.POST)
	public ResponseEntity<String> info() {
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
	 * 完善个人信息
	 * @param passengerName 乘客姓名
	 * @param passengerGeender 乘客性别
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/add/info", method = RequestMethod.POST)
	public ResponseEntity<String> add(@RequestBody JSONObject jsonRequest) {
		// 取得参数值
		String jsonpCallback = "";
		String passengerName = jsonRequest.getString("passengerName");
		String passengerGeender = jsonRequest.getString("passengerGeender");
		PassengerInfo passengerInfo = new PassengerInfo();
		ResultBean<?> resultBean = null;
//		Map<String, Object> result = new HashMap<String, Object>();
		try {
			User user = (User) session.getAttribute("CURRENT_USER");
			passengerInfo.setPassengername(passengerName);
			passengerInfo.setPassengergeender(passengerGeender);
			passengerInfo.setRegisterdate(new Date());
			passengerInfo.setCreatetime(new Date());
			passengerInfo.setPassengerphone(user.getUserphone());
			passengerInfo.setFlag(1);
			passengerInfo.setState(0);
			passengerInfo.setUpdatetime(new Date());
			passengerInfoService.insertSelective(passengerInfo);
			resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
					"用户信息添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"用户信息添加失败！");
		}

		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
}
