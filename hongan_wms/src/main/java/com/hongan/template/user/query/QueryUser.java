package com.hongan.template.user.query;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hongan.template.base.entity.BaseQuery;
import com.hongan.template.enums.AdminStatus;
import com.hongan.template.user.entity.HonganUser;
import lombok.Data;

@Data
public class QueryUser extends BaseQuery<HonganUser> {
    private String nickname;
    private AdminStatus status;

    @Override
    public QueryWrapper<HonganUser> toWrapper() {
        return super.toWrapper()
                .like(notEmpty(nickname), "nickname", nickname)
                .eq(status != null, "status", status)
                .orderByDesc("created_at")
                ;
    }
}
