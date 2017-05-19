package com.lhcx.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.DriverLocation;
import com.lhcx.model.Order;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;
import com.lhcx.service.IDriverLocationService;
import com.lhcx.service.IOrderService;
import com.lhcx.utils.Utils;

/**
 * 司机
 * @author william
 *
 */

@Controller
@RequestMapping(value = "/order")
public class DriverController {
	private static Logger log = Logger.getLogger(DriverController.class);
	@Autowired
	private IOrderService orderService;
	@Autowired
	private IDriverLocationService driverLocationService;
	@Autowired  
    private HttpSession session;  

	@ResponseBody
	@RequestMapping(value = "/match", method = RequestMethod.POST)
	public ResponseEntity<String> match(@RequestBody JSONObject jsonRequest){
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		try {
			User user = (User)session.getAttribute("CURRENT_USER");
			String phone = user.getUserphone();
			DriverLocation driverLocation = driverLocationService.selectByPhone(phone);
			Order order = orderService.selectByOrderId(jsonRequest.getString("OrderId"));
			if (driverLocation != null && driverLocation.getIsdel() == 1) {
				if (order == null || order.getStatus() != 1) {
					resultBean = new ResultBean<Object>(ResponseCode.getError(),
							"该订单已失效或已被接单，如有疑问请联系售后！");
				}else {
					Map<String,Object> result = orderService.match(jsonRequest,phone);
					resultBean = new ResultBean<Object>(ResponseCode.getSuccess(),
							"接单成功！",result);
				}
			} else {
				resultBean = new ResultBean<Object>(ResponseCode.getError(),
						"您没有经营上线，请经营上线后接单！");
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("order match error by :" + e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.getError(),
					"接单异常！");
		}
		
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

}
