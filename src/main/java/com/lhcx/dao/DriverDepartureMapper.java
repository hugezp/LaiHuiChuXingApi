package com.lhcx.dao;

import com.lhcx.model.DriverDeparture;

public interface DriverDepartureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DriverDeparture record);

    int insertSelective(DriverDeparture record);

    DriverDeparture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DriverDeparture record);

    int updateByPrimaryKey(DriverDeparture record);
}