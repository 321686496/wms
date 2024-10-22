package com.hongan.template.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-11-12 11:17
 * @Description: Pagination
 */

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Pagination implements Serializable {

    private static final long serialVersionUID = 6558220848354251378L;

	public static final Long MAXPAGESIZE = 1000L;
    private static final Long CURRNET = 1L;
    private static final Long PAGESIZE = 10L;
    private static final Long TOTAL = 0L;
    private Long current;
    private Long pageSize;
    private Long total;

    public Pagination() {
        this.current = CURRNET;
        this.pageSize = PAGESIZE;
        this.total = TOTAL;
    }

    public Pagination setCurrent(Long current) {
        if (current < 1 ) current = 1L;
        this.current = current;
        return this;
    }

    public Pagination setPageSize(Long pageSize) {
        if (pageSize > MAXPAGESIZE) {
            pageSize = MAXPAGESIZE;
        } else if (pageSize < 1) {
            pageSize = 10L;
        }
        this.pageSize = pageSize;
        return this;
    }

}
