package com.lhcx.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lhcx.model.ResultBean;

/**
 * Created by zhu on 2015/12/30.
 */
@Component
public class Utils {
    public static final String jsonpCallback = "lhcxCallBack@2017";
    public static HttpHeaders responseHeaders(){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json;charset=UTF-8");
        return responseHeaders;
    }

    public static String currentTime(){
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        //转换提日期输出格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
    
    public static Timestamp currentTimestamp() {
    	return new Timestamp(System.currentTimeMillis());
	}

    public static boolean isNullOrEmpty(String input) {
        return input == null || input.length() ==0;
    }
    
    public static String randomUUID() {  
        return UUID.randomUUID().toString().replace("-", "");  
    }
    
    /** 
     * 获取当前网络ip 
     * @param request 
     * @return 
     */  
    public static String getIpAddr(HttpServletRequest request){  
        String ipAddress = request.getHeader("x-forwarded-for");  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getRemoteAddr();  
                if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
                    //根据网卡取本机配置的IP  
                    InetAddress inet=null;  
                    try {  
                        inet = InetAddress.getLocalHost();  
                    } catch (UnknownHostException e) {  
                        e.printStackTrace();  
                    }  
                    ipAddress= inet.getHostAddress();  
                }  
            }  
            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
            if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
                if(ipAddress.indexOf(",")>0){  
                    ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
                }  
            }  
            return ipAddress;   
    }

    //接口返回结果处理
    public static ResponseEntity<String> resultResponseJson(ResultBean<?> resultBean, String callBack){
        String resultJsonStr = "";
        if(!isNullOrEmpty(callBack) && callBack.equals(jsonpCallback)){
            resultJsonStr = jsonpCallback + "(" + JSON.toJSONString(resultBean) + ")";
        }else{
            resultJsonStr = JSON.toJSONString(resultBean);
        }
        return new ResponseEntity<String>(resultJsonStr, responseHeaders(), HttpStatus.OK);
    }
}
