package com.lhcx.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.ResultBean;
import com.lhcx.service.IOrderService;
import com.lhcx.utils.Utils;

/**
 * 司机
 * @author william
 *
 */

@Controller
@RequestMapping(value = "/api/app")
public class DriverController {
	private static Logger log = Logger.getLogger(DriverController.class);
	@Autowired
	private IOrderService orderService;

	@ResponseBody
	@RequestMapping(value = "/order/match", method = RequestMethod.POST)
	public ResponseEntity<String> match(@RequestBody JSONObject jsonRequest){
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

}
