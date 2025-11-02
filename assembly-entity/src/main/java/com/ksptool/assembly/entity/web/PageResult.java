package com.ksptool.assembly.entity.web;

import com.ksptool.assembly.entity.exception.BizException;

import java.util.Collection;
import java.util.Collections;

public class PageResult<T> {

    //业务状态码：0-正常，1-业务异常，2-内部服务器错误
    private final int code;

    //描述信息
    private final String message;

    //返回数据集合
    private final Collection<T> data;

    //总记录数
    private final Integer total;

    protected PageResult(int code, String message, Collection<T> data, Integer total) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.total = total;
    }

    // 业务正常
    public static <T> PageResult<T> success(Collection<T> data, Integer total) {
        return new PageResult<>(0, "success", data, total);
    }

    public static <T> PageResult<T> success(Collection<T> data, Long total) {
        return new PageResult<>(0, "success", data, Integer.parseInt(total.toString()));
    }

    // 业务正常但无数据
    public static <T> PageResult<T> successWithEmpty() {
        return new PageResult<>(0, "无数据", Collections.emptyList(), 0);
    }

    public static <T> PageResult<T> success(String message, Collection<T> data, Integer total) {
        return new PageResult<>(0, message, data, total);
    }

    // 业务异常
    public static <T> PageResult<T> error(String message) {
        return new PageResult<>(1, message, Collections.emptyList(), 0);
    }

    // 业务异常
    public static <T> PageResult<T> error(BizException e) {
        return new PageResult<>(1, e.getMessage(), Collections.emptyList(), 0);
    }

    public static <T> PageResult<T> error(int code, String message) {
        return new PageResult<>(code, message, Collections.emptyList(), 0);
    }

    // 内部服务器错误
    public static <T> PageResult<T> internalError(String message, Object throwable) {
        return new PageResult<>(1, message, Collections.emptyList(), 0);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Collection<T> getData() {
        return data;
    }

    public Integer getTotal() {
        return total;
    }
}
