//package com.ponlay.template.base.entity;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.github.yulichang.wrapper.MPJLambdaWrapper;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.experimental.Accessors;
//
//import java.util.List;
//
///**
// * @Author: zhangxd
// * @Version: v1.0
// * @CreateTime: 2019-11-12 11:15
// * @Description: BaseQuery
// */
//
//@Data
//@EqualsAndHashCode(callSuper = true)
//@Accessors(chain = true)
//public class BaseMPJQuery<T> extends Pagination {
//
//    private static final long serialVersionUID = -3949195765660511911L;
//
//    private String search;
//    private Long[] idList;
//    private Long id;
//    private Long userId;
//
//    public MPJLambdaWrapper<T> toWrapper() {
//        return new MPJLambdaWrapper<T>();
//    }
//
//    protected static Boolean notEmpty(final String s) {
//        return s != null && !s.isEmpty();
//    }
//
//    protected static Boolean notEmpty(final Integer l) {
//        return l != null && l > 0;
//    }
//
//    protected static Boolean notEmpty(final Long l) {
//        return l != null && l > 0;
//    }
//
//    protected static Boolean notEmpty(final Double d) {
//        return d != null && d > 0;
//    }
//
//    protected static Boolean notEmpty(final Long[] l) {
//        return l != null && l.length > 0;
//    }
//
//    protected static Boolean notEmpty(final String[] l) {
//        return l != null && l.length > 0;
//    }
//
//    protected static Boolean notNull(final Integer l) {
//        return l != null;
//    }
//
//    protected static Boolean notNull(final Object l) {
//        return l != null;
//    }
//
//    private static Boolean listNotNull(final List<Object> list) {
//        return list != null && list.size() > 0;
//    }
//}
