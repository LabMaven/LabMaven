package com.tide.ailab.usermanager.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tide.ailab.common.model.TreeNode;
import com.tide.ailab.common.util.TreeBuilder;
import com.tide.ailab.dao.MenuDao;
import com.tide.ailab.model.Menu;

/**
 * 菜单业务类
 * @author User
 */
@Service
@Scope("prototype")
public class MenuService {

    @Autowired
    private MenuDao menuDao;

    /**
     * 获取所有菜单列表
     * @return
     */
    public List<Menu> getAllMenus() {
        return menuDao.getAllMenus();
    }

    /**
     * 获取用户能够访问的菜单树
     * @return
     */
    public List<TreeNode> getLoginUserMenus(String userId) {
        List<Menu> menuList = menuDao.getLoginUserMenus(userId);

        List<TreeNode> nodeList = new ArrayList<TreeNode>();
        if (CollectionUtils.isEmpty(menuList)) {
            return nodeList;
        }

        Collections.sort(menuList);

        for (Menu menu : menuList) {
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(menu.getId()));
            node.setLabel(menu.getName());
            node.setParentId(String.valueOf(menu.getParentId()));
            node.setPath(menu.getUrl());
            node.setStyle(menu.getStyle());

            nodeList.add(node);
        }

        return TreeBuilder.buildByRecursive(nodeList);
    }

    /**
     * 获取层级结构的菜单列表
     * @return
     */
    public List<TreeNode> getMenuTree() {
        List<Menu> menuList = menuDao.getAllMenus();

        List<TreeNode> nodeList = new ArrayList<TreeNode>();
        if (CollectionUtils.isEmpty(menuList)) {
            return nodeList;
        }

        Collections.sort(menuList);

        for (Menu menu : menuList) {
            TreeNode node = new TreeNode();
            node.setId(String.valueOf(menu.getId()));
            node.setLabel(menu.getName());
            node.setParentId(String.valueOf(menu.getParentId()));
            node.setPath(menu.getUrl());

            nodeList.add(node);
        }

        return TreeBuilder.buildByRecursive(nodeList);
    }

}
