package com.gmsj.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmsj.base.CustomError;
import com.gmsj.base.YmlConfig;
import com.gmsj.common.constant.HttpHeaderConstant;
import com.gmsj.common.constant.SignInTypeEnum;
import com.gmsj.common.dto.BaseOutDTO;
import com.gmsj.common.exception.BaseRuntimeException;
import com.gmsj.common.model.UserInfo;
import com.gmsj.common.util.JwtUtil;
import com.gmsj.dao.ActionDao;
import com.gmsj.dao.AuthDao;
import com.gmsj.dao.RoleDao;
import com.gmsj.dao.UserDao;
import com.gmsj.model.bo.GetWxOpenIdBo;
import com.gmsj.model.bo.SignInSuccessBo;
import com.gmsj.model.po.ActionPo;
import com.gmsj.model.po.AuthPo;
import com.gmsj.model.po.RolePo;
import com.gmsj.model.po.UserPo;
import com.gmsj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author baojieren
 * @date 2020/4/16 13:46
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Resource
    ObjectMapper objectMapper;
    @Resource
    UserDao userDao;
    @Resource
    RoleDao roleDao;
    @Resource
    ActionDao actionDao;
    @Resource
    YmlConfig ymlConfig;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    RestTemplate restTemplate;
    @Resource
    AuthDao authDao;

    @Override
    @Transactional
    public BaseOutDTO getOpenId(String code) throws Exception {
        final String wxOpenIdUrl = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                ymlConfig.getAppId(),
                ymlConfig.getSecret(),
                code);

        GetWxOpenIdBo wxOpenIdBo;
        try {
            wxOpenIdBo = restTemplate.getForObject(wxOpenIdUrl, GetWxOpenIdBo.class);
        } catch (Exception e) {
            log.error("获取OpenId: 异常: {}", objectMapper.writeValueAsString(e));
            throw new BaseRuntimeException(new CustomError("获取OpenId异常"));
        }

        if (ObjectUtils.isEmpty(wxOpenIdBo) || StringUtils.isEmpty(wxOpenIdBo.getOpenid())) {
            log.error("获取OpenId: 失败");
            log.error("获取OpenId: 返回数据:{}", objectMapper.writeValueAsString(wxOpenIdBo));
            throw new BaseRuntimeException(new CustomError("获取OpenId异常"));
        }

        String openId = wxOpenIdBo.getOpenid();
        String sessionKey = wxOpenIdBo.getSession_key();
        Map<String, String> openIdMap = new HashMap<>();
        // 查询是否存在该用户,如果不存在，则创建
        AuthPo authPo = authDao.selectOneByAuthAccount(openId);
        if (null == authPo) {
            log.info("微信用户openId:{} 不存在，开始创建", openId);
            UserPo userPo = new UserPo();
            userPo.setOpenId(openId);
            userDao.insertSelective(userPo);

            AuthPo auth = new AuthPo();
            auth.setUserId(userPo.getId());
            auth.setAuthType(SignInTypeEnum.WX.key);
            auth.setAuthAccount(openId);
            auth.setValid(1);
            authDao.insertSelective(auth);
            openIdMap.put("userId", userPo.getId().toString());
        }

        openIdMap.put("openId", openId);
        openIdMap.put("sessionKey", sessionKey);
        BaseOutDTO outDTO = new BaseOutDTO();
        outDTO.setData(openIdMap);
        return outDTO;
    }

    @Override
    public BaseOutDTO wxMiniSignIn(String openId, String ip) {
        BaseOutDTO outDTO = new BaseOutDTO();
        AuthPo authPo = authDao.selectOneByAuthAccount(openId);
        if (ObjectUtils.isEmpty(authPo)) {
            return outDTO.fail("用户不存在,请先获取openId");
        }

        // 用户基本信息
        UserPo userPo = userDao.selectOneByOpenId(openId);
        if (ObjectUtils.isEmpty(userPo)) {
            return outDTO.fail("用户不存在,请先获取openId");
        }

        if (userPo.getValid() != 1) {
            return outDTO.fail("账号被冻结");
        }

        List<RolePo> rolePos = roleDao.selectRoleListByUserId(userPo.getId());
        SignInSuccessBo bo = new SignInSuccessBo();
        BeanUtils.copyProperties(userPo, bo);
        bo.setRoleList(rolePos.stream().map(RolePo::getRoleTag).collect(Collectors.toList()));

        // 生成token
        String token = JwtUtil.createToken(userPo.getId().toString(), bo.getRoleList().toArray(new String[0]), new String[0], ip, null);
        stringRedisTemplate.opsForValue().set("token:" + userPo.getId(), token, 30, TimeUnit.DAYS);
        bo.setToken(token);
        // 将用户基本信息存redis
        saveUserInfo2Redis(userPo);
        return new BaseOutDTO().setData(bo);
    }

    @Override
    @Transactional
    public BaseOutDTO mngSignUp(AuthPo authPo) {
        // log.info("手机号:{} 请求注册...", authsPo.getAuthAccount());
        // // 先查询是否已经注册过
        // UserPo oneByPhone = userDao.selectOneByPhone(authsPo.getAuthAccount());
        // if (ObjectUtils.isEmpty(oneByPhone)) {
        //     throw new BaseRuntimeException(new CustomError("手机号已被注册"));
        // }
        //
        // // 随机盐
        // String salt = RandomUtil.randomStr(5);
        // String p = userPo.getPassword() + salt;
        // userPo.setPassword(DigestUtils.md5DigestAsHex(p.getBytes()));
        // userPo.setSalt(salt);
        // userDao.insertSelective(userPo);
        //
        // // 后台用户注册需要默认添加后台用户角色
        // RolePo rolePo = roleDao.selectOneByRoleTag("mng");
        // if (ObjectUtils.isEmpty(rolePo)) {
        //     // 添加普通后台用户角色
        //     rolePo = new RolePo();
        //     rolePo.setRoleTag("mng");
        //     rolePo.setRoleName("普通后台用户");
        //     roleDao.insertSelective(rolePo);
        // }
        //
        // // 添加用户的后台用户角色
        // roleDao.insertUser2Role(userPo.getId(), rolePo.getId());
        return new BaseOutDTO();
    }

    @Override
    public BaseOutDTO mngSignIn(UserPo userPo, String ip) {
        // log.info("手机号:{} 请求登录...", userPo.getPhone());
        //
        // UserPo oneByPhone = userDao.selectOneByPhone(userPo.getPhone());
        // if (ObjectUtils.isEmpty(oneByPhone)) {
        //     throw new BaseRuntimeException(CustomError.USER_NOT_EXIST);
        // }
        //
        // // 对比密码
        // String psw = DigestUtils.md5DigestAsHex((userPo.getPassword() + oneByPhone.getSalt()).getBytes());
        // if (!oneByPhone.getPassword().equals(psw)) {
        //     throw new BaseRuntimeException(new CustomError("密码错误"));
        // }
        //
        // // 查询用户角色列表
        // Set<String> roleSet = new HashSet<>();
        // List<RolePo> rolePoList = roleDao.selectRoleListByUserId(oneByPhone.getId());
        // if (rolePoList != null && rolePoList.size() > 0) {
        //     roleSet = rolePoList.stream().map(RolePo::getRoleTag).collect(Collectors.toSet());
        // }
        //
        // // 查询用户行为列表
        // Set<String> actionSet = new HashSet<>();
        // List<ActionPo> actionPoList = actionDao.selectActionsByUserId(oneByPhone.getId());
        // if (actionPoList != null && actionPoList.size() > 0) {
        //     actionSet = actionPoList.stream().map(ActionPo::getActionTag).collect(Collectors.toSet());
        // }
        //
        // // 生成token
        // String token = JwtUtil.createToken(oneByPhone.getId().toString(), roleSet.toArray(new String[0]), actionSet.toArray(new String[0]), ip, oneByPhone.getSalt());
        // stringRedisTemplate.opsForValue().set("token:" + oneByPhone.getId(), token, 30, TimeUnit.DAYS);
        //
        // SignInBo signInBo = new SignInBo();
        // BeanUtils.copyProperties(oneByPhone, signInBo);
        // signInBo.setRoleList(roleSet);
        // signInBo.setActionList(actionSet);
        // signInBo.setToken(token);
        //
        // // 将用户基本信息存redis
        // saveUserInfo2Redis(oneByPhone);
        // return new BaseOutDTO().setData(signInBo);
        return new BaseOutDTO();
    }

    @Override
    public BaseOutDTO mngUpdate(UserPo userPo) {
        // todo
        log.info("");
        return null;
    }

    @Override
    public BaseOutDTO mngSignOut(String token) {
        String userId = JwtUtil.getUserId(token);
        stringRedisTemplate.delete("token:" + userId);
        log.info("用户id:{} 已经退出登录", userId);
        return new BaseOutDTO();
    }

    @Override
    public BaseOutDTO findOneByPhone(String phone) {
        BaseOutDTO outDTO = new BaseOutDTO();

        UserPo userPo = userDao.selectOneByPhone(phone);
        if (ObjectUtils.isEmpty(userPo)) {
            throw new BaseRuntimeException(CustomError.USER_NOT_EXIST);
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userPo, userInfo);
        return outDTO.setData(userInfo);
    }

    @Override
    public BaseOutDTO findOneByUserId(int userId) {
        BaseOutDTO outDTO = new BaseOutDTO();

        UserPo userPo = userDao.selectByPrimaryKey(userId);
        if (ObjectUtils.isEmpty(userPo)) {
            throw new BaseRuntimeException(CustomError.USER_NOT_EXIST);
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userPo, userInfo);
        return outDTO.setData(userInfo);
    }

    @Override
    public List<RolePo> findRoleListByUserId(int userId) {
        return roleDao.selectRoleListByUserId(userId);
    }

    @Override
    public List<ActionPo> findActionListByUserId(int userId) {
        return actionDao.selectActionsByUserId(userId);
    }

    /**
     * 将用户基本信息存redis
     */
    public void saveUserInfo2Redis(UserPo userPo) {
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userPo, userInfo);
        try {
            stringRedisTemplate.opsForValue().set(HttpHeaderConstant.HEADER_USER_INFO + ":" + userInfo.getId(), objectMapper.writeValueAsString(userInfo));
        } catch (JsonProcessingException e) {
            log.error("序列化 UserInfo 异常");
            e.printStackTrace();
        }
    }
}
