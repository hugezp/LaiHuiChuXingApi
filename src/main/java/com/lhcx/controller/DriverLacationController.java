package com.lhcx.controller;

import java.util.Date;

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
import com.lhcx.utils.Utils;

@Controller
public class DriverLacationController {
	@Autowired
	private IDriverLocationService driverLocationService;
	
	@ResponseBody
	@RequestMapping(value = "/position/driver", method = RequestMethod.POST)
	public ResponseEntity<String> positionDriver(@RequestBody JSONObject jsonRequest){
		// 取得参数值
		String jsonpCallback = jsonRequest.getString("jsonpCallback");
		String positionTime = jsonRequest.getString("PositionTime");
		String longitude = jsonRequest.getString("Longitude");
		String latitude = jsonRequest.getString("Latitude");
		ResultBean<?> resultBean = null;
		try {
			DriverLocation driverLocation = new DriverLocation();
			if(null!=positionTime){
				driverLocation.setPositiontime(Utils.getDate(positionTime));		
			}else{
				driverLocation.setPositiontime(new Date());	
			}
			if(null!=longitude){
				driverLocation.setLongitude(longitude);		
			}
			if(null!=latitude){
				driverLocation.setLatitude(latitude);		
			}
			int i=driverLocationService.insert(driverLocation);
			if(i>0){
				resultBean = new ResultBean<Object>(ResponseCode.getSuccess(),"添加车主位置成功！");	
			}else{
				resultBean = new ResultBean<Object>(ResponseCode.getError(),"添加车主位置失败！");	
			}
		} catch (Exception e) {
			// TODO: handle exception			
		}
		
		return Utils.resultResponseJson(resultBean, jsonpCallback);
	}
	

}
