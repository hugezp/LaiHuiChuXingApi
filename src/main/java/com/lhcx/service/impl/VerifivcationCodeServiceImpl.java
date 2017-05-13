package com.lhcx.service.impl;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhcx.dao.VerificationCodeMapper;
import com.lhcx.model.VerificationCode;
import com.lhcx.service.IVerificationCodeService;
import com.lhcx.utils.SmsUtils;

@Transactional(rollbackFor=Exception.class)
@Service(value="verificationCodeService")
public class VerifivcationCodeServiceImpl implements IVerificationCodeService{

    @Resource
    private VerificationCodeMapper verificationCodeMapper;

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
        verificationCode.setCreatetime(new Timestamp(System.currentTimeMillis()));
        
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
    
    public void test() {
    	createSMS("13862149157","1234","pasenger");
		createSMS(null,"1234","pasenger");
		
	}

}