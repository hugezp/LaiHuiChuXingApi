package com.lhcx.service;

import java.util.List;

import com.lhcx.model.VerificationLogs;

/**
 * 车主认证信息日志service
 * @author YangGuang
 *
 */
public interface IVerificationLogsService {

    List<VerificationLogs> selectByDriverPhone(String driverPhone);
}
