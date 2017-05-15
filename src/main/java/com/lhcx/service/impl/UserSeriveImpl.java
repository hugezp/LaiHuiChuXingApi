package com.lhcx.service.impl;

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
	
	public User selectUserByPhone(String phone,String userType) {
		User user= userMapper.selectUserByPhone(phone, userType);
		DriverInfo driverInfo = null;
        PassengerInfo passengerInfo = null;
        if(user != null){
        	 if (userType.equals(UserType.DRIVER.value())) {
             	driverInfo = driverInfoService.selectByPhone(phone);
             	user.setDriverInfo(driverInfo);
     		}else if (userType.equals(UserType.PASSENGER.value())) {
     			passengerInfo = passengerInfoService.selectByPhone(phone);
     			user.setPassengerInfo(passengerInfo);
     		}
        }
       
		return user;
	}
	
	public int insert(User record){
		return userMapper.insert(record);
	}
	
	public int updateByPrimaryKeySelective(User record){
		return userMapper.updateByPrimaryKeySelective(record);
	}
	
	public ResultBean<?> login(HttpServletRequest request,JSONObject jsonRequest) {	
		ResultBean<?> resultBean = null;
		
		//取得参数值
		String phone = jsonRequest.getString("phone");
        String userType = jsonRequest.getString("userType");
        String code = jsonRequest.getString("code");
        
        //1、校验用户是否存在
        //2、如果用户存在，校验验证码        
		User user = selectUserByPhone(phone, userType);
		if(user != null && (user.getDriverInfo() != null || user.getPassengerInfo() != null)){
			if(verificationCodeService.checkPhoneCode(phone, userType, code)){
				//验证成功后保存登录信息
				user.setLogintime(Utils.currentTimestamp());
				user.setUpatetime(Utils.currentTimestamp());
				user.setLoginip(Utils.getIpAddr(request));
				updateByPrimaryKeySelective(user);
				
				resultBean = new ResultBean<Object>(ResponseCode.getSuccess(),"登录成功！",user);
			}else{
				//验证失败
				resultBean = new ResultBean<Object>(ResponseCode.getSms_checked_failed(),"验证失败！请检查手机号或验证码，验证码有效期30分钟！");    
			}
		}else{
			//用户不存在
			resultBean = new ResultBean<Object>(ResponseCode.get_no_user(),"登录失败!用户不存在");		        
		}
		
		return resultBean;
	}
	
	public boolean registerForDriver(HttpServletRequest request,JSONObject jsonRequest) {
		boolean result = false;
		String phone = jsonRequest.getString("phone");
		String userType = UserType.DRIVER.value();
		
		User user = selectUserByPhone(phone, userType);
		DriverInfo driverInfo = new DriverInfo(jsonRequest);
		if (user == null) {
			//step1：保存driver 信息	
			driverInfo = new DriverInfo(jsonRequest);
			driverInfo.setCreatetime(Utils.currentTimestamp());
			driverInfo.setUpdatetime(Utils.currentTimestamp());
			driverInfoService.insert(driverInfo);
			
			//step2:保存user信息 
			user = new User();
			user.setUserphone(phone);
			user.setUsertype(userType);
			user.setToken(MD5Kit.encode(userType+"@"+phone));
			user.setCreatetime(Utils.currentTimestamp());
			user.setUpatetime(Utils.currentTimestamp());
			insert(user);
			
		} else {
			//step1：更新driver 信息
			driverInfo.setFlag(2);
			driverInfo.setUpdatetime(Utils.currentTimestamp());
			driverInfoService.updateByPhoneSelective(driverInfo);
		}
		
		return result;
	}
}
