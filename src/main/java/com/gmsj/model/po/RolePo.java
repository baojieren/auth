package com.gmsj.model.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色表
 *
 * @author baojieren
 * @date 2020/4/16 14:02
 */
@Data
@Table(name = "`role`")
public class RolePo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 角色标签
     */
    @Column(name = "role_tag")
    private String roleTag;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 0:无效 1:有效
     */
    @Column(name = "`valid`")
    private Integer valid;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}