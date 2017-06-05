package com.lhcx.service;

import com.lhcx.model.VerificationCode;

/**
 * Created by lh on 2017/5/10.
 * author：william
 * description：验证码接口 
 */
public interface IVerificationCodeService {
    int insert(VerificationCode record);
    int getCountByPhonePerDay(String phone,String userType);
    int createSMS(String phone,String code,String userType);
    void sendPhoneCode(String phone,String userType);
    VerificationCode selectLastByPhone(String phone,String userType);
    /**
     * code有效期:半个小时
     */
    boolean checkPhoneCode(String phone,String userType,String code,String type);
    
    int updateByPrimaryKeySelective(VerificationCode record);
    
    void test();
}
