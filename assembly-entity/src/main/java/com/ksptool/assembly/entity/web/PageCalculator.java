package com.ksptool.assembly.entity.web;

public class PageCalculator {

    private int currentPage;  // 当前页码
    private int pageSize;     // 每页记录数
    private String limitSql;

    private int offset;
    private int limit;

    // 构造函数带有校验
    public PageCalculator(int currentPage, int pageSize) {
        if (currentPage <= 0) {
            currentPage = 1;
        }
        if (pageSize <= 0) {
            pageSize = 1;
        }
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.limitSql = getSqlLimitClause();
        this.offset = getOffset();
        this.limit = getLimit();
    }

    // 设置页码并校验
    public void setCurrentPage(int currentPage) {
        if (currentPage <= 0) {
            currentPage = 1;
        }
        this.currentPage = currentPage;
    }

    // 设置每页记录数并校验
    public void setPageSize(int pageSize) {
        if (pageSize <= 0) {
            pageSize = 1;
        }
        this.pageSize = pageSize;
    }

    // 计算SQL中的offset值
    public int getOffset() {
        return (currentPage - 1) * pageSize;
    }

    // 计算SQL中的limit值
    public int getLimit() {
        return pageSize;
    }

    // 获取用于SQL的分页限制字符串
    public String getSqlLimitClause() {
        return String.format("LIMIT %d, %d", getOffset(), getLimit());
    }

    public int next() {
        currentPage = currentPage + 1;
        return currentPage;
    }

/*
    public static void main(String[] args) {

        PageCalculator pagination = new PageCalculator(3, 10);

        System.out.println("OFFSET: " + pagination.getOffset());
        System.out.println("LIMIT: " + pagination.getLimit());
        System.out.println("SQL Limit Clause: " + pagination.getSqlLimitClause());

    }
*/

}