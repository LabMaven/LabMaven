package com.znv.dispatch.usermanager;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.tide.ailab.usermanager.UserManagerApplication;

/**
 * 用户管理微服务单元测试类
 * @author User
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UserManagerApplication.class)
public class UserManagerControllerTests {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac; //注入WebApplicationContext

    @Before //在测试开始前初始化工作
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getHello() throws Exception {
        mvc.perform(get("/user/hello").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().string(equalTo("Hello World")));
    }

    @Test
    public void testSave() throws Exception {
        MvcResult result = mvc.perform(post("/user/save")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn(); // 返回执行请求的结果

        System.out.println("testSave result: " + result.getResponse().getContentAsString());
    }

    @Test
    public void testSaveUser() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", "90");
        map.put("name", "小新");
        map.put("address", "江苏南京");
        map.put("sex", 2);
        map.put("age", 50);
        map.put("createTime", new Date());
        map.put("remark", "呵呵呵");

        MvcResult result = mvc
                .perform(post("/user/save2").contentType(MediaType.APPLICATION_JSON)
                        .content(JSONObject.toJSONString(map)))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();

        System.out.println("testSave2 result: " + result.getResponse().getContentAsString());
    }

    @Test
    public void testDelete() throws Exception {
        MvcResult result = mvc.perform(delete("/user/delete/90")).andExpect(status().isOk()).andReturn(); // 返回执行请求的结果
        System.out.println("testDelete result: " + result.getResponse().getContentAsString());
    }

    @Test
    public void testGetAll() throws Exception {
        MvcResult result = mvc.perform(get("/user/getall")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn(); // 返回执行请求的结果
        System.out.println("testGetAll result: " + result.getResponse().getContentAsString());
    }

    @Test
    public void testGetUserbyId() throws Exception {
        MvcResult result = mvc.perform(get("/user/get/1")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8)).andReturn(); // 返回执行请求的结果
        System.out.println("testGetUserbyId result: " + result.getResponse().getContentAsString());
    }

    @Test
    public void testGetUserbyAddress() throws Exception {
        MvcResult result = mvc.perform(get("/user/find/广东深圳")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();

        System.out.println("testGetUserbyAddress result: " + result.getResponse().getContentAsString());
    }

    @Test
    public void testGetUserbyAddress2() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("address", "广东深圳");

        MvcResult result = mvc.perform(post("/user/find?address=广东深圳")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();

        System.out.println("testGetUserbyAddress result: " + result.getResponse().getContentAsString());
    }

    @Test
    public void testGetUsersByParams() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("address", "广东深圳");

        MvcResult result = mvc.perform(get("/user/sort?page=2&size=5&sort=age,desc"))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();

        System.out.println("testGetUsersByParams result: " + result.getResponse().getContentAsString());
    }
}
