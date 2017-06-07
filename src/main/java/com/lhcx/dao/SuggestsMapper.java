package com.lhcx.dao;

import com.lhcx.model.Suggests;

/**
 * 投诉与建议dao
 * @author YangGuang
 *
 */
public interface SuggestsMapper {

    int insertSelective(Suggests record);
}