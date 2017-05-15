package com.lhcx.model;

public enum UserType {

	DRIVER("driver","司机端"), PASSENGER("passenger","乘客端");
	
	private String value;
	private String message;
	private UserType(String value, String message) {
		this.value = value;
		this.message = message;
	}
	
	public String value() {
		return value;
	}
	public String message() {
		return message;
	}
}
