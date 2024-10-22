package com.hongan.template.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hongan.template.admin.service.IHonganAdminService;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.system.entity.HonganSystemMessage;
import com.hongan.template.system.mapper.HonganSystemMessageMapper;
import com.hongan.template.system.query.QueryMessage;
import com.hongan.template.system.service.IHonganSystemMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统消息表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2024-05-21
 */
@Service
public class HonganSystemMessageServiceImpl extends ServiceImpl<HonganSystemMessageMapper, HonganSystemMessage> implements IHonganSystemMessageService {
    @Autowired
    private IHonganAdminService adminService;

    @Override
    public IPage<HonganSystemMessage> queryPage(QueryMessage query) throws BaseException {
        IPage<HonganSystemMessage> page = new Page<>(query.getCurrent(), query.getPageSize());
        List<HonganSystemMessage> records = baseMapper.selectPage(page, query.toWrapper()).getRecords();
        for (HonganSystemMessage record : records) {
            record.setUser(adminService.getShouAdminDataById(record.getUserId(), "id", "nickname", "avatar", "phone"));
        }
        return page;
    }

    @Override
    public List<HonganSystemMessage> queryList(QueryMessage query) throws BaseException {
        List<HonganSystemMessage> records = baseMapper.selectList(query.toWrapper());
        for (HonganSystemMessage record : records) {
            record.setUser(adminService.getShouAdminDataById(record.getUserId(), "id", "nickname", "avatar", "phone"));
        }
        return records;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void saveMessage(HonganSystemMessage data) {
        data.insert();
    }
}
