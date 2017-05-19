package com.lhcx.model;

public enum OrderType {
	CANCEL(0,"已撤销"), BILL(1,"已发单"),Receiving(2,"已接单"),ABORAD(3,"已发车"),COMPLETE(4,"已完成") ;
	
	private Integer value;
	private String message;
	private OrderType(Integer value, String message) {
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
