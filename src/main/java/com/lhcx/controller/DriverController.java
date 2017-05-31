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
import com.lhcx.model.DriverInfo;
import com.lhcx.model.Order;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;
import com.lhcx.service.IDriverInfoService;
import com.lhcx.service.IOrderService;
import com.lhcx.utils.DateUtils;
import com.lhcx.utils.Utils;

/**
 * 
 * @author Jiawl
 *
 */
@Controller
@RequestMapping(value = "/driver")
public class DriverController {
	private static Logger log = Logger.getLogger(DriverController.class);
	@Autowired
	private IOrderService orderService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private IDriverInfoService driverInfoService;
	
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
			User user = (User)session.getAttribute("CURRENT_USER");
			Order newOrder = orderService
					.selectNewOrderByDriverPhone(user.getUserphone());
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
	 * 获取司机基本信息
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
			DriverInfo driverInfo = driverInfoService.selectByPhone(user.getUserphone());
			result.put("phone", user.getUserphone());
			result.put("photo", driverInfo.getPhoto());
			result.put("addressName", driverInfo.getAddressname());
			result.put("driverName", driverInfo.getDrivername());
			result.put("licenseId", driverInfo.getLicenseid());
			result.put("driverNation", driverInfo.getDrivernation());
			result.put("driverNationlity", driverInfo.getDrivernationality());
			result.put("address", driverInfo.getAddress());
			
			//证件信息
			result.put("licensePhoto", driverInfo.getLicensephoto());
			result.put("getDriverLicenseDate", DateUtils.dateFormat2(driverInfo.getGetdriverlicensedate()));
			result.put("driverLicenseOn",  DateUtils.dateFormat2(driverInfo.getDriverlicenseon()));
			result.put("driverLicenseOff", DateUtils.dateFormat2(driverInfo.getDriverlicenseoff()));
			result.put("fullTimeDriver", driverInfo.getFulltimedriver());
			result.put("VehicleNo", driverInfo.getVehicleNo());
						
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
			User user = (User) session.getAttribute("CURRENT_USER");
			DriverInfo driverInfo = new DriverInfo(jsonRequest);
			driverInfo.setDriverphone(user.getUserphone());
			driverInfo.setFlag(2);
			driverInfo.setUpdatetime(DateUtils.currentTimestamp());
			driverInfoService.updateByPhoneSelective(driverInfo);
			
			resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
					"更新成功！");

		} catch (Exception e) {
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"更新失败！");
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
	
}
