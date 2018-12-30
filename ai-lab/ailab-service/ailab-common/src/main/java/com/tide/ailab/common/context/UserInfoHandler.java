package com.tide.ailab.common.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tide.ailab.common.model.UserInfo;
import com.tide.ailab.common.service.RedisService;

/**
 * 用户信息handler
 * @author User
 */
@Component
public class UserInfoHandler {

    @Autowired
    RedisService<UserInfo> redisService;

    public UserInfo getCurrentUser() {

        // 获取当前登录用户
        String userId = UserContextHolder.getUserId();
        if (redisService.get(userId) == null) {
            return new UserInfo();
        } else {
            return (UserInfo) redisService.get(userId);
        }
    }
}
