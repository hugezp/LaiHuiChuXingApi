package com.lhcx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.User;

public interface UserMapper {

    int insertSelective(User record);

    int updateByPrimaryKeySelective(User record);
    
    int updatePhoneByIdToken(User record);
    
    User selectUserByIdentityToken(String identityToken);
    
    User selectUserByPhone(@Param("phone") String phone,@Param("userType") String userType);
    
    User selectByToken(@Param("token") String token);
    
    List<User> selectByPhone(User user);
}