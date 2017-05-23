package com.lhcx.controller;

import java.util.HashMap;
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
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;
import com.lhcx.model.UserType;
import com.lhcx.service.IUserService;
import com.lhcx.service.IVerificationCodeService;
import com.lhcx.utils.Utils;
import com.lhcx.utils.VerificationUtils;

/**
 * 用户登录与注册
 * 
 * @author william
 *
 */

@Controller
@RequestMapping(value = "/api/user")
public class UserController {
	private static Logger log = Logger.getLogger(UserController.class);
	@Autowired
	private IVerificationCodeService verificationCodeService;
	@Autowired
	private IUserService userSerive;
	@Autowired
	private HttpSession session;
	@Autowired
	private HttpServletRequest request;

	/**
	 * 获取验证码 content-type:application/json
	 * 
	 * @param: mobile:手机号 userType:用户类型，driver-司机端，passenger-乘客端 code:验证码
	 *         jsonpCallback：跨域参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<String> sendPhoneCode(
			@RequestBody JSONObject jsonRequest) {
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");

		ResultBean<?> resultBean = null;
		if (!VerificationUtils.login(jsonRequest)) {
			resultBean = new ResultBean<Object>(
					ResponseCode.PARAMETER_WRONG.value(),
					ResponseCode.PARAMETER_WRONG.message());
		}else {
			try {
				resultBean = userSerive.login(request, jsonRequest);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
				resultBean = new ResultBean<Object>(
						ResponseCode.SMS_CHECKED_FAILED.value(),
						ResponseCode.SMS_CHECKED_FAILED.message());
			}
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

	/**
	 * 司机端注册 content-type:application/json
	 * 
	 * @param: mobile:手机号 userType:用户类型，driver-司机端，passenger-乘客端 code:验证码
	 *         jsonpCallback：跨域参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "registerForDriver", method = RequestMethod.POST)
	public ResponseEntity<String> registerForDriver(HttpServletRequest request,
			@RequestBody JSONObject jsonRequest) {
		ResultBean<?> resultBean = null;
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		String phone = jsonRequest.getString("phone");
		String userType = UserType.DRIVER.value();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String checkSession = (String) session
					.getAttribute(Utils.REGISTER_PHONE_SESSION);
			String sessionString = userType + "@" + phone;
			if (sessionString.equals(checkSession)) {
				User user = userSerive.registerForDriver(request, jsonRequest);
				if (user != null) {
					result.put("phone", phone);
					result.put("userType", userType);
					result.put("token", user.getToken());
					resultBean = new ResultBean<Object>(
							ResponseCode.SUCCESS.value(), ResponseCode.SUCCESS.message(), result);
				} else {
					resultBean = new ResultBean<Object>(
							ResponseCode.REGISTER_FAILED.value(), ResponseCode.REGISTER_FAILED.message());
				}

			} else {
				resultBean = new ResultBean<Object>(ResponseCode.SMS_CHECKED_FAILED.value(),
						ResponseCode.SMS_CHECKED_FAILED.message());
			}

		} catch (Exception e) {
			log.error("register error by :" + e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					ResponseCode.ERROR.message());
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

	@ResponseBody
	@RequestMapping(value = "/noLogin", method = RequestMethod.GET)
	public ResponseEntity<String> noLogin1() {
		ResultBean<?> resultBean = new ResultBean<Object>(
				ResponseCode.LOGIN_FAILED.value(), "未登录或登录已失效，请重新登录！");
		return Utils.resultResponseJson(resultBean, null);
	}

	@ResponseBody
	@RequestMapping(value = "/noLogin", method = RequestMethod.POST)
	public ResponseEntity<String> noLogin2() {
		ResultBean<?> resultBean = new ResultBean<Object>(
				ResponseCode.LOGIN_FAILED.value(), "未登录或登录已失效，请重新登录！");
		return Utils.resultResponseJson(resultBean, null);
	}

}
