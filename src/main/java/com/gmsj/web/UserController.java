package com.gmsj.web;

import com.gmsj.base.CustomError;
import com.gmsj.common.constant.HttpHeaderConstant;
import com.gmsj.common.dto.BaseOutDTO;
import com.gmsj.common.exception.BaseRuntimeException;
import com.gmsj.common.util.CheckUtil;
import com.gmsj.model.po.AuthPo;
import com.gmsj.model.po.UserPo;
import com.gmsj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author baojieren
 * @date 2020/4/15 17:38
 */
@Slf4j
@RestController
public class UserController {

    @Resource
    UserService userService;

    /**
     * 小程序获取openId (小程序用户注册)
     */
    @GetMapping("/wx/openid")
    public BaseOutDTO getWxOpenId(String code) throws Exception {
        CheckUtil.isEmpty("微信授权码", code);
        return userService.getOpenId(code);
    }

    /**
     * 小程序登录
     */
    @GetMapping("/wx/signin")
    public BaseOutDTO wxSignIn(String openId, HttpServletRequest request) {
        CheckUtil.isEmpty("openId", openId);
        return userService.wxMiniSignIn(openId, request.getHeader(HttpHeaderConstant.HEADER_IP));
    }

    /**
     * 后台注册
     */
    @PostMapping("/mng/signup")
    public BaseOutDTO mngSignUp(@RequestBody AuthPo authPo) {
        // CheckUtil.isEmpty("登录类型", authsPo.getAuthType());
        // CheckUtil.isEmpty("手机号", authsPo.getAuthAccount());
        // CheckUtil.isEmpty("密码", authsPo.getAuthToken());
        // return userService.mngSignUp(userPo);
        return null;
    }

    /**
     * 后台登录
     */
    @PostMapping("/mng/signin")
    public BaseOutDTO mngSignIn(@RequestBody UserPo userPo, HttpServletRequest request) {
        // CheckUtil.isEmpty("手机号", userPo.getPhone());
        // CheckUtil.isEmpty("密码", userPo.getPassword());
        // return userService.mngSignIn(userPo, request.getHeader("ip"));
        return null;
    }

    /**
     * 后台修改资料
     */
    @PostMapping("/mng/update")
    public BaseOutDTO mngUpdateInfo(@RequestBody UserPo userPo) {
        return null;
    }

    /**
     * 后台登出
     */
    @GetMapping("/mng/signout")
    public BaseOutDTO mngSignOut(HttpServletRequest request) {
        String auth = request.getHeader(HttpHeaderConstant.HEADER_AUTH);
        if (StringUtils.isEmpty(auth) || auth.split(" ").length < 2) {
            throw new BaseRuntimeException(CustomError.AUTH_FAILED);
        }
        return userService.mngSignOut(auth.split(" ")[1]);
    }

    /**
     * 用phone查询用户信息
     */
    @GetMapping("/userInfoByPhone")
    public BaseOutDTO findUserInfoByPhone(String phone) {
        CheckUtil.isEmpty("phone", phone);
        return userService.findOneByPhone(phone);
    }

    /**
     * 用userId查询用户信息
     */
    @GetMapping("/userInfoByUserId")
    public BaseOutDTO findUserInfoByUserId(Integer userId) {
        CheckUtil.isEmpty("userId", userId);
        return userService.findOneByUserId(userId);
    }

    @GetMapping("/test")
    public String hello() {
        return "请求了test接口";
    }
}
