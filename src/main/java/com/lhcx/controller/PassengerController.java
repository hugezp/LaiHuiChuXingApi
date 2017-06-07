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
import com.lhcx.utils.DateUtils;
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
			Order newOrder = orderService.selectNewOrderByPassengerIdentityToken(user.getIdentityToken());
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
		String jsonpCallback = "";
		ResultBean<?> resultBean = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			User user = (User) session.getAttribute("CURRENT_USER");
			PassengerInfo info = passengerInfoService.selectByIdentityToken(user.getIdentityToken());
			if (info != null) {
				result.put("passengerName", info.getPassengername());
				result.put("passengerGeender", info.getPassengergeender());
				result.put("photo", info.getPhoto());
				result.put("description", info.getDescription());
				Date birthDay = info.getBirthDay();
				String birthDayFormat = "";
				if (birthDay != null) {
					birthDayFormat = DateUtils.dateFormat(birthDay);
				}
				result.put("birthDay", birthDayFormat);
				result.put("homeAddress", info.getHomeAddress());
				result.put("contactAddress", info.getContactAddress());
				result.put("company", info.getCompany());
			}else {
				result.put("passengerName", "");
				result.put("passengerGeender", "");
				result.put("passengerName", "");
				result.put("passengerGeender", "");
				result.put("photo", "");
				result.put("description", "");
				result.put("birthDay", "");
				result.put("homeAddress", "");
				result.put("contactAddress", "");
				result.put("company", "");
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
	 * 乘客端更新
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
			String photo = jsonRequest.getString("photo");
			Long birthDay =  jsonRequest.getLong("birthDay");
			String description =  jsonRequest.getString("description");
			String contactAddress =  jsonRequest.getString("contactAddress");
			String homeAddress =  jsonRequest.getString("homeAddress");
			String company =  jsonRequest.getString("company");
			
			User user = (User) session.getAttribute("CURRENT_USER");
			String identityToken = user.getIdentityToken();
			PassengerInfo info = passengerInfoService.selectByIdentityToken(identityToken);
			if (info == null) {
				info = new PassengerInfo();
				info.setPassengername(passengerName);
				info.setPassengergeender(passengerGeender);
				info.setPassengerphone(user.getUserphone());
				info.setIdentityToken(identityToken);
				info.setFlag(1);
				info.setState(0);
				info.setPhoto(photo);
				info.setCompany(company);
				info.setBirthDay(DateUtils.toDate(birthDay));
				info.setContactAddress(contactAddress);
				info.setHomeAddress(homeAddress);
				info.setDescription(description);
				passengerInfoService.insertSelective(info);
			} else {
				info.setPhoto(photo);
				info.setCompany(company);
				info.setBirthDay(DateUtils.toDate(birthDay));
				info.setContactAddress(contactAddress);
				info.setHomeAddress(homeAddress);
				info.setDescription(description);
				info.setPassengername(passengerName);
				info.setPassengergeender(passengerGeender);
				info.setFlag(2);
				passengerInfoService.updateByIdentityTokenSelective(info);
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
