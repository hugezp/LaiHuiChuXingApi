package com.lhcx.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.dao.OrderMapper;
import com.lhcx.model.DriverInfo;
import com.lhcx.model.DriverLocation;
import com.lhcx.model.Order;
import com.lhcx.model.OrderLog;
import com.lhcx.model.OrderStatus;
import com.lhcx.model.PushNotification;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;
import com.lhcx.model.UserType;
import com.lhcx.service.IDriverInfoService;
import com.lhcx.service.IDriverLocationService;
import com.lhcx.service.IOrderLogService;
import com.lhcx.service.IOrderService;
import com.lhcx.service.IPushNotificationService;
import com.lhcx.utils.ConfigUtils;
import com.lhcx.utils.DateUtils;
import com.lhcx.utils.JpushClientUtil;
import com.lhcx.utils.MD5Kit;
import com.lhcx.utils.Utils;

@Transactional(rollbackFor=Exception.class)
@Service
public class OrderServiceImpl implements IOrderService {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private IDriverLocationService driverLocationService;
	@Autowired
	private IDriverInfoService driverInfoService;
	@Autowired  
    private HttpSession session;
	@Autowired
	private IPushNotificationService pushNotificationService;
	@Autowired
	private IOrderLogService orderLogService;
	
	public int insertSelective(Order order) {
		return orderMapper.insertSelective(order);
	}
	
	public int updateByOrderIdSelective(Order order){
		return orderMapper.updateByOrderIdSelective(order);
	}
	
	public Order selectByOrderId(String orderId) {
		Order order = orderMapper.selectByOrderId(orderId);
		
		return setLogOrderByOrder(order);
	}
	
	public Order setLogOrderByOrder(Order order) {
		String orderId = order.getOrderid();
		List<OrderLog> orderLogs = orderLogService.selectByOrderId(orderId);
		order.setOrderLogs(orderLogs);
		//10分钟没有接单，订单失效。
		if (order.getStatus() == OrderStatus.BILL.value() && order.getDeparttime().getTime() < new Date().getTime() - ConfigUtils.ORDER_TO_LIVE ) {
			OrderLog orderLog = new OrderLog();
			orderLog.setOrderid(orderId);
			orderLog.setOldstatus(OrderStatus.BILL.value());
			orderLog.setOperatorstatus(OrderStatus.FAILURE.value());
			orderLog.setOperatordescription(OrderStatus.FAILURE.message());
			orderLog.setOperatorphone("");
			orderLog.setDescription("由于长时间没有司机接单，订单自动失效。");
			orderLog.setOperatortime(new Date());
			orderLog.setOperatortype(3);//操作人员类型:：1-乘客，2-驾驶员，3-平台公司			
			
			//更新订单日志
			orderLogService.insertSelective(orderLog);
			orderLogs = orderLogService.selectByOrderId(orderId);
			
			//更新订单最终状态
			order.setOrderLogs(orderLogs);
			order.setStatus(OrderStatus.FAILURE.value());
			order.setOldstatus(OrderStatus.BILL.value());
			updateByOrderIdSelective(order);
		}
		
		return order;
	}
	
	public String create(JSONObject jsonRequest) throws ParseException {
		String result = "";
		Order order = new Order(jsonRequest);
		String orderId = MD5Kit.encode(String.valueOf(System.currentTimeMillis()));
		order.setOrderid(orderId);
		order.setStatus(OrderStatus.BILL.value());
		//保存订单日志记录
		Integer operatortype = jsonRequest.getInteger("OperatorType");
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderid(orderId);
		orderLog.setOperatorphone(order.getPassengerphone());
		orderLog.setOperatortime(new Date());
		orderLog.setOperatorstatus(OrderStatus.BILL.value());
		orderLog.setOperatordescription(OrderStatus.BILL.message());
		orderLog.setOperatortype(operatortype);
		
		if (insertSelective(order) > 0 && orderLogService.insertSelective(orderLog) > 0) {
			result = orderId;
		}
		
		return result;
	}
	
	public ResultBean<?> match(JSONObject jsonRequest) throws Exception {
		ResultBean<?> resultBean = null;
		Map<String,Object> result = new HashMap<String, Object>();		
		
		User user = (User)session.getAttribute("CURRENT_USER");
		String driverPhone = user.getUserphone();
		DriverLocation driverLocation = driverLocationService.selectByPhone(driverPhone);
		String orderId = jsonRequest.getString("OrderId");
		Order order = selectByOrderId(orderId);
		Order olBean = selectNewOrderByDriverPhone(driverPhone);
		if (olBean == null || olBean.getStatus() >= OrderStatus.ARRIVE.value() || olBean.getStatus() <= OrderStatus.CANCEL.value()) {
			if (driverLocation != null && driverLocation.getIsdel() == 1) {
				if (order == null || order.getStatus() != OrderStatus.BILL.value()) {
					resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
							"该订单已失效或已被接单，如有疑问请联系售后！");
				}else {
					String longitude = jsonRequest.getString("Longitude");
					String latitude = jsonRequest.getString("Latitude");
					Integer operatortype = jsonRequest.getInteger("OperatorType");
					Date distributeTime = new Date();
					
					//step1：保存订单日志记录				
					OrderLog orderLog = new OrderLog();
					orderLog.setOrderid(orderId);
					orderLog.setOperatorphone(driverPhone);
					orderLog.setOperatortime(distributeTime);
					orderLog.setOperatorstatus(OrderStatus.Receiving.value());
					orderLog.setOperatordescription(OrderStatus.Receiving.message());
					orderLog.setOldstatus(OrderStatus.BILL.value());
					orderLog.setOperatortype(operatortype);
					orderLogService.insertSelective(orderLog);
					
					//step1.1：保存订单最终状态
					order.setDriverphone(driverPhone);
					order.setStatus(OrderStatus.Receiving.value());
					updateByOrderIdSelective(order);
					
					//step2：推送给发单乘客
					DriverInfo driverInfo = driverInfoService.selectByPhone(driverPhone);
					String vehicleNo = driverInfo.getVehicleNo();
					String distributeTimeString =  DateUtils.dateFormat(distributeTime);
					
					String content = "【来回出行】您的行程订单已被接单，请查看!";
					
					String passengerPhone = order.getPassengerphone();
					
					Map<String, String> extrasParam= new HashMap<String, String>();
					extrasParam.put("OrderId", orderId);
					extrasParam.put("passengerPhone", passengerPhone);
					extrasParam.put("DriverPhone", driverPhone);
					extrasParam.put("DistributeTime", distributeTimeString);				
					
					int flag = JpushClientUtil.getInstance(ConfigUtils.PASSENGER_JPUSH_APP_KEY,ConfigUtils.PASSENGER_JPUSH_MASTER_SECRET)
							.sendToRegistrationId("11", passengerPhone,
									content, content, content,
									extrasParam);
					
					if (flag == 1) {
						PushNotification pushNotification = new PushNotification();
						pushNotification.setPushPhone(driverPhone);
						pushNotification.setReceivePhone(passengerPhone);
						pushNotification.setOrderId(orderId);
						pushNotification.setAlert(content);
						pushNotification.setPushType(1);
						pushNotification.setData(extrasParam.toString());
						pushNotificationService.insertSelective(pushNotification);
					}else {
						throw new Exception();
					}
									
					//step3：返回结果字段
					result.put("OrderId", orderId);
					result.put("DriverPhone", driverPhone);
					result.put("VehicleNo", vehicleNo);
					result.put("Longittude", longitude);
					result.put("Latitude", latitude);
					result.put("DistributeTime",distributeTimeString);				

					resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
							"接单成功！",result);
				}
			} else {
				resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
						"您没有经营上线，请经营上线后接单！");
			}
		}else {
			resultBean = new ResultBean<Object>(ResponseCode.DEPART_ORDER_REPEAT.value(),
					"您已接单,不能抢单！");
		}
		return resultBean;
	}
	
	public ResultBean<?> cancel(JSONObject jsonRequest) throws Exception {
		ResultBean<?> resultBean = null;
		String orderId = jsonRequest.getString("OrderId");
		String operator = jsonRequest.getString("Operator");
		String cancelTypeCode = jsonRequest.getString("CancelTypeCode");
		String cancelReason = jsonRequest.getString("CancelReason");
		
		User user = (User)session.getAttribute("CURRENT_USER");
		Order order = selectByOrderId(orderId);
		
		String userPhone = user.getUserphone();
		String passenegerPhone = order.getPassengerphone();
		String driverPhone = order.getDriverphone();
		
		if ( !userPhone.equals(passenegerPhone)  && !userPhone.equals(driverPhone)) {
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"您没有权限取消该订单！");
			return resultBean;
		}else if (order.getStatus() > OrderStatus.Receiving.value() ) {
			//订单不可取消状态
			resultBean = new ResultBean<Object>(ResponseCode.CANCEL_ORDER_FAILED.value(),
					"司机已发车，订单不能被取消！");
			return resultBean;
		}else if(order.getStatus() == OrderStatus.FAILURE.value() || order.getStatus() == OrderStatus.CANCEL.value()){
			//订单不可取消状态
			resultBean = new ResultBean<Object>(ResponseCode.CANCEL_ORDER_FAILED.value(),
					"订单已取消或已失效，不能被取消！");
			return resultBean;
		}
		
		//更新订单日志
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderid(orderId);
		orderLog.setOperatorphone(user.getUserphone());
		orderLog.setOperatorstatus(OrderStatus.CANCEL.value());
		orderLog.setOperatordescription(OrderStatus.CANCEL.message());
		orderLog.setOldstatus(order.getStatus());
		orderLog.setDescription(cancelReason);
		if (operator != null) {
			orderLog.setOperatortype(Integer.parseInt(operator));
		}
		orderLog.setOperatortime(new Date());
		orderLog.setCanceltypecode(Integer.parseInt(cancelTypeCode));
		
		orderLogService.insertSelective(orderLog);
		
		//更新订单最终状态
		order.setStatus(OrderStatus.CANCEL.value());
		updateByOrderIdSelective(order);
				
		//step2：推送给接单司机
		
		String content = "【来回出行】您的行程订单已被取消，请前往查看!";
		
		Map<String, String> extrasParam= new HashMap<String, String>();
		extrasParam.put("OrderId", orderId);
		extrasParam.put("passengerPhone", passenegerPhone);
		extrasParam.put("DriverPhone", driverPhone);				
		
		String userType = user.getUsertype();
		int flag = 0;
		PushNotification pushNotification = new PushNotification();
		if (userType.equals(UserType.PASSENGER.value()) && !Utils.isNullOrEmpty(driverPhone)) {
			flag = JpushClientUtil.getInstance(ConfigUtils.JPUSH_APP_KEY,ConfigUtils.JPUSH_MASTER_SECRET)
					.sendToRegistrationId("11", driverPhone,
							content, content, content,
							extrasParam);
			pushNotification.setPushPhone(passenegerPhone);
			pushNotification.setReceivePhone(driverPhone);
		}else {
			flag = JpushClientUtil.getInstance(ConfigUtils.PASSENGER_JPUSH_APP_KEY,ConfigUtils.PASSENGER_JPUSH_MASTER_SECRET)
					.sendToRegistrationId("11", passenegerPhone,
							content, content, content,
							extrasParam);
			pushNotification.setPushPhone(driverPhone);
			pushNotification.setReceivePhone(passenegerPhone);
		}
		
		if (flag == 1) {
			pushNotification.setOrderId(orderId);
			pushNotification.setAlert(content);
			pushNotification.setPushType(1);
			pushNotification.setData(extrasParam.toString());
			pushNotificationService.insertSelective(pushNotification);
		}else {
			throw new Exception();
		}
		
		resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
				"订单取消成功！");
		return resultBean;
		
	}
	
	public ResultBean<?> reached(JSONObject jsonRequest) {
		ResultBean<?> resultBean = null;
		String orderId = jsonRequest.getString("OrderId");
		String operator = jsonRequest.getString("Operator");
		
		User user = (User)session.getAttribute("CURRENT_USER");
		Order order = selectByOrderId(orderId);

		String userPhone = user.getUserphone();
		String driverPhone = order.getDriverphone();		
		
		if ( !userPhone.equals(driverPhone)) {
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"您没有权限操作该订单！");
			return resultBean;
		}else if (order.getStatus() != OrderStatus.Receiving.value()) {
			//订单处于接单状态才可操作
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"订单操作失败！该订单没有处于接单状态。");
			return resultBean;
		}
		
		//更新订单日志
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderid(orderId);
		orderLog.setOperatorphone(user.getUserphone());
		orderLog.setOperatorstatus(OrderStatus.REACHED.value());
		orderLog.setOperatordescription(OrderStatus.REACHED.message());
		orderLog.setOldstatus(order.getStatus());
		if (operator != null) {
			orderLog.setOperatortype(Integer.parseInt(operator));
		}
		orderLog.setOperatortime(new Date());
		orderLogService.insertSelective(orderLog);
		
		//更新订单最终状态
		order.setStatus(OrderStatus.REACHED.value());
		updateByOrderIdSelective(order);
		
		resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
				"订单操作成功！");
		return resultBean;
	}
	
	public ResultBean<?> depart(JSONObject jsonRequest) {
		ResultBean<?> resultBean = null;
		String orderId = jsonRequest.getString("OrderId");
		String operator = jsonRequest.getString("Operator");
		
		User user = (User)session.getAttribute("CURRENT_USER");
		Order order = selectByOrderId(orderId);
		
		String userPhone = user.getUserphone();
		String driverPhone = order.getDriverphone();
		
		if ( !userPhone.equals(driverPhone)) {
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"您没有权限操作该订单！");
			return resultBean;
		}else if (order.getStatus() != OrderStatus.Receiving.value()) {
			//订单处于接单状态才可操作
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"订单操作失败！该订单没有处于接单状态。");
			return resultBean;
		}
		
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderid(orderId);
		orderLog.setOperatorphone(user.getUserphone());
		orderLog.setOperatorstatus(OrderStatus.ABORAD.value());
		orderLog.setOperatordescription(OrderStatus.ABORAD.message());
		orderLog.setOldstatus(order.getStatus());
		if (operator != null) {
			orderLog.setOperatortype(Integer.parseInt(operator));
		}
		orderLog.setOperatortime(new Date());
		orderLogService.insertSelective(orderLog);
		
		//更新订单最终状态
		order.setStatus(OrderStatus.ABORAD.value());
		updateByOrderIdSelective(order);
		
		resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
				"订单操作成功！");
		return resultBean;
	}
	
	public ResultBean<?> arrive(JSONObject jsonRequest) {
		ResultBean<?> resultBean = null;
		String orderId = jsonRequest.getString("OrderId");
		String operator = jsonRequest.getString("Operator");
		
		User user = (User)session.getAttribute("CURRENT_USER");
		Order order = selectByOrderId(orderId);
		
		String userPhone = user.getUserphone();
		String driverPhone = order.getDriverphone();
		
		if ( !userPhone.equals(driverPhone)) {
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"您没有权限操作该订单！");
			return resultBean;
		}else if (order.getStatus() != OrderStatus.ABORAD.value()) {
			//订单处于已发车状态才可操作
			resultBean = new ResultBean<Object>(ResponseCode.ERROR.value(),
					"订单操作失败！该订单没有处于发车状态。");
			return resultBean;
		}
		
		OrderLog orderLog = new OrderLog();
		orderLog.setOrderid(orderId);
		orderLog.setOperatorphone(user.getUserphone());
		orderLog.setOperatorstatus(OrderStatus.ARRIVE.value());
		orderLog.setOperatordescription(OrderStatus.ARRIVE.message());
		orderLog.setOldstatus(order.getStatus());
		if (operator != null) {
			orderLog.setOperatortype(Integer.parseInt(operator));
		}
		orderLog.setOperatortime(new Date());
		orderLogService.insertSelective(orderLog);
		
		//更新订单最终状态
		order.setStatus(OrderStatus.ARRIVE.value());
		updateByOrderIdSelective(order);
		
		resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),
				"订单操作成功！");
		return resultBean;
		
	}
	
	public Order info(String orderId) {
		Order order = selectByOrderId(orderId); 
		String driverPhone =  order.getDriverphone();
		if (!Utils.isNullOrEmpty(driverPhone) ) {
			if (order.getStatus() == OrderStatus.Receiving.value() ) {
				//接单后距离乘客上车实时位置
				DriverLocation driverLocation = driverLocationService.selectOnTimeDistance(driverPhone,Long.parseLong(order.getDeplongitude()) ,Long.parseLong(order.getDeplatitude()));
				order.setOnTimeDistance(driverLocation.getDistance());
				
			}else if (order.getStatus() == OrderStatus.ABORAD.value()) {
				//接到乘客后距离目的地实时位置
				DriverLocation driverLocation = driverLocationService.selectOnTimeDistance(driverPhone,Long.parseLong(order.getDestlongitude()) ,Long.parseLong(order.getDestlatitude()));
				order.setOnTimeTotalDistance(driverLocation.getDistance());
			}
		}
		
		return order;
	}

	public Order selectNewOrderByPhone(String passengerPhone) {
		return orderMapper.selectNewOrderByPhone(passengerPhone);
	}
	
	public Order selectNewOrderByDriverPhone(String driverPhone) {
		return orderMapper.selectNewOrderByDriverPhone(driverPhone);
	}

	@Override
	public List<Order> selectOrderByPassengerPhone(String passengerPhone) {
		return orderMapper.selectOrderByPassengerPhone(passengerPhone);
	}

	@Override
	public List<Order> selectOrderByDriverPhone(String driverPhone) {
		return orderMapper.selectOrderByDriverPhone(driverPhone);
	}

}
