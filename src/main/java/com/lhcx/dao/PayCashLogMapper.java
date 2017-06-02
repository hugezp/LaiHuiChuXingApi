package com.lhcx.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.PayCashLog;

public interface PayCashLogMapper {

    int insertSelective(PayCashLog record);

    PayCashLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayCashLog record);
    
    /**
     * 当日流水
     * @param driverphone
     * @return
     */
    BigDecimal selectCashByDriverPhoneToday(String driverphone);
    
    List<PayCashLog> selectByDriverPhone(@Param("driverPhone") String driverPhone,@Param("startPro") int startPro,@Param("pageSize") int pageSize);;

}