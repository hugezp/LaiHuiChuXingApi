package com.lhcx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhcx.dao.VerificationLogsMapper;
import com.lhcx.model.VerificationLogs;
import com.lhcx.service.IVerificationLogsService;

@Transactional(rollbackFor=Exception.class)
@Service
public class VerificationLogsServiceImpl implements IVerificationLogsService {

	@Autowired
	private VerificationLogsMapper verificationLogsMapper;
	
	public List<VerificationLogs> selectByDriverPhone(String driverPhone) {
		return verificationLogsMapper.selectByDriverPhone(driverPhone);
	}

}
