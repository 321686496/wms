package com.hongan.template.admin.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.enums.AdminStatus;
import com.hongan.template.admin.entity.HonganAdmin;
import com.hongan.template.base.entity.BaseQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Created by zyp on 2021/9/27 0027.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryAdmin extends BaseQuery<HonganAdmin> {
    private Long roleId;
    private String roleKey;
    private String realName;
    private AdminStatus status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate salaryDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    @Override
    public QueryWrapper<HonganAdmin> toWrapper() {
//        if (salaryDate != null) {
//            start = salaryDate.withDayOfMonth(1);
//            end = salaryDate.withDayOfMonth(salaryDate.lengthOfMonth());
//        }
        return super.toWrapper()
                .eq(notEmpty(roleId), "role_id", roleId)
                ;
    }

}
