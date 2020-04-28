package com.gmsj.dao;

import com.gmsj.model.po.RolePo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author baojieren
 * @date 2020/4/16 14:02
 */
public interface RoleDao extends Mapper<RolePo> {

    List<RolePo> selectRoleListByUserId(int userId);

    RolePo selectOneByRoleTag(@Param("roleTag") String roleTag);

    /**
     * 添加用户和角色关系
     */
    int insertUser2Role(@Param("userId") int userId, @Param("roleId") int roleId);
}