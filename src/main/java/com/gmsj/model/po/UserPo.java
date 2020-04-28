package com.gmsj.model.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.Data;

/**
 * 用户表
 *
 * @author baojieren
 * @date 2020/4/24 12:52
 */
@Data
@Table(name = "`user`")
public class UserPo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 用户手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 头像
     */
    @Column(name = "logo")
    private String logo;

    /**
     * 微信openId
     */
    @Column(name = "open_id")
    private String openId;

    /**
     * 公司id
     */
    @Column(name = "company_id")
    private Integer companyId;

    /**
     * 公司名称
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * 职务
     */
    @Column(name = "job")
    private String job;

    /**
     * 国家名称
     */
    @Column(name = "country_name")
    private String countryName;

    /**
     * 国家编码
     */
    @Column(name = "country_code")
    private String countryCode;

    /**
     * 省份名称
     */
    @Column(name = "province_name")
    private String provinceName;

    /**
     * 省份编码
     */
    @Column(name = "province_code")
    private String provinceCode;

    /**
     * 市区名称
     */
    @Column(name = "city_name")
    private String cityName;

    /**
     * 市区编码
     */
    @Column(name = "city_code")
    private String cityCode;

    /**
     * 详细地址
     */
    @Column(name = "addr")
    private String addr;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 个人描述
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 0: 锁定 1:正常
     */
    @Column(name = "`valid`")
    private Integer valid;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}