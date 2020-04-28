package com.gmsj.base;

import com.gmsj.common.exception.BaseError;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 自定义异常处理
 *
 * @author renbaojie
 */
@Accessors(chain = true)
@NoArgsConstructor
public class CustomError extends BaseError {
    protected int code;
    protected String msg;

    public static int BIZ_ERR_CODE_START = 50000;

    public static final BaseError AUTH_FAIL = new CustomError(403, "权限不足");
    public static final BaseError USER_NOT_EXIST = new CustomError(500, "用户不存在");

    public CustomError(String msg) {
        super(msg);
    }

    public CustomError(int code, String msg) {
        super(code, msg);
    }
}
