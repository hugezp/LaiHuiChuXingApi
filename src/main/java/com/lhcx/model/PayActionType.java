package com.lhcx.model;

public enum PayActionType {
	spending(0,"转出 "),income(1,"转入"),refund(2,"退款"),withdrawal(3,"提现");
	
	private Integer value;
	private String message;
	private PayActionType(Integer value, String message) {
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
