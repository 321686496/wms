package com.hongan.template.wms.service.impl;

import ch.qos.logback.core.util.ExecutorServiceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hongan.template.base.entity.BaseError;
import com.hongan.template.base.entity.BaseException;
import com.hongan.template.base.utils.DataCheckUtils;
import com.hongan.template.base.utils.RandomUtils;
import com.hongan.template.wms.entity.HonganWmsMaterial;
import com.hongan.template.wms.entity.HonganWmsMaterialType;
import com.hongan.template.wms.mapper.HonganWmsMaterialMapper;
import com.hongan.template.wms.mapper.HonganWmsMaterialTypeMapper;
import com.hongan.template.wms.query.QueryMaterial;
import com.hongan.template.wms.service.IHonganWmsMaterialService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hongan.template.wms.vo.HonganWmsMaterialVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.toolkit.Db;

/**
 * <p>
 * 物料表 服务实现类
 * </p>
 *
 * @author Administrator
 * @since 2024-06-25
 */
@Service
public class HonganWmsMaterialServiceImpl extends ServiceImpl<HonganWmsMaterialMapper, HonganWmsMaterial> implements IHonganWmsMaterialService {

    @Autowired
    private HonganWmsMaterialTypeMapper materialTypeMapper;


    public HonganWmsMaterialVO buildVO(HonganWmsMaterial honganWmsMaterial){
        HonganWmsMaterialVO vo = new HonganWmsMaterialVO();
        BeanUtils.copyProperties(honganWmsMaterial, vo);
//        HonganWmsMaterialType type = Db.getById(honganWmsMaterial.getTypeId(), HonganWmsMaterialType.class);
        HonganWmsMaterialType honganWmsMaterialType = materialTypeMapper.selectById(honganWmsMaterial.getTypeId());
        vo.setTypeName(honganWmsMaterialType.getClassifyName());
        vo.setTypeNameFr(honganWmsMaterialType.getClassifyNameFr());
        return vo;
    }

    public QueryWrapper<HonganWmsMaterial> buildWrapper(QueryMaterial queryMaterial){
        QueryWrapper<HonganWmsMaterial> wrapper = queryMaterial.toWrapper();
        if(StringUtils.isNotEmpty(queryMaterial.getTypeName())){
            List<HonganWmsMaterialType> honganWmsMaterialTypes = materialTypeMapper.selectList(new LambdaQueryWrapper<HonganWmsMaterialType>()
                    .like(HonganWmsMaterialType::getClassifyName, queryMaterial.getTypeName())
            );
            List<Long> collect = honganWmsMaterialTypes.stream().map(HonganWmsMaterialType::getId).collect(Collectors.toList());
            collect.add(-1l);
            wrapper.in("type_id", collect);
        }
        return wrapper;
    }

    @Override
    public IPage<HonganWmsMaterial> queryPage(QueryMaterial query) {
        IPage<HonganWmsMaterial> page = new Page<>(query.getCurrent(), query.getPageSize());
        return baseMapper.selectPage(page, buildWrapper(query));
    }

    @Override
    public List<HonganWmsMaterial> queryList(QueryMaterial query) {
        return baseMapper.selectList(buildWrapper(query));
    }

    @Override
    public IPage<HonganWmsMaterialVO> queryPageVO(QueryMaterial query) {
        Page<HonganWmsMaterialVO> page = new Page<>(query.getCurrent(), query.getPageSize());
        IPage<HonganWmsMaterial> honganWmsMaterialIPage = queryPage(query);
        BeanUtils.copyProperties(honganWmsMaterialIPage, page);
        page.setRecords(honganWmsMaterialIPage.getRecords().stream().map(this::buildVO).collect(Collectors.toList()));
        return page;
    }

    @Override
    public List<HonganWmsMaterialVO> queryListVO(QueryMaterial query) {
        return queryList(query).stream().map(this::buildVO).collect(Collectors.toList());
    }

    @Override
    public HonganWmsMaterial selectById(Long id) throws BaseException {
        if (null == id) throw new BaseException(BaseError.BadRequest);
        HonganWmsMaterial data = baseMapper.selectById(id);
        if (data == null) throw new BaseException(BaseError.DataNotExist);
        return data;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void create(HonganWmsMaterial data) throws BaseException {
        checkData(data);
        //查询名称是否重复
        Long count = baseMapper.selectCount(new QueryWrapper<HonganWmsMaterial>()
                .eq("name", data.getName()));
        if (count > 0) {
            throw new BaseException(BaseError.NameRepeat);
        }
        //如果填写了编码 检查编码是否重复
        if (StringUtils.isNotEmpty(data.getCode())) {
            Long codeCount = baseMapper.selectCount(new QueryWrapper<HonganWmsMaterial>()
                    .eq("code", data.getCode()));
            if (codeCount > 0) {
                throw new BaseException(BaseError.CodeRepeat);
            }
        } else {
            //系统生产一个随机编码
            String code = RandomUtils.randomNumber(6);
            while (baseMapper.selectCount(new QueryWrapper<HonganWmsMaterial>()
                    .eq("code", code)) != 0) {
                code = RandomUtils.randomNumber(6);
            }
            data.setCode(code);
        }
        // 检查该类别是否存在
        HonganWmsMaterialType honganWmsMaterialType = materialTypeMapper.selectById(data.getTypeId());
        if(Objects.isNull(honganWmsMaterialType))throw new BaseException(BaseError.MaterialTypeError);
        data.insert();
    }

    private void checkData(HonganWmsMaterial data) throws BaseException {
        DataCheckUtils.checkParam(data.getName(), BaseError.PleaseInputName);
        if (StringUtils.isNotEmpty(data.getName())) data.setName(data.getName().trim());
        if (StringUtils.isNotEmpty(data.getCode())) data.setCode(data.getCode().trim());
        if (data.getPriority() == null) data.setPriority(0);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void update(HonganWmsMaterial data) throws BaseException {
        checkData(data);
        HonganWmsMaterial material = selectById(data.getId());
        //查询名称是否重复
        Long count = baseMapper.selectCount(new QueryWrapper<HonganWmsMaterial>()
                .ne("id", data.getId())
                .eq("name", data.getName()));
        if (count > 0) {
            throw new BaseException(BaseError.NameRepeat);
        }
        //如果填写了编码 检查编码是否重复
        if (StringUtils.isNotEmpty(data.getCode())) {
            Long codeCount = baseMapper.selectCount(new QueryWrapper<HonganWmsMaterial>()
                    .ne("id", data.getId())
                    .eq("code", data.getCode()));
            if (codeCount > 0) {
                throw new BaseException(BaseError.CodeRepeat);
            }
        } else {
            //系统生产一个随机编码
            String code = RandomUtils.randomNumber(6);
            while (baseMapper.selectCount(new QueryWrapper<HonganWmsMaterial>()
                    .eq("code", code)) != 0) {
                code = RandomUtils.randomNumber(6);
            }
            data.setCode(code);
        }
        // 检查该类别是否存在
        HonganWmsMaterialType honganWmsMaterialType = materialTypeMapper.selectById(data.getTypeId());
        if(Objects.isNull(honganWmsMaterialType))throw new BaseException(BaseError.MaterialTypeError);
        data.updateById();
    }

    @Override
    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
    public void delete(Long id) {
        baseMapper.deleteById(id);
    }
}
