package com.lhcx.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.lhcx.model.DriverLocation;
import com.lhcx.model.PushNotification;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.service.IDriverLocationService;
import com.lhcx.service.IOrderService;
import com.lhcx.service.IPushNotificationService;
import com.lhcx.utils.ConfigUtils;
import com.lhcx.utils.DateUtils;
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
@RequestMapping(value = "/order")
public class OrderController {
	private static Logger log = Logger.getLogger(OrderController.class);
	@Autowired
	private IOrderService orderService;

	@Autowired
	private IDriverLocationService driverLocationService;

	@Autowired
	private IPushNotificationService pushNotificationService;
	

	/**
	 * 乘客下单，并推送给附近上线司机
	 * 
	 * @param jsonRequest
	 * @param request
	 * @return
	 */
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
		// 乘客目的地经度
		double destLongitude = ""
				.equals(jsonRequest.getString("DestLongitude")) ? -1 : Double
				.parseDouble(jsonRequest.getString("DestLongitude")) / 1000000;
		// 乘客目的地纬度
		double destLatitude = "".equals(jsonRequest.getString("DestLatitude")) ? -1
				: Double.parseDouble(jsonRequest.getString("DestLatitude")) / 1000000;
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
		
		//订单类型
		Integer orderType = jsonRequest.getInteger("OrderType");
		
		//车辆类型
		Integer carType = jsonRequest.getInteger("CarType");
		
		//路程距离
		double totalDistance = PointToDistance
				.distanceOfTwoPoints(depLatitude, depLongitude,
						destLatitude, destLongitude);
		ResultBean<?> resultBean = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String orderId = orderService.create(jsonRequest);
			if (!orderId.equals("")) {
				result.put("OrderId", orderId);
				// 推送内容
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm");
				DriverLocation d = new DriverLocation();
				d.setLatitude(jsonRequest.getString("DepLatitude"));
				d.setLongitude(jsonRequest.getString("DepLongitude"));
				List<DriverLocation> dLocations = driverLocationService
						.selectList(d);
				if (dLocations.size() > 0) {
					for (DriverLocation driverLocation : dLocations) {
						// 距离
						double distance = driverLocation.getDistance();
						String mobile = driverLocation.getPhone();
						Map<String, String> extrasParam = new HashMap<String, String>();
						extrasParam.put("mobile", passengerPhone);
						extrasParam.put("createTime",
								dateFormat.format(DateUtils.toDateTime(orderTime)));
						extrasParam.put("departure", departure);
						extrasParam.put("destination", destination);
						extrasParam.put("departureTime", dateFormat.format(DateUtils.toDateTime(dePartTime)));
						extrasParam.put("fee", fee);
						extrasParam.put("distance", String.valueOf(distance));
						extrasParam.put("totalDistance",String.valueOf(totalDistance));
						extrasParam.put("orderId", orderId);
						extrasParam.put("orderType", orderType.toString());
						extrasParam.put("carType", carType.toString());
						
						String content = "【来回出行】有用户发布新的行程订单消息，请前往查看抢单!";
						int flag = JpushClientUtil.getInstance(
								ConfigUtils.JPUSH_APP_KEY,
								ConfigUtils.JPUSH_MASTER_SECRET)
								.sendToRegistrationId("11", mobile, content,
										content, content, extrasParam);
						if (flag == 1) {
							PushNotification pushNotification = new PushNotification();
							pushNotification.setPushPhone(passengerPhone);
							pushNotification.setReceivePhone(mobile);
							pushNotification.setOrderId(orderId);
							pushNotification.setAlert(content);
							pushNotification.setData(extrasParam.toString());
							pushNotificationService
									.insertSelective(pushNotification);
						}
					}
				}
				resultBean = new ResultBean<Object>(ResponseCode.getSuccess(),
						"发布订单成功！", result);
			} else {
				resultBean = new ResultBean<Object>(ResponseCode.getError(),
						"发布订单失败！");
			}

		} catch (Exception e) {
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
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("Fee", "123.50");
			resultBean = new ResultBean<Object>(ResponseCode.getSuccess(),
					"获取费用成功！", result);

		} catch (Exception e) {
			log.error("create order error by :" + e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.getError(),
					"获取费用异常！");
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

	/**
	 * 撤销订单
	 */
	@ResponseBody
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public ResponseEntity<String> cancel(@RequestBody JSONObject jsonRequest) {
		// 获取参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		
		ResultBean<?> resultBean = null;
		int flag = orderService.cancel(jsonRequest);
		if (flag > 0) {
			resultBean = new ResultBean<Object>(ResponseCode.getSuccess(),
					"撤销订单成功！");
		} else {
			resultBean = new ResultBean<Object>(ResponseCode.getError(),
					"撤销订单异常！");
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

	/**
	 * 司机抢单
	 * 
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/match", method = RequestMethod.POST)
	public ResponseEntity<String> match(@RequestBody JSONObject jsonRequest) {
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		try {
			resultBean = orderService.match(jsonRequest);
		} catch (Exception e) {
			log.error("order match error by :" + e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.getError(),
					"接单失败 ！服务器繁忙，请重试！");
		}

		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
	
	/**
	 * 司机接到乘客后发车
	 * 
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/depart", method = RequestMethod.POST)
	public ResponseEntity<String> depart(@RequestBody JSONObject jsonRequest) {
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		String orderId = jsonRequest.getString("OrderId");
		ResultBean<?> resultBean = null;
		try {
			
		} catch (Exception e) {
			log.error("order match error by :" + e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.getError(),
					"接单失败 ！服务器繁忙，请重试！");
		}

		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

	/**
	 * 订单完成
	 * 
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/arrive", method = RequestMethod.POST)
	public ResponseEntity<String> arrive(@RequestBody JSONObject jsonRequest) {
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		try {
			//TODO
		} catch (Exception e) {
			log.error("order match error by :" + e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.getError(),
					"接单失败 ！服务器繁忙，请重试！");
		}

		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
}
