package com.lhcx.model;

public enum ResponseCode {
	
	SUCCESS(2000,"请求成功"), 
	ERROR(3000,"请求失败"),
	TOKEN_EXPIRED(3001,"无效的token"),
	PHONE_FORMAT_ERROR(3002,"手机号格式错误"),
	SMS_SEND_FAILED(3005,"验证码发送失败"), 
	SMS_TIMES_LIMIT(3006,"发送验证码过于频繁"),
	SMS_CHECKED_FAILED(3007,"验证码校验失败"),
	PARAMETER_WRONG(4000,"参数不完整"),
	NO_DATA(4004,"暂无数据");
	
	
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
