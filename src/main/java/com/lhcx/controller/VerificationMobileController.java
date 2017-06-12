package com.lhcx.controller;

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
import com.lhcx.utils.TestConfig;
import com.lhcx.utils.Utils;
import com.lhcx.utils.VerificationUtils;
import com.lhcx.utils.SMSUtils.SmsWebApiKit;

/**
 * 手机号验证
 * 
 * @author pangzhenpeng
 *
 */
@Controller
@RequestMapping(value = "/api")
public class VerificationMobileController {
	private static Logger log = Logger
			.getLogger(VerificationMobileController.class);

	@Autowired
	private HttpSession session;

	/**
	 * 验证手机验证码
	 * 
	 * @param: mobile:手机号 userType:用户类型，driver-司机端，passenger-乘客端 code:手机验证码
	 *         jsonpCallback：跨域参数 appMark:APP标识
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/verification", method = RequestMethod.POST)
	public ResponseEntity<String> PassengerVerification(
			HttpServletRequest request, @RequestBody JSONObject jsonRequest) {

		// 取得参数值
		String mobile = jsonRequest.getString("phone");
		String userType = jsonRequest.getString("userType");
		String code = jsonRequest.getString("code");
		String actionType = jsonRequest.getString("actionType");
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		String source = jsonRequest.getString("source");
		ResultBean<?> resultBean = null;
		int count = VerificationUtils.checkPhoneCodeValidation(jsonRequest);
		if (count == -1) {
			resultBean = new ResultBean<Object>(
					ResponseCode.PARAMETER_WRONG.value(),
					ResponseCode.PARAMETER_WRONG.message());
			return Utils.resultResponseJson(resultBean, jsonpCallback);
		} else if (count == 0) {
			resultBean = new ResultBean<Object>(
					ResponseCode.PHONE_FORMAT_ERROR.value(),
					ResponseCode.PHONE_FORMAT_ERROR.message());
			return Utils.resultResponseJson(resultBean, jsonpCallback);
		}
		try {
			if (actionType == null) {
				actionType = "register";
				session.setAttribute(Utils.REGISTER_PHONE_SESSION, userType
						+ "@" + mobile);
			} else if ("checkOldPhone".equals(actionType)) {
				// 验证老的手机号
				session.setAttribute("check@OldPhone", userType + "@" + mobile);
			} else if ("checkNewPhone".equals(actionType)) {
				// 验证新的手机号
				session.setAttribute("check@NewPhone", userType + "@" + mobile);
			}
			 String status = SmsWebApiKit.getInstance().checkcode(mobile, "86", code,
					userType, source);
			if (TestConfig.testMobile(mobile)) {
				status = "200";
			}
			if (status.equals("200")) {
				resultBean = new ResultBean<Object>(
						ResponseCode.SUCCESS.value(),
						ResponseCode.SUCCESS.message());
				return Utils.resultResponseJson(resultBean, jsonpCallback);
			} else {
				resultBean = new ResultBean<Object>(
						ResponseCode.SMS_CHECKED_FAILED.value(),
						ResponseCode.SMS_CHECKED_FAILED.message());
				return Utils.resultResponseJson(resultBean, jsonpCallback);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					ResponseCode.ERROR.message());
			return Utils.resultResponseJson(resultBean, jsonpCallback);
		}
	}

}
