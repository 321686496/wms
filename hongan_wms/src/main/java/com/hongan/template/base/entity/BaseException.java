package com.hongan.template.base.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.exceptions.PersistenceException;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Author: zhangxd
 * @Version: v1.0
 * @CreateTime: 2019-12-28 09:24
 * @Description: BaseException
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends Exception {
    private static final long serialVersionUID = 9079972344123939007L;
    /**
     * 异常码
     */
    private Integer status;
    //附加
    private String additional = "";
    private String remark = "";

    public BaseException(BaseError error) {
        super(error.getMessage());
        this.status = error.getStatus();
        this.remark = error.getRemark();
    }

    public BaseException(BaseError error, String additional) {
        super(error.getMessage());
        this.status = error.getStatus();
        this.additional = additional;
        this.remark = error.getRemark();
    }

//    public BaseException(BaseError error, String detail) {
//        super(error.getMessage() + ":" + detail);
//        this.status = error.getStatus();
//    }

    public BaseException(Integer code, String message) {
        super(message);
        this.status = code;
        this.remark = message;
    }

    public BaseException(String message) {
        super(message);
        this.status = 20088;
        this.remark = message;
    }

    public String toJSON() {
        return String.format("{\"status\":%d,\"message\":\"%s\",\"remark\":\"%s\",\"additional\":\"%s\"}", status, getMessage(), getRemark(), getAdditional());
    }

    public static BaseException fromException(Throwable e) {
        if (e == null) return new BaseException(BaseError.ServerError);
        Throwable cause = e.getCause();
        cause = cause == null ? e : cause;
        String message = cause.getMessage();
        if (message == null) return new BaseException(BaseError.ServerError);
        if (e instanceof BaseException) {
            return (BaseException) e;
        } else if (cause instanceof BaseException) {
            return (BaseException) cause;
        } else if (cause instanceof PersistenceException || cause instanceof SQLIntegrityConstraintViolationException) {
            if (message.contains("Duplicate")) {
                BaseError error;
                int duplicate = message.indexOf("Duplicate");
                int newLine = message.indexOf("\n", duplicate);
                String detail = message.substring(duplicate, newLine);
                if (message.contains("phone")) {
                    error = BaseError.DBExistPhone;
                } else if (message.contains("name")) {
                    error = BaseError.DBExistName;
                } else if (message.contains("email")) {
                    error = BaseError.DBExistEmail;
                } else {
                    error = BaseError.DBExist;
                }
                return new BaseException(error, detail);
            }
            return new BaseException(BaseError.DBError);
        } else if (message.contains("validation")) {
            return new BaseException(BaseError.BadRequest, message);
        } else if (message.contains(("no such file or directory"))) {
            return new BaseException(BaseError.BadRequest, message);
        }

        return new BaseException(50000, e.getMessage());
    }

}
