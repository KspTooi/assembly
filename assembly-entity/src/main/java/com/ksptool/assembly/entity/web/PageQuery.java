package com.ksptool.assembly.entity.web;

import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;


public class PageQuery {

    /**
     * 当前页码，从1开始
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer pageNum = 1;

    /**
     * 每页记录数
     */
    @Min(value = 1, message = "每页记录数必须大于0")
    private Integer pageSize = 10;

    /**
     * 获取JPA分页请求对象
     */
    public PageRequest pageRequest() {
        return PageRequest.of(pageNum - 1, pageSize);
    }

    /**
     * 获取分页计算器
     */
    public PageCalculator pageCalculator() {
        return new PageCalculator(pageNum, pageSize);
    }


    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
