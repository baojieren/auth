package com.gmsj.model.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author baojieren
 * @date 2020/4/16 16:13
 */
@Data
public class SignInBo implements Serializable {
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
     * 职务
     */
    private String job;

    /**
     * 角色列表
     */
    private Set<String> roleList;

    /**
     * 行为列表
     */
    private Set<String> actionList;

    /**
     * token
     */
    private String token;
}
