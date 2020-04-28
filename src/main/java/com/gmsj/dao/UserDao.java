package com.gmsj.dao;

import com.gmsj.model.po.UserPo;
import org.apache.ibatis.annotations.Param;import tk.mybatis.mapper.common.Mapper;

/**
 * @author baojieren
 * @date 2020/4/24 12:52
 */
public interface UserDao extends Mapper<UserPo> {
    UserPo selectOneByPhone(@Param("phone")String phone);

	UserPo selectOneByOpenId(@Param("openId")String openId);


}