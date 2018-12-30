package com.tide.ailab.usermanager.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tide.ailab.common.model.JsonResult;
import com.tide.ailab.common.model.JsonResultType;
import com.tide.ailab.common.service.RedisService;
import com.tide.ailab.model.User;

import io.swagger.annotations.ApiOperation;

/**
 * redis管理控制类
 * @author User
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisService<User> redisService;

    private static final String KEY = "USER_REDIS_KEY";

    @ApiOperation(value = "批量添加", notes = "批量添加")
    @RequestMapping(value = "/addusers", method = RequestMethod.POST)
    public String addUsers() {
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setId(i + "");
            user.setUserName("张三" + i);
            user.setCreateTime(new Date());
            users.add(user);
            redisService.set(user.getId(), user);
        }
        JsonResult result = new JsonResult(JsonResultType.SUCCESS);
        result.setMessage("添加用户成功!");
        return result.toJSON();
    }

    @ApiOperation(value = "增加用户(hash类型)", notes = "增加用户")
    @RequestMapping(value = "/addusers2hash", method = RequestMethod.POST)
    public String addUsersToHash() {
        Map<String, User> map = new HashMap<String, User>();
        for (int i = 0; i < 20; i++) {
            User user = new User();
            user.setId(i + "");
            user.setUserName("张三" + i);
            user.setCreateTime(new Date());
            map.put(user.getId(), user);
        }
        redisService.hmset("USER_REDIS_KEY", map);
        JsonResult result = new JsonResult(JsonResultType.SUCCESS);
        result.setMessage("redis hash操作：添加用户成功!");
        return result.toJSON();
    }

    @ApiOperation(value = "查询所有", notes = "查询所有")
    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    public List<Object> findAll() {
        Set<String> keys = redisService.keys("*");
        return redisService.multiGet(new ArrayList<String>(keys));
    }

    @ApiOperation(value = "查询用户详情", notes = "查询用户详情")
    @RequestMapping(value = "/finduser/{id}", method = { RequestMethod.GET })
    public User findUser(@PathVariable String id) {
        return (User) redisService.get(id);
    }

    @ApiOperation(value = "删除用户", notes = "删除用户")
    @RequestMapping(value = "/deleteuser/{id}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable String id) {
        redisService.del(id);
        JsonResult result = new JsonResult(JsonResultType.SUCCESS);
        result.setMessage("删除用户成功!");
        return result.toJSON();
    }

    /**
     * 删除存在hash中的数据
     * @param id
     * @return
     */
    @ApiOperation(value = "删除Hash中用户", notes = "删除Hash中用户")
    @RequestMapping(value = "/deletehash/{id}", method = RequestMethod.DELETE)
    public String delUserInHash(@PathVariable String id) {
        redisService.hdel(KEY, id);
        JsonResult result = new JsonResult(JsonResultType.SUCCESS);
        result.setMessage("redis hash操作：删除用户成功!");
        return result.toJSON();
    }

    /**
     * 删除指定key的hash数据
     * @param id
     * @return
     */
    @ApiOperation(value = "清空Hash数据", notes = "清空Hash数据")
    @RequestMapping(value = "/emptyhash", method = RequestMethod.DELETE)
    public String emptyHash() {
        redisService.hempty(KEY);
        JsonResult result = new JsonResult(JsonResultType.SUCCESS);
        result.setMessage("redis hash操作：清空hash中数据成功!");
        return result.toJSON();
    }
}
