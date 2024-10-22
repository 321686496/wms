package com.hongan.template.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hongan.template.admin.query.QueryAdmin;
import com.hongan.template.admin.entity.HonganAdmin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author zhangxd
 * @since 2020-02-24
 */
public interface HonganAdminMapper extends BaseMapper<HonganAdmin> {

    HonganAdmin selectByIdXml(@Param("id")  Long id);

    List<HonganAdmin> selectAdminList(@Param("query") QueryAdmin query,
                                      @Param("pageNum") Long pageNum,
                                      @Param("pageSize") Long pageSize);

    Integer selectAdminListCount(@Param("query") QueryAdmin query);

}
