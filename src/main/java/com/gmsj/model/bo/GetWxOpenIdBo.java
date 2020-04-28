package com.gmsj.model.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetWxOpenIdBo implements Serializable {
    public int errcode;
    public String errmsg;
    public String openid;
    public String session_key;
}
