package com.lhcx.utils;

/**
 * 字符串工具类
 * create by YangGuang on 2017/5/23
 */
public class StringUtils {
	
	private StringUtils(){
		
	}
	
	/**
	 * 验证字符串是否为空
	 * @param string
	 * @return true不为空,false为空
	 */
	public static boolean isOrNotEmpty(String string){
		boolean flag = false;
		if(string != null && !string.equals(""))
			flag = true;
		return flag;
	}

}
