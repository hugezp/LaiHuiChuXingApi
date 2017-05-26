package com.lhcx.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.lhcx.model.DriverInfo;
import com.lhcx.model.DriverLocation;
import com.lhcx.model.Order;
import com.lhcx.model.OrderDetail;
import com.lhcx.model.OrderStatus;
import com.lhcx.model.PassengerInfo;
import com.lhcx.model.PushNotification;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;
import com.lhcx.model.response.OrderResponse;
import com.lhcx.service.IDriverInfoService;
import com.lhcx.service.IDriverLocationService;
import com.lhcx.service.IOrderLogService;
import com.lhcx.service.IOrderService;
import com.lhcx.service.IPassengerInfoService;
import com.lhcx.service.IPushNotificationService;
import com.lhcx.utils.ConfigUtils;
import com.lhcx.utils.DateUtils;
import com.lhcx.utils.JpushClientUtil;
import com.lhcx.utils.PointToDistance;
import com.lhcx.utils.StringUtils;
import com.lhcx.utils.Utils;
import com.lhcx.utils.VerificationUtils;

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

	@Autowired
	private IOrderLogService orderLogService;
	
	@Autowired
	private IDriverInfoService driverInfoService;
	
	@Autowired
	private IPassengerInfoService passengerInfoService;
	
	@Autowired
	private HttpSession session;

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
		ResultBean<?> resultBean = null;
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		if (!VerificationUtils.createOrderValidation(jsonRequest)) {
			resultBean = new ResultBean<Object>(
					ResponseCode.PARAMETER_WRONG.value(),
					ResponseCode.PARAMETER_WRONG.message());
		} else {

			// 乘客手机号
			String passengerPhone = jsonRequest.getString("PassengerPhone");
			Map<String, Object> result = new HashMap<String, Object>();
			try {
				Order newOrder = orderService
						.selectNewOrderByPhone(passengerPhone);
				if (newOrder == null) {
					String orderId = orderService.create(jsonRequest);
					if (!orderId.equals("")) {
						result.put("OrderId", orderId);
						// 推送内容
						resultBean = new ResultBean<Object>(
								ResponseCode.SUCCESS.value(),
								ResponseCode.SUCCESS.message(), result);
					} else {
						resultBean = new ResultBean<Object>(
								ResponseCode.RELEASE_ORDER_FAILED.value(),
								ResponseCode.RELEASE_ORDER_FAILED.message());
					}
				} else {
					resultBean = new ResultBean<Object>(
							ResponseCode.RELEASE_ORDER_REPEAT.value(),
							"您当前有未处理的订单,不能发单");
				}
			} catch (Exception e) {
				log.error("create order error by :" + e.getMessage());
				e.printStackTrace();
				resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						ResponseCode.ERROR.message());
			}
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
			resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
					ResponseCode.SUCCESS.message(), result);

		} catch (Exception e) {
			log.error("create order error by :" + e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					ResponseCode.ERROR.message());
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
	
	
	/**
	 * 创建成功后推送
	 * 
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/create/push", method = RequestMethod.POST)
	public void push(@RequestBody JSONObject jsonRequest) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm");
			DriverLocation d = new DriverLocation();
			String orderId = jsonRequest.getString("OrderId");
			
			Order order = orderService.selectByOrderId(orderId);
			
			if (order.getStatus() != OrderStatus.BILL.value()) {
				return;
			}
			
			// 乘客出发地经度
			double depLongitude = Double.parseDouble(order.getDeplongitude()) / 1000000;
			// 乘客出发地纬度
			double depLatitude = Double.parseDouble(order.getDeplatitude()) / 1000000;
			// 乘客目的地经度
			double destLongitude = Double.parseDouble(order.getDestlongitude()) / 1000000;
			// 乘客目的地纬度
			double destLatitude =Double.parseDouble(order.getDestlatitude()) / 1000000;
			// 乘客手机号
			String passengerPhone = order.getPassengerphone();
			// 乘客发单时间
			Date orderTime = order.getOrdertime();
			// 乘客出发时间
			Date dePartTime = order.getDeparttime();
			// 乘客出发地
			String departure = order.getDeparture();
			// 乘客目的地
			String destination = order.getDestination();
			// 乘客费用
			String fee = order.getFee().toString();

			// 订单类型
			Integer orderType = order.getOrderType();

			// 车辆类型
			Integer carType = order.getCarType();
			
			// 路程距离
			double totalDistance = PointToDistance.distanceOfTwoPoints(
					depLatitude, depLongitude, destLatitude, destLongitude);

			d.setLatitude(order.getDeplatitude());
			d.setLongitude(order.getDeplongitude());
			List<DriverLocation> dLocations = driverLocationService
					.selectList(d);
			if (dLocations.size() > 0) {
				for (DriverLocation driverLocation : dLocations) {
					double distance = driverLocation.getDistance();
					String mobile = driverLocation.getPhone();
					Map<String, String> extrasParam = new HashMap<String, String>();
					extrasParam.put("mobile", passengerPhone);
					extrasParam.put("createTime",
							dateFormat.format(orderTime));
					extrasParam.put("departure", departure);
					extrasParam.put("destination", destination);
					extrasParam.put("departureTime", dateFormat
							.format(dePartTime));
					extrasParam.put("fee", fee);
					extrasParam.put("distance",
							String.valueOf(distance));
					extrasParam.put("totalDistance",
							String.valueOf(totalDistance));
					extrasParam.put("orderId", orderId);
					extrasParam.put("orderType",
							orderType.toString());
					extrasParam.put("carType", carType.toString());

					String content = "【来回出行】有用户发布新的行程订单消息，请前往查看抢单!";
					int flag = JpushClientUtil.getInstance(
							ConfigUtils.JPUSH_APP_KEY,
							ConfigUtils.JPUSH_MASTER_SECRET)
							.sendToRegistrationId("11", mobile,
									content, content, content,
									extrasParam);
					if (flag == 1) {
						PushNotification pushNotification = new PushNotification();
						pushNotification
								.setPushPhone(passengerPhone);
						pushNotification.setReceivePhone(mobile);
						pushNotification.setOrderId(orderId);
						pushNotification.setAlert(content);
						pushNotification.setData(extrasParam
								.toString());
						pushNotificationService
								.insertSelective(pushNotification);
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 撤销订单
	 */
	@ResponseBody
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public ResponseEntity<String> cancel(@RequestBody JSONObject jsonRequest) {
		ResultBean<?> resultBean = null;
		// 获取参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		try {
			if (!VerificationUtils.cancelOrderValidation(jsonRequest)) {
				resultBean = new ResultBean<Object>(
						ResponseCode.PARAMETER_WRONG.value(),
						ResponseCode.PARAMETER_WRONG.message());
			} else {
				resultBean = orderService.cancel(jsonRequest);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					ResponseCode.ERROR.message());
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
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					ResponseCode.ERROR.message());
		}

		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

	/**
	 * 司机已到达乘客乘客位置
	 * 
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/reached", method = RequestMethod.POST)
	public ResponseEntity<String> reached(@RequestBody JSONObject jsonRequest) {
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		try {
			resultBean = orderService.reached(jsonRequest);
		} catch (Exception e) {
			log.error("order reached error by :" + e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					ResponseCode.ERROR.message());
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
		ResultBean<?> resultBean = null;
		if (!VerificationUtils.departOrderValidation(jsonRequest)) {
			resultBean = new ResultBean<Object>(
					ResponseCode.PARAMETER_WRONG.value(),
					ResponseCode.PARAMETER_WRONG.message());
		} else {
			try {
				resultBean = orderService.depart(jsonRequest);
			} catch (Exception e) {
				log.error("order depart error by :" + e.getMessage());
				e.printStackTrace();
				resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						ResponseCode.ERROR.message());
			}
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
		if (!VerificationUtils.departOrderValidation(jsonRequest)) {
			resultBean = new ResultBean<Object>(
					ResponseCode.PARAMETER_WRONG.value(),
					ResponseCode.PARAMETER_WRONG.message());
		} else {
			try {
				resultBean = orderService.arrive(jsonRequest);

			} catch (Exception e) {
				log.error("order arrive error by :" + e.getMessage());
				e.printStackTrace();
				resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						ResponseCode.ERROR.message());
			}
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}

	/**
	 * 订单行程
	 * 
	 * @param jsonRequest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/info", method = RequestMethod.POST)
	public ResponseEntity<String> info(@RequestBody JSONObject jsonRequest) {
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		String orderId = jsonRequest.getString("OrderId");
		ResultBean<?> resultBean = null;
		try {
			Order order = orderService.info(orderId);
			OrderResponse result = new OrderResponse(order);

			resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
					"获取订单信息成功！", result);
		} catch (Exception e) {
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"获取订单信息失败！");
		}

		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
	
	/**
	 * 车主订单列表
	 */
	@ResponseBody
	@RequestMapping(value = "/orderDriverList", method = RequestMethod.POST)
	public ResponseEntity<String> orderDriverList(){
		ResultBean<?> resultBean = null;
		try {
			User user = (User)session.getAttribute("CURRENT_USER");
			String driverPhone = user.getUserphone();
			Map<String, Object> result = new HashMap<String, Object>();
			List<OrderDetail> list = new ArrayList<OrderDetail>();
			if (!StringUtils.isOrNotEmpty(driverPhone)) {
				resultBean = new ResultBean<Object>(
						ResponseCode.PARAMETER_WRONG.value(),
						ResponseCode.PARAMETER_WRONG.message(),result);
			}else {
				DriverInfo driverInfo = driverInfoService.selectByPhone(driverPhone);
				if (driverInfo != null) {
					result.put("money","86.86");
					result.put("userName",driverInfo.getDrivername());
					result.put("userPhoto",driverInfo.getPhoto());
					List<Order> orderList = orderService.selectOrderByDriverPhone(driverInfo.getDriverphone());
					for (Order order : orderList) {
						OrderDetail orderDetail = new OrderDetail();
						orderDetail.setOrderId(order.getOrderid());
						orderDetail.setDeparture(order.getDeparture());
						orderDetail.setDestination(order.getDestination());
						orderDetail.setFee(order.getFee().toString());
						orderDetail.setTime(DateUtils.todayDate(order.getOrdertime()));
						orderDetail.setStatus(order.getStatus());
						list.add(orderDetail);
					}
					result.put("detailList", list);
					resultBean = new ResultBean<Object>(
							ResponseCode.SUCCESS.value(),
							ResponseCode.SUCCESS.message(),result);
				}else{
					resultBean = new ResultBean<Object>(
							ResponseCode.NO_USER.value(),
							ResponseCode.NO_USER.message(),result);
				}
			}
		} catch (Exception e) {
			resultBean = new ResultBean<Object>(
					ResponseCode.ERROR.value(),
					"服务器繁忙，请稍后再试！");
		}
		
		
		return Utils.resultResponseJson(resultBean, null);
	}
	
	/**
	 * 乘客订单列表
	 */
	@ResponseBody
	@RequestMapping(value = "/orderPassengerList", method = RequestMethod.POST)
	public ResponseEntity<String> orderPassengerList(){
		ResultBean<?> resultBean = null;
		try {
			User user = (User)session.getAttribute("CURRENT_USER");
			String passengerPhone = user.getUserphone();
			Map<String, Object> result = new HashMap<String, Object>();
			List<OrderDetail> list = new ArrayList<OrderDetail>();
			if (!StringUtils.isOrNotEmpty(passengerPhone)) {
				resultBean = new ResultBean<Object>(
						ResponseCode.PARAMETER_WRONG.value(),
						ResponseCode.PARAMETER_WRONG.message(),result);
			}else {
				PassengerInfo passenger = passengerInfoService.selectByPhone(passengerPhone);
				if (passenger != null) {
					result.put("money","86.86");
					result.put("userName",passenger.getPassengername());
					result.put("userPhoto",passenger.getPassengername());
				}
				List<Order> orderList = orderService.selectOrderByPassengerPhone(user.getUserphone());
				for (Order order : orderList) {
					OrderDetail orderDetail = new OrderDetail();
					orderDetail.setOrderId(order.getOrderid());
					orderDetail.setDeparture(order.getDeparture());
					orderDetail.setDestination(order.getDestination());
					orderDetail.setFee(order.getFee().toString());
					orderDetail.setTime(DateUtils.todayDate(order.getOrdertime()));
					orderDetail.setStatus(order.getStatus());
					list.add(orderDetail);
				}
				result.put("detailList", list);
				resultBean = new ResultBean<Object>(
						ResponseCode.SUCCESS.value(),
						ResponseCode.SUCCESS.message(),result);
			}
		} catch (Exception e) {
			resultBean = new ResultBean<Object>(
					ResponseCode.ERROR.value(),
					"服务器繁忙，请稍后再试！");
		}
		
		return Utils.resultResponseJson(resultBean, null);
	}
	
}
