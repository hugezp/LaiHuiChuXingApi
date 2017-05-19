package com.lhcx.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.lhcx.dao.UserMapper;
import com.lhcx.model.PushNotification;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;
import com.lhcx.utils.Utils;
/**
 * @author dp
 * 通过获取车主的token,查出关于车主的列表信息
 * */

@Controller
@RequestMapping(value = "/push")
public class PushController {
	private static Logger log = Logger.getLogger(PushController.class);
	@Autowired
	PushNotificationMapper pushNotification ;
	@Autowired
	UserMapper userDao ;
	@ResponseBody
	@RequestMapping(value = "/List", method = RequestMethod.POST)
	public ResponseEntity<String> PushList(@RequestBody JSONObject jsonRequest,HttpServletRequest request) {	
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;	
		String token = request.getHeader("Token");
	        if(Utils.isNullOrEmpty(token)){
	        	resultBean = new ResultBean<Object>(ResponseCode.getError(),"非法token！");
	    		return Utils.resultResponseJson(resultBean, jsonpCallback);
	        }
	    User user = userDao.selectByToken(token);
	    String phone = user.getUserphone(); 
		try {
			List<PushNotification> pushs = pushNotification.selectAll(phone);
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
