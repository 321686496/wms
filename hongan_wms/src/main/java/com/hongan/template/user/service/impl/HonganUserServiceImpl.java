package com.hongan.template.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.service.IRedisValue;
import com.hongan.template.base.utils.BaseCrypto;
import com.hongan.template.base.utils.RandomUtils;
import com.hongan.template.system.entity.HonganSystemConfig;
import com.hongan.template.system.service.IHonganSystemConfigService;
import com.hongan.template.user.entity.HonganUser;
import com.hongan.template.user.mapper.HonganUserMapper;
import com.hongan.template.user.query.QueryUser;
import com.hongan.template.user.service.IHonganUserService;
import com.hongan.template.enums.AdminStatus;
import com.hongan.template.external.service.IExternalWechatAppletService;
import com.hongan.template.external.vo.WechatPhoneEnData;
import com.hongan.template.external.vo.WechatSessionKeyResult;
import com.hongan.template.external.vo.WechatVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author user
 * @since 2020-07-30
 */
@Service
@Slf4j
@CacheConfig(cacheNames = "CACHE:CUSTOMER:USER")
public class HonganUserServiceImpl extends ServiceImpl<HonganUserMapper, HonganUser> implements IHonganUserService {

    @Autowired
    IRedisValue<Object> redisValue;
    @Autowired
    private IHonganSystemConfigService systemConfigService;
    @Autowired
    private IExternalWechatAppletService wechatAppletService;

    @Override
    public HonganUser selectById(Long id, String... keys) throws BaseException {
        if (keys.length == 0) keys = new String[]{"*"};
        if (id == null) throw new BaseException(BaseError.BadRequest);
        HonganUser user = baseMapper.selectOne(new QueryWrapper<HonganUser>().select(keys).eq("id", id));
        if (user == null) throw new BaseException("用户信息不存在！");
        return user;
    }


    @Override
    public HonganUser getByPhone(String phone, String... keys) {
        if (keys.length == 0) keys = new String[]{"*"};
        return baseMapper.selectOne(new QueryWrapper<HonganUser>().select(keys).eq("phone", phone));
    }

    @Override
    public HonganUser getByName(String name, String... keys) {
        if (keys.length == 0) keys = new String[]{"*"};
        return baseMapper.selectOne(new QueryWrapper<HonganUser>().select(keys).eq("name", name));
    }

    @Override
    public HonganUser getByWxOpenId(String wxOpenId, String... keys) {
        if (keys.length == 0) keys = new String[]{"*"};
        return baseMapper.selectOne(new QueryWrapper<HonganUser>().select(keys).eq("wx_open_id", wxOpenId));
    }

    @Override
    public HonganUser getDetailById(Long id, String... keys) throws BaseException {
        if (keys == null || keys.length < 1) keys = new String[]{"*"};
        if (id == null) throw new BaseException(BaseError.BadRequest);
        HonganUser user = baseMapper.selectOne(new QueryWrapper<HonganUser>().eq("id", id).select(keys));
        return user;
    }

    @Override
    public HonganUser getShowUserMessage(Long userId) {
        HonganUser data = baseMapper.selectOne(new QueryWrapper<HonganUser>()
                .select("id", "nickname", "phone", "avatar").eq("id", userId));
        if (data == null) {
            data = new HonganUser();
            data.setNickname("该用户已注销");
            data.setPhone("");
            data.setAvatar("");
        }
        return data;
    }

    @Override
    public IPage<HonganUser> queryPage(QueryUser query) {
        Page<HonganUser> page = new Page<>(query.getCurrent(), query.getPageSize());
        List<HonganUser> list = baseMapper.selectPage(page, query.toWrapper()).getRecords();
        return page;
    }


    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public void userUpdateMessage(HonganUser user) throws BaseException {
        user.setStatus(null);
        user.updateById();
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public void updateUserStatus(Long id) throws BaseException {
        HonganUser user = selectById(id);
        if (user == null) throw new BaseException(BaseError.BadRequest);
        HonganUser data = new HonganUser();
        data.setId(user.getId());
        data.setStatus(user.getStatus() == AdminStatus.LOCK ? AdminStatus.NORMAL : AdminStatus.LOCK);
        data.updateById();
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    public HonganUser wechatLogin(WechatVo wechat) throws BaseException {
        log.info("用户授权信息:{}", wechat.getUserInfo().toString());

        Map<String, String> testData = new HashMap<>() {{
            put("test", "test-open-id-0000");
            put("test1", "test-open-id-0001");
            put("test2", "test-open-id-0002");
            put("test3", "test-open-id-0003");
            put("test4", "test-open-id-0004");
            put("test5", "test-open-id-0005");
        }};
        String openId = "";
        if (!testData.containsKey(wechat.getCode())) {
            WechatSessionKeyResult result = wechatAppletService.getSessionKeyAndOpenIdByCode(wechat.getCode());
            openId = result.getOpenId();
        } else {
            openId = testData.get(wechat.getCode());
        }
        //根据openId查询用户是否存在
        HonganUser user = getByWxOpenId(openId);
        if (user == null) {
            //不存在则新增
            user = new HonganUser();
            user.setName(openId);
            user.setPhone("");
            user.setWxOpenId(openId);
            user.setCryptPassword(BaseCrypto.hmac(user.getWxOpenId()));
            user.setStatus(AdminStatus.NORMAL);
            HonganSystemConfig setting = systemConfigService.getConfig();
            user.setNickname(setting.getUserDefaultNicknamePrefix() + RandomUtils.randomNumber(6));
            user.setAvatar(setting.getUserDefaultAvatar());
            user.setPid(0L);
            //生成邀请码
            String flag = RandomUtils.getRandomChar(8);
            while (baseMapper.selectCount(new QueryWrapper<HonganUser>()
                    .eq("invitation_code", flag)) != 0) {
                flag = RandomUtils.getRandomChar(8);
            }
            user.setInvitationCode(flag.toUpperCase());
            if (StringUtils.isNotEmpty(wechat.getInvitationCode())) {
                HonganUser invitationUser = baseMapper.selectOne(new QueryWrapper<HonganUser>()
                        .eq("invitation_code", wechat.getInvitationCode().trim()));
                if (invitationUser != null) {
                    user.setPid(invitationUser.getId());
                }
            }
            user.insert();
        }
        if (user.getStatus() != AdminStatus.NORMAL) throw new BaseException(20088, "该账户已被禁用！");
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    @CacheEvict(key = "#name")
    public HonganUser updateWechatPhone(String name, WechatPhoneEnData en) throws BaseException, IOException, InterruptedException {
        if (StringUtils.isEmpty(en.getCode())) throw new BaseException(BaseError.BadRequest);
        HonganUser user = getByName(name);
        //换取用户手机号
        String phone = "";
        if (en.getCode().equals("test")) {
            phone = "16666666666";
        } else {
            phone = wechatAppletService.getUserPhoneNumber(en.getCode());
        }
        // 更新用户手机, 必须调用service保证缓存刷新
        user.setPhone(phone);
        user.updateById();
        return user;
    }
}
