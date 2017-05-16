package com.lhcx.dao;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    User selectUserByPhone(@Param("phone") String phone,@Param("userType") String userType);
    
    User selectByToken(@Param("token") String token);
}