package com.lhcx.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.PayCashLog;

public interface PayCashLogMapper {

    int insertSelective(PayCashLog record);

    PayCashLog selectByPrimaryKey(Integer id);

    /**
     * 当日流水
     * @param driverphone
     * @return
     */
    BigDecimal selectCashByIdentityTokenToday(String driverIdentityToken);
    
    List<PayCashLog> selectByIdentityToken(@Param("identityToken") String driverIdentityToken,@Param("startPro") int startPro,@Param("pageSize") int pageSize);;

}