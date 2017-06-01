package com.lhcx.dao;

import com.lhcx.model.PayCashLog;

public interface PayCashLogMapper {

    int insertSelective(PayCashLog record);

    PayCashLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayCashLog record);

}