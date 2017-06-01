package com.lhcx.model;

public enum DriverInfoType {

	PHOTO("0","头像不符合","photo"),DRIVERNAME("1","名称不符合","driverName"),LICENSEID("2","身份证不符合","licenseId"),
	DRIVERNATIONALITY("3","国际不符合","driverNationality"),DRIVERNATION("4","民族不符合","driverNation"),
	ADDRESSNAME("5","通讯地址不符合","addressName"),LICENSEPHOTO("6","驾驶证照片不符合","licensePhoto"),
	GETDRIVERLICENSEDATE("7","领取驾驶证日期不符合","getDriverLicenseDate"),DRIVERLICENSEON("8","驾驶证有效期起不符合","driverLicenseOn"),
	DRIVERLICENSEOFF("9","驾驶证有效期止不符合","driverLicenseOff"),FULLTIMEDRIVER("10","是否是职业驾驶员不符合","fullTimeDriver");
	
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
