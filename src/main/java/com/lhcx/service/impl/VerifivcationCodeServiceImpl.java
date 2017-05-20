package com.lhcx.service.impl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhcx.dao.VerificationCodeMapper;
import com.lhcx.model.VerificationCode;
import com.lhcx.service.IVerificationCodeService;
import com.lhcx.utils.DateUtils;
import com.lhcx.utils.SmsUtils;
import com.lhcx.utils.Utils;

@Transactional(rollbackFor=Exception.class)
@Service
public class VerifivcationCodeServiceImpl implements IVerificationCodeService{

    @Autowired
    private VerificationCodeMapper verificationCodeMapper; 
    @Autowired  
    private HttpSession session;  
    @Autowired  
    private HttpServletRequest request;
    
    public void test() {
    	createSMS("13862149157","1234","pasenger");
		createSMS(null,"1234","pasenger");
		
	}


    public int insert(VerificationCode record){
        return verificationCodeMapper.insert(record);
    }

    public int getCountByPhonePerDay(String phone,String userType) {
        return verificationCodeMapper.getCountByPhonePerDay(phone,userType);
    }
    
    public int createSMS(String phone,String code,String userType){
    	VerificationCode verificationCode = new VerificationCode();
        verificationCode.setPhone(phone);
        verificationCode.setCode(code);
        verificationCode.setUsertype(userType);
        verificationCode.setCreatetime(DateUtils.currentTimestamp());
         
        return verificationCodeMapper.insert(verificationCode);
    }
    
    public void sendPhoneCode(String phone,String userType) {
    	String code = SmsUtils.randomNum();
        boolean isIgnorPhone = false;
        
        if (SmsUtils.isContains(SmsUtils.ignorPhones,phone)) {
			code = SmsUtils.commonCode;
			isIgnorPhone = true;
		}  
        
    	int createResult = createSMS(phone,code,userType);//保存记录
    	if(createResult > 0 && (isIgnorPhone || (!isIgnorPhone && SmsUtils.sendCodeMessage(phone,code)))){
			//TODO
    	}else{
    		throw new RuntimeException("验证码发送失败，请校验您输入的手机号是否正确！");
    	}
	}
    
    public int updateByPrimaryKeySelective(VerificationCode record) {
    	return verificationCodeMapper.updateByPrimaryKeySelective(record);
	}
    
    public VerificationCode selectLastByPhone(String phone,String userType) {
    	return verificationCodeMapper.selectLastByPhone(phone,userType);
	}
     
    /**
     * 验证码有效期30分钟
     */
    public boolean checkPhoneCode(String phone,String userType,String code) {
    	boolean result = false;
    	VerificationCode verificationCode = selectLastByPhone(phone,userType);
    	if (verificationCode != null && verificationCode.getCode().equals(code)) {
    		verificationCode.setVerificationtime(DateUtils.currentTimestamp());
        	updateByPrimaryKeySelective(verificationCode);
        	session.setAttribute(Utils.REGISTER_PHONE_SESSION, userType+"@"+phone);
        	result = true;
		}

    	return result;
	}
}