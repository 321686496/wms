package com.hongan.template.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.admin.query.QueryAdmin;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.vo.TreeDataVo;
import com.hongan.template.admin.entity.HonganAdmin;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhangxd
 * @since 2019-12-30
 */
public interface IHonganAdminService extends IService<HonganAdmin> {

    //分页查询
    IPage<HonganAdmin> queryPage(QueryAdmin query) throws BaseException;

    List<TreeDataVo> querySelectList(QueryAdmin query) throws BaseException;

    //查询详情
    HonganAdmin getById(Long id, String... keys) throws BaseException;

    HonganAdmin getDetailById(Long id, String... keys) throws BaseException;

    HonganAdmin getShouAdminDataById(Long id, String... keys) throws BaseException;

    HonganAdmin getByIdXml(Long id) throws BaseException;

    //新增账号
    String insert(HonganAdmin data) throws Exception;

    //修改账号
    void update(HonganAdmin data) throws BaseException;

    //重置密码
    String resetPassword(HonganAdmin admin) throws BaseException;

    //修改员工状态
    HonganAdmin updateStatus(Long id) throws BaseException;

    //校验密码
    HonganAdmin checkPassword(String name, String password) throws BaseException;

    //根据手机号登陆
    HonganAdmin loginByPhone(String phone) throws BaseException;

    //删除账号
    void delete( Long id) throws Exception;

    Boolean updateAdmin(HonganAdmin honganAdmin);

    void uploadLoginInfo(Long id, String ipAddr);

    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, readOnly = false, rollbackFor = Exception.class)
    String insertOrUpdate(HonganAdmin honganAdmin) throws Exception;


}
