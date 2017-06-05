package com.lhcx.service.impl;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.dao.UserMapper;
import com.lhcx.model.DriverInfo;
import com.lhcx.model.PassengerInfo;
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;
import com.lhcx.model.UserType;
import com.lhcx.service.IDriverInfoService;
import com.lhcx.service.IPassengerInfoService;
import com.lhcx.service.IUserService;
import com.lhcx.service.IVerificationCodeService;
import com.lhcx.utils.DateUtils;
import com.lhcx.utils.MD5Kit;
import com.lhcx.utils.Utils;

@Transactional(rollbackFor=Exception.class)
@Service
public class UserSeriveImpl implements IUserService{
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private IVerificationCodeService verificationCodeService;
	@Autowired
	private IDriverInfoService driverInfoService;
	@Autowired
	private IPassengerInfoService passengerInfoService;
	
	public User selectByToken(String token) {
		User user= userMapper.selectByToken(token);		
		return user;
	}
	
	public User selectUserByPhone(String phone,String userType) {
		User user= userMapper.selectUserByPhone(phone, userType);
		DriverInfo driverInfo = null;
        PassengerInfo passengerInfo = null;
        if(user != null){
        	 if (userType.equals(UserType.DRIVER.value())) {
             	driverInfo = driverInfoService.selectByIdentityToken(user.getIdentityToken());
             	user.setDriverInfo(driverInfo);
     		}else if (userType.equals(UserType.PASSENGER.value())) {
     			passengerInfo = passengerInfoService.selectByIdentityToken(user.getIdentityToken());
     			user.setPassengerInfo(passengerInfo);
     		}
        }
       
		return user;
	}
	
	public int insertSelective(User record){
		return userMapper.insertSelective(record);
	}
	
	public int updateByPrimaryKeySelective(User record){
		return userMapper.updateByPrimaryKeySelective(record);
	}
	
	public ResultBean<?> login(HttpServletRequest request,JSONObject jsonRequest) {	
		ResultBean<?> resultBean = null;
		Map<String,Object> result = new HashMap<String, Object>();
		//取得参数值
		String phone = jsonRequest.getString("phone");
        String userType = jsonRequest.getString("userType");
        String code = jsonRequest.getString("code");
        String token = MD5Kit.encode(userType+"@"+phone + System.currentTimeMillis());
        
        //1、司机端校验用户是否存在
        //2、如果用户存在，校验验证码        
		User user = selectUserByPhone(phone, userType);

		boolean driverBoolean = user != null && userType.equals(UserType.DRIVER.value()) && user.getDriverInfo() != null ;
		//乘客端不需要注册即可登录 
		boolean passengerBoolean = userType.equals(UserType.PASSENGER.value());
		
		if( driverBoolean || passengerBoolean){
			if(verificationCodeService.checkPhoneCode(phone, userType, code,null)){
				//验证成功后保存登录信息
				if (passengerBoolean && user == null) {
					String identityToken = token;
					user = new User();
					user.setUserphone(phone);
					user.setUsertype(userType);		
					user.setToken(token);
					user.setCreatetime(DateUtils.currentTimestamp());
					user.setLogintime(DateUtils.currentTimestamp());
					user.setUpatetime(DateUtils.currentTimestamp());
					user.setLoginip(Utils.getIpAddr(request));
					user.setIdentityToken(identityToken);
					
					insertSelective(user);
				}else {
					user.setLogintime(DateUtils.currentTimestamp());
					user.setUpatetime(DateUtils.currentTimestamp());
					user.setLoginip(Utils.getIpAddr(request));
					user.setToken(token);
					updateByPrimaryKeySelective(user);
				}
				result.put("phone", phone);
				result.put("userType", userType);
				result.put("token", user.getToken());
				result.put("status", user.getFlag());
				
				resultBean = new ResultBean<Object>(ResponseCode.SUCCESS.value(),ResponseCode.SUCCESS.message(),result);
			}else{
				//验证失败
				resultBean = new ResultBean<Object>(ResponseCode.SMS_CHECKED_FAILED.value(),ResponseCode.SMS_CHECKED_FAILED.message());    
			}
		}else{
			//用户不存在
			resultBean = new ResultBean<Object>(ResponseCode.NO_USER.value(),ResponseCode.NO_USER.message());		        
		}
		
		return resultBean;
	}
	
	public User registerForDriver(HttpServletRequest request,JSONObject jsonRequest) throws ParseException {
		String phone = jsonRequest.getString("phone");
		String userType = UserType.DRIVER.value();
		String token = MD5Kit.encode(userType+"@"+phone + System.currentTimeMillis());
		
		User user = selectUserByPhone(phone, userType);
		DriverInfo driverInfo = new DriverInfo(jsonRequest);
		if (user == null) {
			//step1：保存driver 信息	
			driverInfo = new DriverInfo(jsonRequest);
			driverInfo.setCreatetime(DateUtils.currentTimestamp());
			driverInfo.setUpdatetime(DateUtils.currentTimestamp());
			driverInfo.setIdentityToken(token);
			driverInfoService.insertSelective(driverInfo);
			
			//step2:保存user信息 
			user = new User();
			user.setUserphone(phone);
			user.setUsertype(userType);		
			user.setToken(token);
			user.setCreatetime(DateUtils.currentTimestamp());
			user.setLogintime(DateUtils.currentTimestamp());
			user.setUpatetime(DateUtils.currentTimestamp());
			user.setLoginip(Utils.getIpAddr(request));
			user.setIdentityToken(token);
			insertSelective(user);
			
		} else {
			//step1：更新driver 信息
			driverInfo.setFlag(2);
			driverInfo.setUpdatetime(DateUtils.currentTimestamp());
			driverInfoService.updateByIdentityTokenSelective(driverInfo);
			
			//step2:更新user信息 
			user.setLogintime(DateUtils.currentTimestamp());
			user.setUpatetime(DateUtils.currentTimestamp());
			user.setLoginip(Utils.getIpAddr(request));
			user.setToken(token);
			updateByPrimaryKeySelective(user);
		}
		
		return user;
	}
}
