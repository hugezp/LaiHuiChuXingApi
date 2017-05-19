package com.lhcx.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import com.lhcx.dao.UserMapper;
import com.lhcx.model.DriverLocation;
import com.lhcx.model.PushNotification;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;
import com.lhcx.service.IDriverLocationService;
import com.lhcx.service.IPushNotificationService;
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
	IPushNotificationService pushNotificationService ;
	@Autowired
	private IDriverLocationService driverLocationService;
	@Autowired
	private HttpSession session;
	@ResponseBody
	@RequestMapping(value = "/List", method = RequestMethod.POST)
	public ResponseEntity<String> PushList(@RequestBody JSONObject jsonRequest,HttpServletRequest request) {	
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;	
	  
		try {
			User user = (User)session.getAttribute("CURRENT_USER");
		    String phone = user.getUserphone(); 
			List<PushNotification> pushs = pushNotificationService.selectAll(phone);
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
	
	/**
	 * 推送开关 
	 */
	@ResponseBody
	@RequestMapping(value = "/button", method = RequestMethod.POST)
	public ResponseEntity<String> pushButton(@RequestBody JSONObject jsonRequest,HttpServletRequest request){
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		String isDel = jsonRequest.getString("isDel");
		try {
			User user = (User)session.getAttribute("CURRENT_USER");
			DriverLocation driverLocation = new DriverLocation();
			driverLocation.setPhone(user.getUserphone());
			driverLocation.setIsdel(Integer.parseInt(isDel));
			int flag = driverLocationService.updateByPhoneSelective(driverLocation);
			if (flag > 0) {
				resultBean = new ResultBean<Object>(ResponseCode.getSuccess(),"设置成功！");
			}else {
				resultBean = new ResultBean<Object>(ResponseCode.getError(),"设置失败！");
			}
		} catch (Exception e) {
			resultBean = new ResultBean<Object>(ResponseCode.getError(),"设置失败！");
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
	
	/**
	 * 实时更新经纬度
	 */
	@ResponseBody
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<String> update(@RequestBody JSONObject jsonRequest){
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		ResultBean<?> resultBean = null;
		String longitude = jsonRequest.getString("Longitude");
		String latitude = jsonRequest.getString("Latitude");
		try {
			User user = (User)session.getAttribute("CURRENT_USER");
			DriverLocation driverLocation = new DriverLocation();
			driverLocation.setPhone(user.getUserphone());
			driverLocation.setLongitude(longitude);
			driverLocation.setLatitude(latitude);
			driverLocation.setPositiontime(new Date());
			int flag = driverLocationService.updateByPhoneSelective(driverLocation);
			if (flag > 0) {
				resultBean = new ResultBean<Object>(ResponseCode.getSuccess(),"更新成功！");
			}else {
				resultBean = new ResultBean<Object>(ResponseCode.getError(),"更新失败！");
			}
		} catch (Exception e) {
			resultBean = new ResultBean<Object>(ResponseCode.getError(),"更新失败！");
		}
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
}
