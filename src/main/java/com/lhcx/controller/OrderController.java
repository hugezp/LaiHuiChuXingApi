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
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.service.IOrderService;
import com.lhcx.utils.Utils;

/**
 * 订单
 * @author william
 *
 */

@Controller
@RequestMapping(value = "/api/order")
public class OrderController {
	private static Logger log = Logger.getLogger(OrderController.class);
	@Autowired
	private IOrderService orderService;

	@ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<String> create(@RequestBody JSONObject jsonRequest) {
		//取得参数值
        String jsonpCallback = jsonRequest.getString("jsonpCallback");
        ResultBean<?> resultBean = null; 
        try {  
        	if (orderService.create(jsonRequest)) {
        		resultBean = new ResultBean<Object>(ResponseCode.getSuccess(),"发布订单成功！");
			} else {
				resultBean = new ResultBean<Object>(ResponseCode.getError(),"发布订单失败！");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("create order error by :" + e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.getError(),"发布订单异常！");
		}
        return Utils.resultResponseJson(resultBean,jsonpCallback);
	}
}
