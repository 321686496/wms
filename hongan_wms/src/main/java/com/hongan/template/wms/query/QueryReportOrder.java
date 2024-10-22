package com.hongan.template.wms.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseQuery;
import com.hongan.template.wms.entity.HonganWmsReportOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryReportOrder extends BaseQuery<HonganWmsReportOrder> {
    private Boolean isMerge;
    private String billCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    @Override
    public QueryWrapper<HonganWmsReportOrder> toWrapper() {
        return super.toWrapper()
                .eq(isMerge != null, "is_merge", isMerge)
                .like(notEmpty(billCode), "bill_code", billCode)
                .ge(start != null, "bill_date", start)
                .le(end != null, "bill_date", end)
                .orderByDesc("id");
    }
}
