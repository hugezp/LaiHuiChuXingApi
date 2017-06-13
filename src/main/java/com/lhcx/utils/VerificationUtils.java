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
	public static int sendPhoneCodeValidation(JSONObject jsonRequest) {
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
	public static int checkPhoneCodeValidation(JSONObject jsonRequest) {
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

	/**
	 * 创建订单
	 * 
	 * @return
	 */
	public static boolean createOrderValidation(JSONObject jsonRequest) {
		boolean flag = false;
		// 乘客出发地
		String departure = jsonRequest.getString("Departure");
		String destination = jsonRequest.getString("Destination");
		String depLongitude = jsonRequest.getString("DepLongitude");
		String depLatitude = jsonRequest.getString("DepLatitude");
		String destLongitude = jsonRequest.getString("DestLongitude");
		String destLatitude = jsonRequest.getString("DestLatitude");
		String passengerPhone = jsonRequest.getString("PassengerPhone");
		String orderTime = jsonRequest.getString("OrderTime");
		String dePartTime = jsonRequest.getString("DePartTime");
		String fee = jsonRequest.getString("Fee");
		Integer orderType = jsonRequest.getInteger("OrderType");
		Integer carType = jsonRequest.getInteger("CarType");
		if (StringUtils.isOrNotEmpty(departure)
				&& StringUtils.isOrNotEmpty(destination)
				&& StringUtils.isOrNotEmpty(depLongitude)
				&& StringUtils.isOrNotEmpty(depLatitude)
				&& StringUtils.isOrNotEmpty(destLongitude)
				&& StringUtils.isOrNotEmpty(destLatitude)
				&& StringUtils.isOrNotEmpty(passengerPhone)
				&& StringUtils.isOrNotEmpty(orderTime)
				&& StringUtils.isOrNotEmpty(dePartTime)
				&& StringUtils.isOrNotEmpty(fee)
				&& StringUtils.isOrNotEmpty(orderType.toString())
				&& StringUtils.isOrNotEmpty(carType.toString())) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 获取价格
	 * 
	 * @return
	 */
	public static boolean getFee(JSONObject jsonRequest) {
		boolean flag = false;
		// 乘客出发地
		String depLongitude = jsonRequest.getString("DepLongitude");
		String depLatitude = jsonRequest.getString("DepLatitude");
		String destLongitude = jsonRequest.getString("DestpLongitude");
		String destLatitude = jsonRequest.getString("DestpLatitude");
		if (StringUtils.isOrNotEmpty(depLongitude)
				&& StringUtils.isOrNotEmpty(depLatitude)
				&& StringUtils.isOrNotEmpty(destLongitude)
				&& StringUtils.isOrNotEmpty(destLatitude)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 撤销订单
	 * 
	 * @param jsonRequest
	 * @return
	 */
	public static boolean cancelOrderValidation(JSONObject jsonRequest) {
		boolean flag = false;
		String operator = jsonRequest.getString("Operator");
		String cancelTypeCode = jsonRequest.getString("CancelTypeCode");
		String cancelReason = jsonRequest.getString("CancelReason");
		if (StringUtils.isOrNotEmpty(operator)
				&& StringUtils.isOrNotEmpty(cancelTypeCode)
				&& StringUtils.isOrNotEmpty(cancelReason)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 司机接到乘客后发车
	 * 
	 * @param jsonRequest
	 * @return
	 */
	public static boolean departOrderValidation(JSONObject jsonRequest) {
		boolean flag = false;
		String orderId = jsonRequest.getString("OrderId");
		if (StringUtils.isOrNotEmpty(orderId)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 推送开关
	 * 
	 * @param jsonRequest
	 * @return
	 */
	public static boolean pushButtonValidation(JSONObject jsonRequest) {
		boolean flag = false;
		String longitude = jsonRequest.getString("Longitude");
		String latitude = jsonRequest.getString("Latitude");
		String isDel = jsonRequest.getString("isDel");
		if (StringUtils.isOrNotEmpty(longitude)
				&& StringUtils.isOrNotEmpty(latitude)
				&& StringUtils.isOrNotEmpty(isDel)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 更新经纬度
	 * 
	 * @param jsonRequest
	 * @return
	 */
	public static boolean updateValidation(JSONObject jsonRequest) {
		boolean flag = false;
		String longitude = jsonRequest.getString("Longitude");
		String latitude = jsonRequest.getString("Latitude");
		if (StringUtils.isOrNotEmpty(longitude)
				&& StringUtils.isOrNotEmpty(latitude)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 登录
	 * 
	 * @param jsonRequest
	 * @return
	 */
	public static boolean loginValidation(JSONObject jsonRequest) {
		boolean flag = false;
		String phone = jsonRequest.getString("phone");
		String userType = jsonRequest.getString("userType");
		String code = jsonRequest.getString("code");
		if (StringUtils.isOrNotEmpty(phone)
				&& StringUtils.isOrNotEmpty(userType)
				&& StringUtils.isOrNotEmpty(code)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 支付
	 * 
	 * @param jsonRequest
	 * @return
	 */
	public static boolean payValidation(JSONObject jsonRequest) {
		boolean flag = false;
		String orderId = jsonRequest.getString("orderId");
		Integer payType = jsonRequest.getInteger("payType");
		if (StringUtils.isOrNotEmpty(orderId) && payType != null
				&& payType >= 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 全部消息
	 * 
	 * @param jsonRequest
	 * @return
	 */
	public static boolean pushList(JSONObject jsonRequest) {
		boolean flag = false;
		String pushType = jsonRequest.getString("pushType");
		String page = jsonRequest.getString("page");
		String size = jsonRequest.getString("size");
		if (StringUtils.isOrNotEmpty(pushType)
				&& StringUtils.isOrNotEmpty(page)
				&& StringUtils.isOrNotEmpty(size)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 听单设置
	 * 
	 * @param jsonRequest
	 * @return
	 */
	public static boolean setPush(JSONObject jsonRequest) {
		boolean flag = false;
		String preference = jsonRequest.getString("preference");
		String scope = jsonRequest.getString("scope");
		if (StringUtils.isOrNotEmpty(preference)
				&& StringUtils.isOrNotEmpty(scope)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 投诉与建议
	 * 
	 * @param jsonRequest
	 * @return
	 */
	public static boolean suggest(JSONObject jsonRequest) {
		boolean flag = false;
		String contactinformation = jsonRequest.getString("contactinformation");
		String suggest = jsonRequest.getString("suggest");
		String source = jsonRequest.getString("source");
		if (StringUtils.isOrNotEmpty(contactinformation)
				&& StringUtils.isOrNotEmpty(suggest)
				&& StringUtils.isOrNotEmpty(source)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 钱包参数验证
	 * 
	 * @param jsonRequest
	 * @return
	 */
	public static boolean walletValidation(JSONObject jsonRequest) {
		boolean flag = false;
		String time = jsonRequest.getString("time");
		String startPro = jsonRequest.getString("page");
		String pageSize = jsonRequest.getString("size");
		if (StringUtils.isOrNotEmpty(time)
				&& StringUtils.isOrNotEmpty(startPro)
				&& StringUtils.isOrNotEmpty(pageSize)) {
			flag = true;
		}
		return flag;
	}

	public static boolean ithdrawalsValidation(JSONObject jsonRequest) {
		boolean flag = false;
		String actiontype = jsonRequest.getString("actiontype");
		String startPro = jsonRequest.getString("page");
		String pageSize = jsonRequest.getString("size");
		if (StringUtils.isOrNotEmpty(actiontype)
				&& StringUtils.isOrNotEmpty(startPro)
				&& StringUtils.isOrNotEmpty(pageSize)) {
			flag = true;
		}
		return flag;
	}
	
	public static boolean withdrawalsValidation(JSONObject jsonRequest) {
		boolean flag = false;
		String cash = jsonRequest.getString("cash");
		String payAccount = jsonRequest.getString("payAccount");
		String payType = jsonRequest.getString("payType");
		if (StringUtils.isOrNotEmpty(cash)
				&& StringUtils.isOrNotEmpty(payAccount)
				&& StringUtils.isOrNotEmpty(payType)) {
			flag = true;
		}
		return flag;
	}
}
