package com.lhcx.model;

public enum OrderStatus {
	FAILURE(-1,"已失效"),CANCEL(0,"已撤销"), BILL(1,"已发单"),Receiving(2,"已接单"),REACHED(3,"已到达乘客乘客位置"), ABORAD(4,"已发车"),ARRIVE(5,"已完成");
	
	private Integer value;
	private String message;
	private OrderStatus(Integer value, String message) {
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
