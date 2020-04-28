package com.gmsj.model.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author baojieren
 * @date 2020/4/16 18:23
 */
@Data
public class UrlAuthBo implements Serializable {
    public String url;
    public String roles;
    public String actions;
}
