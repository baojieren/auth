package com.gmsj.model.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Data;

/**
 * 用户账号表,手机号+密码登录/微博登录/QQ登录
 *
 * @author baojieren
 * @date 2020/4/20 18:00
 */
@Data
@Table(name = "auth")
public class AuthPo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 登录类型, 0:微信登录,1:手机号密码登录
     */
    private Integer authType;

    /**
     * 授权账号: 1.手机+密码登录=手机号 2:微信登录=openId
     */
    private String authAccount;

    /**
     * 密码,授权码
     */
    private String authToken;

    /**
     * 0:无效1:有效
     */
    private Integer valid;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}