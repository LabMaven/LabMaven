package com.tide.ailab.common.model;

import java.util.List;

/**
 * el-tree数据节点类
 * @author yinyuntao
 */
public class TreeNode implements Comparable<TreeNode> {

    private String id;

    private String label;

    private String parentId;

    private int level;

    private String path; // 路径

    private String style; // 图标

    private String name; // ztree属性

    private boolean open; // ztree属性

    private boolean nocheck;

    private List<TreeNode> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isNocheck() {
        return nocheck;
    }

    public void setNocheck(boolean nocheck) {
        this.nocheck = nocheck;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int compareTo(TreeNode node) {
        return this.getId().compareTo(node.getId());
    }

    @Override
    public String toString() {
        return "TreeNode{" + "id='" + id + '\'' + ", parentId='" + parentId + '\'' + ", label='" + label
                + '\'' + ", children=" + children + '}';
    }
}
