package com.lhcx.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;

/**
 * 计算价格工具类
 * Created by YangGuang on 2017/5/31.
 */
public class PriceUtil {

    /**
     * 获取车主价格
     */
    public static double getOwnerPrice(String origin_location, String destination_location) {
        double price = 0.0;
        String result = "";
        URL file_url = null;
        try {
            String json_url = "http://restapi.amap.com/v3/distance?key=5f128c6b72fb65b81348ca1477f3c3ce&origins=" + origin_location + "&destination=" + destination_location + "&type=1";
            file_url = new URL(json_url);
            InputStream content = (InputStream) file_url.getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(content, "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result = result + line;
            }
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
        JSONObject dataObject = JSONObject.parseObject(result);
        JSONArray dataArray = dataObject.getJSONArray("results");
        if (dataArray.size() > 0) {
            JSONObject nowObject = dataArray.getJSONObject(0);
            int distance = nowObject.getIntValue("distance");
            if (distance <= 200000)
                price = distance * 3.5 / 10000f;
            else
                price = distance * 3.3 / 10000f;
        }
        return new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取乘客价格
     */
    public static double getPessengerPrice(String origin_location, String destination_location, int person) {
        String result = "";
        URL file_url = null;
        try {
            String json_url = "http://restapi.amap.com/v3/distance?key=5f128c6b72fb65b81348ca1477f3c3ce&origins=" + origin_location + "&destination=" + destination_location + "&type=1";
            file_url = new URL(json_url);
            InputStream content = (InputStream) file_url.getContent();
            BufferedReader in = new BufferedReader(new InputStreamReader(content, "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result = result + line;
            }
        } catch (Exception e) {
            //System.out.println(e.getMessage());
        }
        JSONObject dataObject = JSONObject.parseObject(result);
        JSONArray dataArray = dataObject.getJSONArray("results");
        if (dataArray.size() > 0) {
            JSONObject nowObject = dataArray.getJSONObject(0);
            int distance = nowObject.getIntValue("distance");
            double start_price = 0;
            double price = distance * 3.3 / 10000f;
            if (distance <= 200000) {
                start_price = 10.0;
            }
            double last_price = start_price + price * person;
            return new BigDecimal(last_price).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }else {
            return 0.0;
        }
    }
}
