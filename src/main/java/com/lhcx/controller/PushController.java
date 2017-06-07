package com.lhcx.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.lhcx.utils.DateUtils;
import com.lhcx.utils.Utils;
import com.lhcx.utils.VerificationUtils;

/**
 * 推送相关
 * @author YangGuang
 *
 */
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
		} else {
			try {
				User user = (User) session.getAttribute("CURRENT_USER");
				String identityToken = user.getIdentityToken();
				DriverInfo driverInfo = driverInfoService.selectByIdentityToken(identityToken);
				if (driverInfo == null || user.getFlag() != 2) {
					// 司机信息审核未通过
					resultBean = new ResultBean<Object>(
							ResponseCode.DRIVER_INVALID.value(),
							ResponseCode.DRIVER_INVALID.message());
				} else {
					if (driverLocationService.setButton(jsonRequest)) {
						resultBean = new ResultBean<Object>(
								ResponseCode.SUCCESS.value(),
								ResponseCode.SUCCESS.message());
					} else {
						resultBean = new ResultBean<Object>(
								ResponseCode.PUSH_BUTTON_FAILED.value(),
								ResponseCode.PUSH_BUTTON_FAILED.message());
					}
				}

			} catch (Exception e) {
				log.error(e.getMessage());
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
		} else {
			try {
				String longitude = jsonRequest.getString("Longitude");
				String latitude = jsonRequest.getString("Latitude");
				User user = (User) session.getAttribute("CURRENT_USER");
				String identityToken = user.getIdentityToken();
				DriverLocation driverLocation = new DriverLocation();
				driverLocation.setPhone(user.getUserphone());
				driverLocation.setIdentityToken(identityToken);
				driverLocation.setLongitude(longitude);
				driverLocation.setLatitude(latitude);
				driverLocation.setPositiontime(new Date());
				int flag = driverLocationService.updateLocation(driverLocation);
				if (flag > 0) {
					resultBean = new ResultBean<Object>(
							ResponseCode.SUCCESS.value(),
							ResponseCode.SUCCESS.message());
				} else {
					resultBean = new ResultBean<Object>(
							ResponseCode.PUSH_UPDATE_FAILED.value(),
							ResponseCode.PUSH_UPDATE_FAILED.message());
				}
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
				resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						ResponseCode.ERROR.message());
			}
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

	/**
	 * 获取推送列表
	 */
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public ResponseEntity<String> list(@RequestBody JSONObject jsonRequest) {
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (!VerificationUtils.pushList(jsonRequest)) {
			resultBean = new ResultBean<Object>(ResponseCode.PARAMETER_WRONG.value(),
					ResponseCode.PARAMETER_WRONG.message(),resultList);
		}else {
			User user = (User) session.getAttribute("CURRENT_USER");
			String userToken = user.getIdentityToken();
			String pushType = jsonRequest.getString("pushType");
			String page = jsonRequest.getString("page");
			String size = jsonRequest.getString("size");
			PushNotification pushNotification = new PushNotification();
			pushNotification.setReceivePhone(user.getUserphone());
			pushNotification.setReceiveIdentityToken(userToken);
			pushNotification.setPage(Integer.parseInt(page));
			pushNotification.setSize(Integer.parseInt(size));
			pushNotification.setPushType(Integer.parseInt(pushType));
			List<PushNotification> pushList = pushNotificationService.selectAll(pushNotification);
			if (pushList.size()>0) {
				for (PushNotification push : pushList) {
					Map<String, Object> resultMap = new HashMap<String, Object>();
					resultMap.put("alert", push.getAlert());
					resultMap.put("time", DateUtils.pushDate(push.getTime()));
					resultMap.put("flag", push.getFlag());
					resultMap.put("linkUrl",push.getLinkUrl() == null?"":push.getLinkUrl());
					resultList.add(resultMap);
				}
				resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
						ResponseCode.SUCCESS.message(),resultList);
			}else {
				resultBean = new ResultBean<Object>(ResponseCode.NO_DATA.value(),
						ResponseCode.NO_DATA.message(),resultList);
			}
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
	
	/**
	 * 听单设置
	 */
	@ResponseBody
	@RequestMapping(value = "/setPush", method = RequestMethod.POST)
	public ResponseEntity<String> setPush(@RequestBody JSONObject jsonRequest) {
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		if (!VerificationUtils.setPush(jsonRequest)) {
			resultBean = new ResultBean<Object>(ResponseCode.PARAMETER_WRONG.value(),
					ResponseCode.PARAMETER_WRONG.message());
		}else {
			User user = (User) session.getAttribute("CURRENT_USER");
			DriverLocation driverLocation = new DriverLocation();
			driverLocation.setIdentityToken(user.getIdentityToken());
			driverLocation.setPreference(Integer.parseInt(jsonRequest.getString("preference")));
			driverLocation.setScope(Integer.parseInt(jsonRequest.getString("scope")));
			int count = driverLocationService.updatePush(driverLocation);
			if (count > 0) {
				resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
						ResponseCode.SUCCESS.message());
			}else {
				resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						ResponseCode.ERROR.message());
			}
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
}
