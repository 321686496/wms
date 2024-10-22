package com.hongan.template.base.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: Response
 */

@Data
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = -1592093481786945071L;

    private static final int SUCCESS_STATUS = 0;
    private static final int ERROR_STATUS = 500;

    private String message;
    private String remark;
    private int status;
    private Object data;
    private Pagination pagination;
    private Object additional;//附加属性

    public BaseResponse exception(int status, String message) {
        this.status = status;
        this.message = message;
        return this;
    }

    public BaseResponse exception(int status, String message, String remark) {
        this.status = status;
        this.message = message;
        this.remark = remark;
        return this;
    }

    public BaseResponse error() {
        this.status(ERROR_STATUS);
        this.message("服务器错误");
        return this;
    }

    public BaseResponse error(int status, String message) {
        this.status = status;
        this.message = message;
        return this;
    }
    public BaseResponse error(int status, String message, String remark) {
        this.status = status;
        this.message = message;
        this.remark = remark;
        return this;
    }

    public BaseResponse error(Exception e) {
        return error(BaseException.fromException(e));
    }

    public BaseResponse error(BaseError e) {
        return error(e.getStatus(), e.getMessage(), e.getRemark());
    }

    public BaseResponse error(BaseException e) {
        return error(e.getStatus(), e.getMessage());
    }

    public BaseResponse success() {
        this.status(SUCCESS_STATUS);
        return this;
    }

    public BaseResponse success(Object data) {
        this.status(SUCCESS_STATUS);
        this.data = data;
        return this;
    }

    public BaseResponse pagination(Long total) {
        Pagination pagination = new Pagination(1L, 10L, total);
        return pagination(pagination);
    }

    public BaseResponse pagination(Pagination pagination) {
        this.pagination = pagination;
        return this;
    }

    public BaseResponse status(int status) {
        this.status = status;
        return this;
    }

    public BaseResponse data(Object data) {
        this.data = data;
        return this;
    }

    public BaseResponse message(String message) {
        this.message = message;
        return this;
    }

    public BaseResponse setAdditional(Object additional) {
        this.additional = additional;
        return this;
    }

}
