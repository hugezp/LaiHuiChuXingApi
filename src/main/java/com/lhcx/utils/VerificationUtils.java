package com.lhcx.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * 验证工具类
 * 
 * @author YangGuang
 * @since 1.0 create on 2017/5/23
 */
public class VerificationUtils {

	/**
	 * 发送验证码参数
	 * 
	 * @return -1表示参数不完整 0表示手机号格式错误 1通过验证
	 */
	public static int sendPhoneCode(JSONObject jsonRequest) {
		int count = -1;
		String mobile = jsonRequest.getString("phone");
		String userType = jsonRequest.getString("userType");
		if (StringUtils.isOrNotEmpty(mobile)
				&& StringUtils.isOrNotEmpty(userType)) {
			if (!Regex.isPhoneLegal(mobile))
				count = 0;
			count = 1;
		}
		return count;
	}

	/**
	 * 验证验证码参数
	 * 
	 * @return -1表示参数不完整 0表示手机号格式错误 1通过验证
	 */
	public static int checkPhoneCode(JSONObject jsonRequest) {
		int count = -1;
		String mobile = jsonRequest.getString("phone");
		String userType = jsonRequest.getString("userType");
		String code = jsonRequest.getString("code");
		if (StringUtils.isOrNotEmpty(mobile)
				&& StringUtils.isOrNotEmpty(userType)
				&& StringUtils.isOrNotEmpty(code)) {
			if (!Regex.isPhoneLegal(mobile))
				count = 0;
			count = 1;
		}
		return count;
	}

}
