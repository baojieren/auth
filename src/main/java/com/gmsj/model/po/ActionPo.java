package com.gmsj.model.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 行为表
 *
 * @author baojieren
 * @date 2020/4/16 14:02
 */
@Data
@Table(name = "`action`")
public class ActionPo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * 暂定为接口地址
     */
    @Column(name = "action_tag")
    private String actionTag;

    /**
     * 活动名称(描述)
     */
    @Column(name = "action_name")
    private String actionName;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}