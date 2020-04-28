package com.gmsj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gmsj.common.dto.BaseOutDTO;
import com.gmsj.model.po.ActionPo;
import com.gmsj.model.po.AuthPo;
import com.gmsj.model.po.RolePo;
import com.gmsj.model.po.UserPo;

import java.util.List;

/**
 * @author baojieren
 * @date 2020/4/16 13:45
 */
public interface UserService {

    /**
     * 获取openId
     */
    BaseOutDTO getOpenId(String code) throws Exception;

    /**
     * 微信小程序登录
     */
    BaseOutDTO wxMiniSignIn(String openId, String ip);

    /**
     * mng注册
     */
    BaseOutDTO mngSignUp(AuthPo authPo);

    /**
     * mng登录
     */
    BaseOutDTO mngSignIn(UserPo userPo, String ip);

    /**
     * mng修改资料
     */
    BaseOutDTO mngUpdate(UserPo userPo);

    /**
     * mng登出
     */
    BaseOutDTO mngSignOut(String token);

    /**
     * 查询用户
     */
    BaseOutDTO findOneByPhone(String phone);

    /**
     * 通过userId查询
     */
    BaseOutDTO findOneByUserId(int userId);

    /**
     * 查询角色列表
     */
    List<RolePo> findRoleListByUserId(int userId);

    /**
     * 查询行为列表
     */
    List<ActionPo> findActionListByUserId(int userId);
}
