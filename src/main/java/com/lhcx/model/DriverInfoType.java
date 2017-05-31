package com.lhcx.model;

public enum DriverInfoType {

	LICENSEID("0","身份证不符合","licenseId"), DRIVERNAME("1","名称不符合","driverName");
	
	private String value;
	private String message;
	private String codeName;
	private DriverInfoType(String value, String message,String codeName) {
		this.value = value;
		this.message = message;
		this.codeName = codeName;
	}
	
	public String value() {
		return value;
	}
	public String message() {
		return message;
	}
	public String codeName(){
		return codeName;
	}
}
