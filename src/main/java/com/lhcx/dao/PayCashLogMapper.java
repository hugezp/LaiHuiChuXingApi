package com.lhcx.dao;

import com.lhcx.model.PayCashLog;

public interface PayCashLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PayCashLog record);

    int insertSelective(PayCashLog record);

    PayCashLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayCashLog record);

    int updateByPrimaryKey(PayCashLog record);
}