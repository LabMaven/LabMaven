package com.tide.ailab.usermanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.tide.ailab.common.log.Logger;
import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;
import com.tide.ailab.common.model.Page;
import com.tide.ailab.common.model.TreeNode;
import com.tide.ailab.model.Role;
import com.tide.ailab.usermanager.service.MenuService;
import com.tide.ailab.usermanager.service.RoleService;
import com.tide.ailab.usermanager.service.UserService;

import io.swagger.annotations.ApiOperation;

/**
 * 角色管理控制类
 * @author User
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private UserService userService;

    /**
     * 分页查询角色列表
     * @param page
     * @param role
     * @return
     */
    @ApiOperation(value = "查询角色信息", notes = "查询角色信息")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String getRoleList(Page page, Role role) {
        JsonResult result = new JsonResult(JsonResultType.SUCCESS);
        PageInfo<Role> pageInfo = roleService.getRoleListPage(role, page);
        result.add("data", pageInfo);
        return result.toJSON();
    }

    /**
     * 新增角色
     * @param role
     * @return
     */
    @ApiOperation(value = "新增角色信息", notes = "新增角色信息")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addRole(@RequestBody Role role) {
        Logger.d("begin addRole#name:" + role.getName());
        JsonResult result = new JsonResult(JsonResultType.SUCCESS);
        roleService.addRole(role);
        Logger.d("end addRole#name:" + role.getName());
        return result.toJSON();
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @ApiOperation(value = "修改角色信息", notes = "修改角色信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateRole(@RequestBody Role role) {
        Logger.d("begin updateRole#name:" + role.getName());
        JsonResult result = new JsonResult(JsonResultType.SUCCESS);
        roleService.updateRole(role);
        Logger.d("end updateRole#name:" + role.getName());
        return result.toJSON();
    }

    /**
     * 根据角色ID删除角色
     * @param role
     * @return
     */
    @ApiOperation(value = "删除角色信息", notes = "删除角色信息")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String deleteRole(@RequestBody Role role) {
        Logger.d("begin deleteRole#id:" + role.getId());
        JsonResult result = new JsonResult(JsonResultType.SUCCESS);
        roleService.deleteRole(role.getId());
        Logger.d("end deleteRole#id:" + role.getId());
        return result.toJSON();
    }

    /**
     * 获取菜单树
     * @return
     */
    @ApiOperation(value = "获取菜单树", notes = "获取菜单树")
    @RequestMapping(value = "/getMenuTree", method = RequestMethod.GET)
    public String getMenuTree() {
        JsonResult result = new JsonResult(JsonResultType.SUCCESS);
        List<TreeNode> nodeList = menuService.getMenuTree();
        result.add("data", nodeList);
        return result.toJSON();
    }

    @RequestMapping(value = "/getLoginUserMenu", method = RequestMethod.GET)
    public String getLoginUserMenu() {
        JsonResult result = new JsonResult(JsonResultType.SUCCESS);
        String userId = userService.getCurrentUser().getId();
        List<TreeNode> nodeList = menuService.getLoginUserMenus(userId);
        result.add("data", nodeList);
        return result.toJSON();
    }

    /**
     * 获取角色树
     * @return
     */
    @ApiOperation(value = "获取角色树", notes = "获取角色树")
    @RequestMapping(value = "/getRoleTree", method = RequestMethod.GET)
    public String getRoleTree() {
        JsonResult result = new JsonResult(JsonResultType.SUCCESS);
        Role condition = new Role();
        List<TreeNode> nodeList = roleService.getRoleTree(condition);
        result.add("data", nodeList);
        return result.toJSON();
    }

}
