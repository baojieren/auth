package com.gmsj.model.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户表
 */
@Data
public class SignInSuccessBo implements Serializable {
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String logo;

    /**
     * 微信openId
     */
    private String openId;

    /**
     * token
     */
    private String token;

    /**
     * 角色
     */
    private List<String> roleList;
}