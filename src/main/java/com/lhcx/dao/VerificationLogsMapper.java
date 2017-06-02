package com.lhcx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lhcx.model.VerificationLogs;

/**
 * 认证信息日志dao
 * @author YangGuang
 *
 */
public interface VerificationLogsMapper {

    List<VerificationLogs> selectByDriverPhone(@Param("driverPhone")String driverPhone);

}