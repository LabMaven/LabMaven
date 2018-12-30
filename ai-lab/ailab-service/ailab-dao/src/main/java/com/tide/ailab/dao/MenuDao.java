package com.tide.ailab.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tide.ailab.model.Menu;

/**
 * 菜单数据原子操作
 * @author yinyuntao
 */
@Repository
public interface MenuDao {

    /**
     * 查询所有菜单列表
     * @return
     */
    List<Menu> getAllMenus();

    /**
     * 查询用户能够访问的菜单列表
     * @return
     */
    List<Menu> getLoginUserMenus(String userId);

}
