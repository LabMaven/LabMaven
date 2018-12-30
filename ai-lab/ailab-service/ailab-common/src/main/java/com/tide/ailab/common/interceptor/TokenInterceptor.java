package com.tide.ailab.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.tide.ailab.common.context.UserContextHolder;
import com.tide.ailab.common.exception.DispatchExceptionCode;
import com.tide.ailab.common.exception.DispatchUnauthException;
import com.tide.ailab.common.service.TokenService;
import com.tide.ailab.common.util.StringUtil;

import io.jsonwebtoken.Claims;

/**
 * token拦截器
 * @author User
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    TokenService tokenService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj,
            Exception ex) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj,
            ModelAndView mv) throws Exception {

    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj)
            throws Exception {
        boolean isValidToken = false;
        String token = request.getHeader("token");
        if (!StringUtil.isNullOrEmpty(token)) {
            Claims claims = tokenService.parseJWT(token);
            // 能够正常解析token，直接跳转当前请求
            if (claims != null) {
                isValidToken = true;

                String userId = (String) JSONObject.parseObject(claims.getSubject()).get("userId");
                UserContextHolder.setUserId(userId);
            }
        }

        // token为空或无效时，抛出异常，由前端捕获异常跳转至登录页面
        if (!isValidToken) {
            // token 校验失败，清空ThreadLocal中的userId
            UserContextHolder.reset();
            throw new DispatchUnauthException(DispatchExceptionCode.TOKEN_INVALID);
        }

        return true; // 只有返回true才会继续向下执行，返回false取消当前请求
    }

}
