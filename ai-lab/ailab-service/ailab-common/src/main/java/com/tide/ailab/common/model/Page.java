package com.tide.ailab.common.model;

/**
 * 前台分页请求转化对象
 * @author User
 */
public class Page {

    /** 页码，从1开始 */
    private int pageNum;

    /** 页面大小 */
    private int pageSize;

    /** 排序 */
    private String orderBy;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public String toString() {
        return "Page[pageNum=" + pageNum + ", pageSize=" + pageSize + ", orderBy=" + orderBy + "]";
    }

}
