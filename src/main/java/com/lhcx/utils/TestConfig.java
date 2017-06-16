package com.lhcx.utils;

public class TestConfig {
	
	public static boolean testMobile(String mobile){
		boolean flag = false;
		if (mobile.equals("15639356022")||mobile.equals("18538191908")||mobile.equals("18538129108")||mobile.equals("18560459018")||mobile.equals("18838214961")) {
			flag = true;
		}
		return flag;
	}

}
