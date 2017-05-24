package com.lhcx.utils;

/**
 * 配置信息工具类
 * @author YG
 *
 */
public class ConfigUtils {
	
	//极光推送离线时长 单位:秒
	public static final long TIME_TO_LIVE = 1800;
	
	//极光AppKey 
	public static final String JPUSH_APP_KEY = "a1835850adc6bff63a9bf7c6";
	
	//极光秘钥  
	public static final String JPUSH_MASTER_SECRET = "68f13ab0dfd2e888d11522a0";
	
	//乘客端极光AppKey
	public static final String PASSENGER_JPUSH_APP_KEY = "8eeea6a55cf60bfba0ec3648";
	
	//乘客端极光秘钥
	public static final String PASSENGER_JPUSH_MASTER_SECRET = "1bd64257812a589e9c6b461a";
	
	
	//订单失效时间
	public static final long ORDER_TO_LIVE = 30 * 60 * 1000;

}
