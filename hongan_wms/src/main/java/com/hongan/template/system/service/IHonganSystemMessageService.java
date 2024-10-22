package com.hongan.template.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.system.entity.HonganSystemMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.system.query.QueryMessage;

import java.util.List;

/**
 * <p>
 * 系统消息表 服务类
 * </p>
 *
 * @author Administrator
 * @since 2024-05-21
 */
public interface IHonganSystemMessageService extends IService<HonganSystemMessage> {
    IPage<HonganSystemMessage> queryPage(QueryMessage query) throws BaseException;

    List<HonganSystemMessage> queryList(QueryMessage query) throws BaseException;

    void saveMessage(HonganSystemMessage data);

}
