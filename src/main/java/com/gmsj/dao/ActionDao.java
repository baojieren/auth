package com.gmsj.dao;

import com.gmsj.model.po.ActionPo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * 
 * @author baojieren
 * @date 2020/4/16 14:02
 */
public interface ActionDao extends Mapper<ActionPo> {

    /**
     * 根据userId查询所有的action
     */
    List<ActionPo> selectActionsByUserId(int userId);
}