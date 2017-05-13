package com.lhcx.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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
