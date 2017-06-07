package com.lhcx.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhcx.dao.SuggestsMapper;
import com.lhcx.model.Suggests;
import com.lhcx.service.ISuggestsService;

@Transactional(rollbackFor=Exception.class)
@Service
public class SuggestsServiceImpl implements ISuggestsService {

	@Autowired
	private SuggestsMapper suggestsMapper;
	
	public int insertSelective(Suggests record) {
		return suggestsMapper.insertSelective(record);
	}

}
