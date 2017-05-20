package com.lhcx.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
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
import com.lhcx.model.OrderType;
import com.lhcx.model.PushNotification;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;
import com.lhcx.service.IDriverInfoService;
import com.lhcx.service.IDriverLocationService;
import com.lhcx.service.IOrderService;
import com.lhcx.service.IPushNotificationService;
import com.lhcx.utils.ConfigUtils;
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
	
	public int insertSelective(Order order) {
		return orderMapper.insertSelective(order);
	}
	
	public int updateByOrderIdSelective(Order order){
		return orderMapper.updateByOrderIdSelective(order);
	}
	
	public Order selectByOrderId(String orderId) {
		return orderMapper.selectByOrderId(orderId);
	}
	
	public String create(JSONObject jsonRequest) throws ParseException {
		String result = "";
		Order order = new Order(jsonRequest);
		String orderId = MD5Kit.encode(String.valueOf(System.currentTimeMillis()));
		order.setOrderid(orderId);
		if (insertSelective(order) > 0) {
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
		
		if (driverLocation != null && driverLocation.getIsdel() == 1) {
			if (order == null || order.getStatus() != 1) {
				resultBean = new ResultBean<Object>(ResponseCode.getError(),
						"该订单已失效或已被接单，如有疑问请联系售后！");
			}else {
				String longitude = jsonRequest.getString("Longitude");
				String latitude = jsonRequest.getString("Latitude");
				Integer encrypt = jsonRequest.getInteger("Encrypt");
				Date distributeTime = new Date();
				
				//step1：保存接单信息
				order.setDriverphone(driverPhone);
				order.setLongitude(longitude);
				order.setLatitude(latitude);
				order.setEncrypt(encrypt);
				order.setDistributetime(distributeTime);
				order.setStatus(OrderType.Receiving.value());
				updateByOrderIdSelective(order);
				
				//step2：推送给发单乘客
				DriverInfo driverInfo = driverInfoService.selectByPhone(driverPhone);
				String vehicleNo = driverInfo.getVehicleNo();
				String distributeTimeString =  Utils.dateFormat(distributeTime);
				
				StringBuffer contentBuffer = new StringBuffer();
				contentBuffer.append("您的行程订单已被接单，司机手机号为：").append(driverPhone)
				.append("，车牌号为：").append(vehicleNo).append("，接单时间为：").append(distributeTimeString);
				
				StringBuffer extrasParamBuffer = new StringBuffer();
				extrasParamBuffer.append("");
				
				String content = contentBuffer.toString();
				String extrasParam = extrasParamBuffer.toString();
				String passengerPhone = order.getPassengerphone();
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

				resultBean = new ResultBean<Object>(ResponseCode.getSuccess(),
						"接单成功！",result);
			}
		} else {
			resultBean = new ResultBean<Object>(ResponseCode.getError(),
					"您没有经营上线，请经营上线后接单！");
		}
				
		return resultBean;
	}

}
