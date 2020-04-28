package com.gmsj.dao;
import org.apache.ibatis.annotations.Param;

import com.gmsj.model.po.AuthPo;
import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @author baojieren
 * @date 2020/4/20 18:00
 */
public interface AuthDao extends Mapper<AuthPo> {

    AuthPo selectOneByAuthAccount(@Param("authAccount")String authAccount);

}