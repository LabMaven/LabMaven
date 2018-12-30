package com.tide.ailab.model;

import java.util.List;

/**
 * 菜单数据类
 * @author yinyuntao
 */
public class Menu implements Comparable<Menu> {

    /**
     * 菜单ID
     */
    private int id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父菜单ID
     */
    private int parentId;

    /**
     * 菜单链接地址
     */
    private String url;

    /**
     * 菜单样式
     */
    private String style;

    /**
     * 排序
     */
    private int sort;

    /**
     * 子菜单列表
     */
    private List<Menu> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public List<Menu> getItems() {
        return items;
    }

    public void setItems(List<Menu> items) {
        this.items = items;
    }

    @Override
    public int compareTo(Menu menu) {
        return this.sort - menu.getSort();
    }
}
