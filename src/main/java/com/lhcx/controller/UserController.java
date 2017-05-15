package com.lhcx.controller;

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
import com.lhcx.model.ResponseCode;
import com.lhcx.model.ResultBean;
import com.lhcx.service.IUserService;
import com.lhcx.service.IVerificationCodeService;
import com.lhcx.utils.Utils;

/**
 * 用户登录与注册
 * @author william
 *
 */

@Controller
@RequestMapping(value = "/api/user")
public class UserController {
	private static Logger log = Logger.getLogger(UserController.class);
	@Autowired
    private IVerificationCodeService verificationCodeService;
	@Autowired
	private IUserService userSerive;
	
	/**
     * 获取验证码
     * content-type:application/json
     * @param:
     * mobile:手机号
     * userType:用户类型，driver-司机端，passenger-乘客端
     * code:验证码
     * jsonpCallback：跨域参数
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> sendPhoneCode(HttpServletRequest request,@RequestBody JSONObject jsonRequest) {    	
    	//取得参数值
        String jsonpCallback = jsonRequest.getString("jsonpCallback");
        
        ResultBean<?> resultBean = null;      
        try {
        	resultBean = userSerive.login(request,jsonRequest);			
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage());
			e.printStackTrace();
			resultBean = new ResultBean<Object>(ResponseCode.getSms_checked_failed(),"登录失败！服务器繁忙，请稍后重试！");
		}
         return Utils.resultResponseJson(resultBean,jsonpCallback);
    }
    
    
}
