package com.lhcx.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

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
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.service.IDriverLocationService;
import com.lhcx.service.IOrderService;
import com.lhcx.utils.ConfigUtils;
import com.lhcx.utils.JpushClientUtil;
import com.lhcx.utils.PointToDistance;
import com.lhcx.utils.Utils;

/**
 * 订单
 * 
 * @author william
 *
 */

@Controller
@RequestMapping(value = "/api/app/order")
public class OrderController {
	private static Logger log = Logger.getLogger(OrderController.class);
	@Autowired
	private IOrderService orderService;

	@Autowired
	private IDriverLocationService driverLocationService;

	@ResponseBody
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<String> create(@RequestBody JSONObject jsonRequest,
			HttpServletRequest request) {
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		// 乘客出发地经度
		double depLongitude = "".equals(jsonRequest.getString("DepLongitude")) ? -1
				: Double.parseDouble(jsonRequest.getString("DepLongitude")) / 1000000;
		// 乘客出发地纬度
		double depLatitude = "".equals(jsonRequest.getString("DepLatitude")) ? -1
				: Double.parseDouble(jsonRequest.getString("DepLatitude")) / 1000000;
		// 乘客手机号
		String passengerPhone = jsonRequest.getString("PassengerPhone");
		// 乘客发单时间
		long orderTime = Long.parseLong(jsonRequest.getString("OrderTime"));
		// 乘客出发时间
		long dePartTime = Long.parseLong(jsonRequest.getString("DePartTime"));
		// 乘客出发地
		String departure = jsonRequest.getString("Departure");
		// 乘客目的地
		String destination = jsonRequest.getString("Destination");
		// 乘客费用
		String fee = jsonRequest.getString("Fee");
		ResultBean<?> resultBean = null;
		try {
			String orderId = orderService.create(jsonRequest);
			if (!orderId.equals("")) {
				// 推送内容
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String content = "手机号码为" + passengerPhone + "的用户，在"
						+ dateFormat.format(Utils.toDateTime(orderTime)) + "发布了从" + departure + "到"
						+ destination + "的行程，出发时间为 + "
						+ dateFormat.format(Utils.toDateTime(dePartTime)) + "费用为" + fee + "元";
				List<DriverLocation> dLocations = driverLocationService
						.selectList(new DriverLocation());
				if (dLocations.size() > 0) {
					for (DriverLocation driverLocation : dLocations) {
						// 车主经度
						double longitude = "".equals(driverLocation
								.getLongitude()) ? -1 : Double
								.parseDouble(driverLocation.getLongitude())/1000000;
						// 车主纬度
						double latitude = "".equals(driverLocation
								.getLatitude()) ? -1 : Double
								.parseDouble(driverLocation.getLatitude())/1000000;
						if (depLatitude != -1 && depLongitude != -1
								&& longitude != -1 && latitude != -1) {
							double distance = PointToDistance
									.distanceOfTwoPoints(depLatitude,
											depLongitude, latitude, longitude);
							if (distance < ConfigUtils.PUSH_DISTANCE) {
								String mobile = driverLocation.getPhone();
								JpushClientUtil.getInstance()
										.sendToRegistrationId("11", mobile, content,
												content, content, content);
							}
						}
					}
				}
				resultBean = new ResultBean<Object>(ResponseCode.getSuccess(),
						"发布订单成功！");
			} else {
				resultBean = new ResultBean<Object>(ResponseCode.getError(),
						"发布订单失败！");
			}

		} catch (Exception e) {
			// TODO: handle exception
			log.error("create order error by :" + e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.getError(),
					"发布订单异常！");
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
	
	@ResponseBody
    @RequestMapping(value = "/getFee", method = RequestMethod.POST)
    public ResponseEntity<String> getFee(@RequestBody JSONObject jsonRequest) {
		//取得参数值
        String jsonpCallback = jsonRequest.getString("jsonpCallback");
        ResultBean<?> resultBean = null; 
        Map<String,Object> result = new HashMap<String, Object>();
        try {  
        	result.put("Fee", "123.50");
        	resultBean = new ResultBean<Object>(ResponseCode.getSuccess(),"获取费用成功！",result);
			
		} catch (Exception e) {
			// TODO: handle exception
			log.error("create order error by :" + e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.getError(),"获取费用异常！");
		}
        return Utils.resultResponseJson(resultBean,jsonpCallback);
	}
	
	
}
