package com.hongan.template.base.entity;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-11-12 11:15
 * @Description: BaseQuery
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BaseQuery<T> extends Pagination {

    private static final long serialVersionUID = -3949195765660511911L;

    @ApiModelProperty(value = "公用检索", hidden = true)
    private String search;
    @ApiModelProperty(hidden = true)
    private List<Long> idList;

    @ApiModelProperty(hidden = true)
    private List<Long> idList2;

    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(hidden = true)
    private String[] columns;
//    private String sort;

    @ApiModelProperty(hidden = true)
    private String searchKey;

    //公共筛选参数
    @ApiModelProperty(value = "用户Id", hidden = true)
    private Long userId;
    @ApiModelProperty(value = "pid", hidden = true)
    private Long pid;

    @ApiModelProperty(value = "启用状态", hidden = true)
    private Boolean enable;
    @ApiModelProperty(value = "编码", hidden = true)
    private String code;
    @ApiModelProperty(value = "编码(全相等)", hidden = true)
    private String equalCode;
    @ApiModelProperty(value = "规格", hidden = true)
    private String spec;
    @ApiModelProperty(value = "多个规格(且)", hidden = true)
    private List<String> andSpec;
    @ApiModelProperty(value = "多个规格(或)", hidden = true)
    private List<String> orSpec;
    @ApiModelProperty(value = "名称", hidden = true)
    private String name;
    @ApiModelProperty(value = "手机号", hidden = true)
    private String phone;
    @ApiModelProperty(value = "邮箱", hidden = true)
    private String email;
    @ApiModelProperty(value = "地址", hidden = true)
    private String address;
    @ApiModelProperty(value = "备注", hidden = true)
    private String remark;


    @ApiModelProperty(value = "分类ID", hidden = true)
    private Long categoryId;
    @ApiModelProperty(value = "分类ID列表", hidden = true)
    private List<Long> categoryIds;
    @ApiModelProperty(value = "分类名称", hidden = true)
    private String categoryName;


    //检索条件参数 如部门数据过滤
    @ApiModelProperty(value = "数据权限过滤(仅自己)", hidden = true)
    private Long createAdminId;
    @ApiModelProperty(value = "数据权限过滤(本部门数据/自定义数据)", hidden = true)
    private List<Long> deptIds;
    @ApiModelProperty(value = "", hidden = true)
    private Long deptId;
    @ApiModelProperty(value = "", hidden = true)
    private List<Long> dataScopeDeptIds;
    @ApiModelProperty(value = "仓库数据权限过滤", hidden = true)
    private List<Long> stockIds;
    @ApiModelProperty(value = "", hidden = true)
    private List<Long> dataScopeStockIds;

    public Long adminId;
    public Long curDeptId;
    public String role;

    public QueryWrapper<T> toWrapper() {
        QueryWrapper<T> query = new QueryWrapper<T>();
        if (notEmpty(columns)) {
            query = query.select(this.columns);
        }
        if (StringUtils.isNotEmpty(this.getSpec())) {
            if (spec.contains("|")) {
                orSpec = Arrays.asList(spec.split("\\|"));
                spec = "";
            }
            if (spec.contains("&")) {
                andSpec = Arrays.asList(spec.split("&"));
                spec = "";
            }
        }
        if (andSpec != null && andSpec.size() > 0) {
            query.and(wrapper -> {
                andSpec.forEach(value -> {
                    wrapper.like("spec", value);
                });
            });
        }
        if (orSpec != null && orSpec.size() > 0) {
            query.and(wrapper -> {
                orSpec.forEach(value -> {
                    wrapper.or().like("spec", value);
                });
            });
        }


//        if (notEmpty(sort)) {
//            if (sort.startsWith("-")) {
//                query = query.orderByDesc(sort.substring(1));
//            } else {
//                query = query.orderByAsc(sort);
//            }
//        }
        return query
                .eq(notEmpty(id), "id", id)
                .eq(notNull(pid), "pid", pid)
                .eq(notEmpty(userId), "user_id", userId)
                .eq(enable != null, "enable", enable)
                .like(notEmpty(code), "code", code)
                .eq(notEmpty(equalCode), "code", equalCode)
                .like(notEmpty(spec), "spec", spec)
                .like(notEmpty(name), "name", name)
                .like(notEmpty(phone), "phone", phone)
                .like(notEmpty(email), "email", email)
                .like(notEmpty(address), "address", address)
                .like(notEmpty(remark), "remark", remark)

                .eq(notEmpty(categoryId), "category_id", categoryId)
                .in(categoryIds != null && categoryIds.size() > 0, "category_id", categoryIds)
                .like(notEmpty(categoryName), "category_name", categoryName)
                .in(idList != null && idList.size() > 0, "id", idList)
                .in(idList2 != null && idList2.size() > 0, "id", idList2)
                .eq(notEmpty(createAdminId), "create_admin_id", createAdminId)
                .in(deptIds != null && deptIds.size() > 0, "dept_id", deptIds)
                .eq(notEmpty(deptId), "dept_id", deptId)
                .in(dataScopeDeptIds != null && dataScopeDeptIds.size() > 0, "dept_id", dataScopeDeptIds)
                .in(stockIds != null && stockIds.size() > 0, "stock_id", stockIds)
                .in(dataScopeStockIds != null && dataScopeStockIds.size() > 0, "stock_id", dataScopeStockIds)
                .like(notEmpty(search), notEmpty(searchKey) ? searchKey : "name", search);
    }

    public void setSelect(final String... columns) {
        this.columns = columns;
    }

    public void clearSelect() {
        this.columns = null;
    }

    protected static Boolean notEmpty(final String s) {
        return s != null && !s.isEmpty();
    }

    protected static Boolean notEmpty(final Integer l) {
        return l != null && l > 0;
    }

    protected static Boolean notEmpty(final Long l) {
        return l != null && l > 0;
    }

    protected static Boolean notEmpty(final Double d) {
        return d != null && d > 0;
    }

    protected static Boolean notEmpty(final Long[] l) {
        return l != null && l.length > 0;
    }

    protected static Boolean notEmpty(final String[] l) {
        return l != null && l.length > 0;
    }

    protected static Boolean notNull(final Integer l) {
        return l != null;
    }

    protected static Boolean notNull(final Object l) {
        return l != null;
    }

    private static Boolean listNotNull(final List<Object> list) {
        return list != null && list.size() > 0;
    }

}
