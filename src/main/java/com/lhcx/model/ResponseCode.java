package com.lhcx.model;

public enum ResponseCode {
	
	SUCCESS(2000,"请求成功"), 
	FILE_SUCCESS(2001,"文件上传成功"), 
	ERROR(3000,"请求错误"),
	FILE_FAILED(3001,"文件上传失败"),
	TOKEN_EXPIRED(3002,"无效的token"),
	PHONE_FORMAT_ERROR(3003,"手机号格式错误"),
	SMS_SEND_FAILED(3004,"验证码发送失败"), 
	SMS_TIMES_LIMIT(3005,"发送验证码过于频繁"),
	SMS_CHECKED_FAILED(3006,"验证码校验失败"),
	RELEASE_ORDER_FAILED(3007,"发布订单失败"),
	CANCEL_ORDER_FAILED(3008,"取消订单失败"),
	DEPART_ORDER_FAILED(3009,"开始行程失败"),
	ARRIVE_ORDER_FAILED(3010,"订单完成失败"),
	PUSH_BUTTON_FAILED(3011,"设置失败"),
	PUSH_UPDATE_FAILED(3012,"更新失败"),
	LOGIN_FAILED(3013,"未登录或登录已失效"),
	REGISTER_FAILED(3014,"注册失败"),
	RELEASE_ORDER_REPEAT(3015,"您当前有未处理的订单,不能发单"),
	DEPART_ORDER_REPEAT(3016,"您已接单,不能抢单！"),
	DRIVER_INVALID(3017,"司机审核未通过，不能听单上线！"),
	PARAMETER_WRONG(4000,"参数不完整"),
	NO_DATA(4004,"暂无数据"),
	NO_USER(4005,"用户不存在");
	
	private Integer value;
	private String message;
	private ResponseCode(Integer value, String message) {
		this.value = value;
		this.message = message;
	}
	
	public Integer value() {
		return value;
	}
	public String message() {
		return message;
	}

}
