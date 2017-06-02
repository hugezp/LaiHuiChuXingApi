package com.lhcx.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import com.lhcx.model.DriverLocation;
import com.lhcx.model.PushNotification;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;
import com.lhcx.service.IDriverInfoService;
import com.lhcx.service.IDriverLocationService;
import com.lhcx.service.IPushNotificationService;
import com.lhcx.utils.Utils;
import com.lhcx.utils.VerificationUtils;

/**
 * @author dp 通过获取车主的token,查出关于车主的列表信息
 * */

@Controller
@RequestMapping(value = "/push")
public class PushController {
	private static Logger log = Logger.getLogger(PushController.class);
	@Autowired
	IPushNotificationService pushNotificationService;
	@Autowired
	private IDriverLocationService driverLocationService;
	@Autowired
	private HttpSession session;
	@Autowired
	private IDriverInfoService driverInfoService;

	@ResponseBody
	@RequestMapping(value = "/List", method = RequestMethod.POST)
	public ResponseEntity<String> PushList(@RequestBody JSONObject jsonRequest,
			HttpServletRequest request) {
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;

		try {
			User user = (User) session.getAttribute("CURRENT_USER");
			String phone = user.getUserphone();
			List<PushNotification> pushs = pushNotificationService
					.selectAll(phone);
			ArrayList<HashMap<String, Object>> jsonArray = new ArrayList<HashMap<String, Object>>();
			if (pushs.size() > 0) {
				for (PushNotification push : pushs) {
					HashMap<String, Object> receive = new HashMap<String, Object>();
					receive.put("pushId", push.getId());
					receive.put("content", push.getAlert());
					receive.put("orderId", push.getOrderId());
					jsonArray.add(receive);
				}
				resultBean = new ResultBean<Object>(
						ResponseCode.SUCCESS.value(),
						ResponseCode.SUCCESS.message(), jsonArray);
			} else {
				resultBean = new ResultBean<Object>(
						ResponseCode.NO_DATA.value(),
						ResponseCode.NO_DATA.message());
			}
		} catch (Exception e) {
			log.error("create order error by :" + e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					ResponseCode.ERROR.message());
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

	/**
	 * 推送开关
	 */
	@ResponseBody
	@RequestMapping(value = "/button", method = RequestMethod.POST)
	public ResponseEntity<String> pushButton(
			@RequestBody JSONObject jsonRequest, HttpServletRequest request) {
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		if (!VerificationUtils.pushButtonValidation(jsonRequest)) {
			resultBean = new ResultBean<Object>(
					ResponseCode.PARAMETER_WRONG.value(),
					ResponseCode.PARAMETER_WRONG.message());
		}else {
			try {
				User user = (User) session.getAttribute("CURRENT_USER");
				DriverInfo driverInfo = driverInfoService.selectByPhone(user.getUserphone());
				if(driverInfo == null || driverInfo.getState() != 2){
					//司机信息审核未通过
					resultBean = new ResultBean<Object>(ResponseCode.DRIVER_INVALID.value(),
							ResponseCode.DRIVER_INVALID.message());
				}else {
					if (driverLocationService.setButton(jsonRequest,
							user.getUserphone())) {
						resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
								ResponseCode.SUCCESS.message());
					} else {
						resultBean = new ResultBean<Object>(ResponseCode.PUSH_BUTTON_FAILED.value(),
								ResponseCode.PUSH_BUTTON_FAILED.message());
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						ResponseCode.ERROR.message());
			}
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

	/**
	 * 实时更新经纬度
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<String> update(@RequestBody JSONObject jsonRequest) {
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		if (!VerificationUtils.updateValidation(jsonRequest)) {
			resultBean = new ResultBean<Object>(
					ResponseCode.PARAMETER_WRONG.value(),
					ResponseCode.PARAMETER_WRONG.message());
		}else {
			try {
				String longitude = jsonRequest.getString("Longitude");
				String latitude = jsonRequest.getString("Latitude");
				User user = (User) session.getAttribute("CURRENT_USER");
				DriverLocation driverLocation = new DriverLocation();
				driverLocation.setPhone(user.getUserphone());
				driverLocation.setLongitude(longitude);
				driverLocation.setLatitude(latitude);
				driverLocation.setPositiontime(new Date());
				int flag = driverLocationService
						.updateLocation(driverLocation);
				if (flag > 0) {
					resultBean = new ResultBean<Object>(
							ResponseCode.SUCCESS.value(),
							ResponseCode.SUCCESS.message());
				} else {
					resultBean = new ResultBean<Object>(ResponseCode.PUSH_UPDATE_FAILED.value(),
							ResponseCode.PUSH_UPDATE_FAILED.message());
				}
			} catch (Exception e) {
				resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						ResponseCode.ERROR.message());
			}
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
}
