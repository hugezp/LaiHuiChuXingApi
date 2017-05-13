package com.lhcx.service;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.lhcx.model.ResultBean;
import com.lhcx.model.User;

/**
 * Created by lh on 2017/5/10.
 * author：william
 * description：用户登录记录表信息
 */
public interface IUserService {
	
	User selectUserByPhone(String phone,String userType);
	
	int updateByPrimaryKeySelective(User record);
	
	/*
	 * content-type:application/json
     * @param:
     * mobile:手机号
     * userType:用户类型，driver-司机端，passenger-乘客端
     * code:验证码
	*/
	ResultBean<?> login(HttpServletRequest request,JSONObject jsonRequest);
}
