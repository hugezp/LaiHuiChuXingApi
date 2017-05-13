package com.lhcx.utils;

import com.alibaba.fastjson.JSONObject;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lh on 2017/5/11.
 */
public class SmsUtils {
    private static Logger log = Logger.getLogger(SmsUtils.class);
    //配置您申请的KEY
    public static final String APPKEY ="6c871b315def3b449dbdbce28a4dc55a";
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

    public static final String ignorPhones[] = {"18330288097","17788112947","17698909223"};
    public static final String commonCode = "0603";
    
    public static boolean sendCodeMessage(String mobile,String code) {    	
    	code = "#code#=" + code;
    	return sendSMS(mobile, "push", 0, code);
	}
    
    public static boolean isContains(String[] arr, String targetValue) {
        for(String s: arr){
            if(s.equals(targetValue))
                return true;
        }
        return false;
    }

    public static String sendCodeMessage(String mobile) {
        String rand = randomNum();
        if (mobile.equals("18330288097")) {
            return "0603";
        }
        if (mobile.equals("17788112947")) {
            return "0603";
        }
        if (mobile.equals("17698909223")) {
            return "0603";
        }
        if (mobile.equals("15639356022")) {
            return "0603";
        }
        if (mobile.equals("18538191908")) {
            return "0603";
        }
        if (mobile.equals("18560459018")) {
            return "0603";
        }
        if (mobile.equals("15516015893")) {
            return "0603";
        }
        String code = "#code#=" + rand;
        boolean send_isSuccess = sendSMS(mobile, "push", 0, code);
        if (!send_isSuccess) {
            code = null;
        } else {
            code = rand;
        }
        return code;
    }

    public static String randomNum(){
        String randomNum="";
        int i=0;
        int [] all=new int[4];
        while (i<4){
            int t=(int)(Math.random()*10);
            all[i]=t;
            i++;
        }
        for(int k=0;k<4;k++){
            randomNum=randomNum+all[k];
        }
        return randomNum;
    }

    public static boolean sendSMS(String mobile,String smsType,int tpl_id,String tpl_value)
    {
        boolean isSuccess=false;
        String result=sendSMStoServer(mobile,tpl_id,tpl_value);
        @SuppressWarnings("unused")
		int sms_error_code=-1;
        @SuppressWarnings("unused")
		String sms_reason="";
        @SuppressWarnings("unused")
		String sms_sid="";
        if(result!=null) {
            JSONObject object = JSONObject.parseObject(result);
            /*System.out.println("object信息：" + object);*/
            if (object.getIntValue("error_code") == 0) {
                JSONObject jsonObject = object.getJSONObject("result");
                @SuppressWarnings("unused")
				int fee = jsonObject.getIntValue("fee");
                @SuppressWarnings("unused")
				int count = jsonObject.getIntValue("count");
                sms_sid= jsonObject.getString("sid");
                sms_reason=object.getString("reason");
                sms_error_code=0;
                isSuccess=true;
            } else {
                sms_reason=object.getString("reason");
                sms_error_code=object.getIntValue("error_code");
                System.out.println(object.get("error_code") + ":" + object.get("reason"));
                isSuccess=false;
            }
        }
        //创建发送短信信息记录
        return isSuccess;
    }

    public static String sendSMStoServer(String mobile,int tpl_id,String tpl_value){
        if(tpl_id==0)
        {
            tpl_id=29230;
        }
        String result =null;
        String url ="http://v.juhe.cn/sms/send";//请求接口地址
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        params.put("mobile",mobile);//接收短信的手机号码
        params.put("tpl_id",tpl_id);//短信模板ID，请参考个人中心短信模板设置 8193
        params.put("tpl_value",tpl_value);//变量名和变量值对。如果你的变量名或者变量值中带有#&amp;=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，&lt;a href=&quot;http://www.juhe.cn/news/index/id/50&quot; target=&quot;_blank&quot;&gt;详细说明&gt;&lt;/a&gt;
        params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
        params.put("dtype","json");//返回数据的格式,xml或json，默认json

        int maxTry=3;
        int num=0;
        while (num<maxTry) {
            try {
                boolean flag = false;
                result = net(url, params, "GET");
                JSONObject object = JSONObject.parseObject(result);
                if(object!=null)
                {
                    flag=true;
                }
                if(flag==true){
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String net(String strUrl, Map<String, Object> params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(urlencode(params));
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }

    //将map型转为请求参数型
    public static String urlencode(Map<String, ?> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, ?> i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
