package com.lhcx.service.impl;

import java.sql.Timestamp;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhcx.dao.VerificationCodeMapper;
import com.lhcx.model.VerificationCode;
import com.lhcx.service.IVerificationCodeService;


/**
 * Created by lh on 2017/5/10.
 * author：william
 * desc：验证码接口实现
 */

//没有指定value的话，默认是第一个字母小写的类名,可以看做是xml中的bean的id
@Service(value="verificationCodeService")
public class VerifivcationCodeServiceImpl implements IVerificationCodeService{

    private static Logger logger = Logger.getLogger(VerifivcationCodeServiceImpl.class);
    @Resource
    private VerificationCodeMapper verificationCodeMapper;

    @Transactional(readOnly = false)
    public int insert(VerificationCode record){
        int result = 0;
        try{
            result = verificationCodeMapper.insert(record);
        }catch(Exception ex){
            logger.error("Exception occured while insert verificationCode,the exception is "+ ex.getMessage() );
            ex.printStackTrace();
        }
        return result;
    }

    @Transactional(readOnly = true)
    public int getCountByPhonePerDay(String phone,String userType) {
        int result = 0;
        try{
            result = 0;//verificationCodeMapper.getCountByPhonePerDay(phone,userType);
        }catch(Exception ex){
            logger.error("Exception occured while doing getCountByPhonePerDay,the exception is "+ ex.getMessage() );
            ex.printStackTrace();
        }
        return result;
    }
    @Transactional(readOnly = false)
    public int createSMS(String phone,String code,String userType){
        int result = 0;
        try{
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setPhone(phone);
            verificationCode.setCode(code);
            verificationCode.setUsertype(userType);
            verificationCode.setCreatetime(new Timestamp(System.currentTimeMillis()));
            result = verificationCodeMapper.insert(verificationCode);
        }catch(Exception ex){
            logger.error("Exception occured while doing createSMS,the exception is "+ ex.getMessage() );
            ex.printStackTrace();
        }
        return result;
    }

}