package com.lhcx.controller;

import javax.servlet.http.HttpServletRequest;

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
import com.lhcx.service.IVerificationCodeService;
import com.lhcx.utils.SmsUtils;
import com.lhcx.utils.Utils;
import com.lhcx.utils.VerificationUtils;

/**
 * 手机验证码
 * 
 * @author william
 *
 */

@Controller
@RequestMapping(value = "/api")
public class VerificationCodeController {
	private static Logger log = Logger
			.getLogger(VerificationCodeController.class);
	@Autowired
	private IVerificationCodeService verificationCodeService;
	// 自定义userid
	/*private String userid = "lhcx@2017";*/

	/**
	 * geetest 滑动验证模块 客户端第一次请求，从第三方取得数据返回给客户端
	 * 
	 * @param request
	 * @return
	 *//*
	@RequestMapping(value = "/verify/address", method = RequestMethod.GET)
	public ResponseEntity<String> address(HttpServletRequest request,
			String callBack) {
		GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(),
				GeetestConfig.getGeetest_key(), GeetestConfig.isnewfailback());
		String resStr = "{}";
		// 进行验证预处理
		int gtServerStatus = gtSdk.preProcess(userid);
		// 将服务器状态设置到session中
		request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey,
				gtServerStatus);
		// 将userid设置到session中
		request.getSession().setAttribute("userid", userid);
		resStr = gtSdk.getResponseStr();

		return new ResponseEntity<String>(resStr, Utils.responseHeaders(),
				HttpStatus.OK);
	}

	*//**
	 * 客户端第二次请求，进行第二次验证
	 * 
	 * @param request
	 * @return
	 *//*
	@ResponseBody
	@RequestMapping(value = "/verify/date", method = RequestMethod.POST)
	public ResponseEntity<String> date(HttpServletRequest request) {
		GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(),
				GeetestConfig.getGeetest_key(), GeetestConfig.isnewfailback());
		String challenge = request
				.getParameter(GeetestLib.fn_geetest_challenge);
		String validate = request.getParameter(GeetestLib.fn_geetest_validate);
		String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);
		// 取得手机号
		String mobile = request.getParameter("mobile");
		String userType = request.getParameter("userType");
		String callBack = request.getParameter("jsonpcallback");
		// 从session中获取gt-server状态
		int gt_server_status_code = 2017;
		int gtResult = 2016;

		if (gt_server_status_code == 2017) {
			// gt-server正常，向gt-server进行二次验证
			gtResult = gtSdk.enhencedValidateRequest(challenge, validate,
					seccode, userid);
		} else {
			// gt-server非正常情况下，进行failback模式验证
			System.out.println("failback:use your own server captcha validate");
			gtResult = gtSdk.failbackValidateRequest(challenge, validate,
					seccode);
			System.out.println(gtResult);
		}
		String code = SmsUtils.randomNum();
		boolean isIgnorPhone = false;
		ResultBean<?> resultBean = new ResultBean<Object>();
		// 验证成功
		if (gtResult == 2017) {
			int total1 = verificationCodeService.getCountByPhonePerDay(mobile,
					userType);
			if (total1 <= 5) {
				if (SmsUtils.isContains(SmsUtils.ignorPhones, mobile)) {
					code = SmsUtils.commonCode;
					isIgnorPhone = true;
				}
				int createResult = verificationCodeService.createSMS(mobile,
						code, userType);// 保存记录
				if (createResult > 0
						&& (isIgnorPhone || (!isIgnorPhone && SmsUtils
								.sendCodeMessage(mobile, code)))) {
					resultBean = new ResultBean<Object>(
							ResponseCode1.getSuccess(), "验证码发送成功！");
				} else {
					resultBean = new ResultBean<Object>(
							ResponseCode1.getSms_send_failed(),
							"验证码发送失败，请校验您输入的手机号是否正确！");
				}
			} else {
				resultBean = new ResultBean<Object>(
						ResponseCode1.getSms_times_limit(), "发送验证码过于频繁，请稍后重试！");
			}
		} else {
			resultBean = new ResultBean<Object>(
					ResponseCode1.getSms_checked_failed(), "验证失败！"); // 验证失败
		}
		return Utils.resultResponseJson(resultBean, callBack);
	}
*/
	/**
	 * 获取验证码
	 * 
	 * @param: mobile:手机号 userType:用户类型，driver-司机端，passenger-乘客端
	 *         jsonpCallback：跨域参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sendPhoneCode", method = RequestMethod.POST)
	public ResponseEntity<String> sendPhoneCode(HttpServletRequest request,
			@RequestBody JSONObject jsonRequest) {
		// 取得参数值
		String mobile = jsonRequest.getString("phone");
		String userType = jsonRequest.getString("userType");
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		int count = VerificationUtils.sendPhoneCode(jsonRequest);
		if (count == -1) {
			resultBean = new ResultBean<Object>(ResponseCode.PARAMETER_WRONG.value(),
					ResponseCode.PARAMETER_WRONG.message());
			return Utils.resultResponseJson(resultBean,
					jsonpCallback);
		} else if (count == 0) {
			resultBean = new ResultBean<Object>(ResponseCode.PHONE_FORMAT_ERROR.value(),
					ResponseCode.PHONE_FORMAT_ERROR.message());
			return Utils.resultResponseJson(resultBean,
					jsonpCallback);
		}
		int codeTotal = verificationCodeService.getCountByPhonePerDay(mobile,
				userType);
		if (SmsUtils.isContains(SmsUtils.ignorPhones, mobile) || codeTotal < 5) {
			try {
				verificationCodeService.sendPhoneCode(mobile, userType);
				resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
						ResponseCode.SUCCESS.message());
				return Utils.resultResponseJson(resultBean,
						jsonpCallback);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
				resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						ResponseCode.ERROR.message());
				return Utils.resultResponseJson(resultBean,
						jsonpCallback);
			}

		} else {
			resultBean = new ResultBean<Object>(ResponseCode.SMS_TIMES_LIMIT.value(),
					ResponseCode.SMS_TIMES_LIMIT.message());
			return Utils.resultResponseJson(resultBean,
					jsonpCallback);
		}
	}

	/**
	 * 验证手机验证码
	 * 
	 * @param: mobile:手机号 userType:用户类型，driver-司机端，passenger-乘客端 code:手机验证码
	 *         jsonpCallback：跨域参数
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/checkPhoneCode", method = RequestMethod.POST)
	public ResponseEntity<String> checkPhoneCode(HttpServletRequest request,
			@RequestBody JSONObject jsonRequest) {
		// 取得参数值
		String mobile = jsonRequest.getString("phone");
		String userType = jsonRequest.getString("userType");
		String code = jsonRequest.getString("code");
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		int count = VerificationUtils.checkPhoneCode(jsonRequest);
		if (count == -1) {
			resultBean = new ResultBean<Object>(ResponseCode.PARAMETER_WRONG.value(),
					ResponseCode.PARAMETER_WRONG.message());
			return Utils.resultResponseJson(resultBean,jsonpCallback);
		} else if (count == 0) {
			resultBean = new ResultBean<Object>(ResponseCode.PHONE_FORMAT_ERROR.value(),
					ResponseCode.PHONE_FORMAT_ERROR.message());
			return Utils.resultResponseJson(resultBean,jsonpCallback);
		}
		try {
			if (verificationCodeService.checkPhoneCode(mobile, userType, code)) {
				resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
						ResponseCode.SUCCESS.message());
				return Utils.resultResponseJson(resultBean,jsonpCallback);
			} else {
				resultBean = new ResultBean<Object>(ResponseCode.SMS_CHECKED_FAILED.value(),
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
