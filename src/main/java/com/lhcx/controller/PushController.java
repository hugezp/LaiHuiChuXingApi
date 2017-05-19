package com.lhcx.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.dao.PushNotificationMapper;
import com.lhcx.model.PushNotification;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.utils.Utils;
/**
 * @author dp
 * */

@Controller
@RequestMapping(value = "/api/app")
public class PushController {
	private static Logger log = Logger.getLogger(PushController.class);
	@Autowired
	PushNotificationMapper pushNotification ;
	@ResponseBody
	@RequestMapping(value = "/push/List", method = RequestMethod.POST)
	public ResponseEntity<String> PushList(@RequestBody JSONObject jsonRequest) {	
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;	
		int userId = 0;
		try {
			List<PushNotification> pushs = pushNotification.selectAll(userId);
			ArrayList<HashMap<String, Object>> jsonArray = new ArrayList<HashMap<String, Object>>();
			if(pushs.size()>0){
				for(PushNotification push : pushs){
					HashMap<String,Object> receive = new HashMap<String, Object>(); 
					receive.put("pushId", push.getId());
					receive.put("content", push.getAlert());
					receive.put("orderId", push.getOrderId());
					jsonArray.add(receive);
					
				}
			resultBean = new ResultBean<Object>(ResponseCode.getSuccess(),"获取列表成功！",jsonArray);
			return Utils.resultResponseJson(resultBean, jsonpCallback);
			}	
		} catch (Exception e) {	
			log.error("create order error by :" + e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.getError(),
					"发布订单异常！");
		}
		resultBean = new ResultBean<Object>(ResponseCode.getError(),"获取列表失败！");
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	 }
	 
}
