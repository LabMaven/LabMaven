package com.tide.ailab.common.service;

import java.lang.reflect.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.tide.ailab.common.consts.Consts;

/**
 * Redis系统参数处理
 * 
 * @author User
 */
@Service
public class ParamRedisService {

    @Autowired
    private ValueOperations<String, Object> valueOperations;

    /**
     * 根据账号id和配置项名称，获取配置项的值
     * 
     * @param accountId 账户id
     * @param name 配置项名称
     * @return 配置项的值
     */
    public String getParamValue(String accountId, String name) {
        String key = Consts.RedisKey.PARAM_REDIS_PRE + accountId + "." + name; // 拼接配置信息在redis中的key
        String value = null;

        try {
            Object obj = valueOperations.get(key); // 从redis中获取该key对应的对象，因为配置的实体类定义在dao里面，common没有引入dao，所以没法用配置的实体类来接收，只能用Object
            if (null == obj) {
                return null;
            }
            Field field = obj.getClass().getDeclaredField("value"); // 获取该对象 value属性
            field.setAccessible(true);
            value = (String) field.get(obj); // 获取value值

        } catch (IllegalArgumentException | IllegalAccessException e) {

            e.printStackTrace();

        } catch (NoSuchFieldException e) {

            e.printStackTrace();

        } catch (SecurityException e) {

            e.printStackTrace();
        }
        return value;

    }

    /**
     * 根据故障类型id获取得故障类型信息
     * 
     * @param falutId
     * @return 故障类型信息
     */
    public Object getFalutById(int falutId) {
        // 拼接配置信息在redis中的key
        String key = Consts.RedisKey.FALUTTYPE_REDIS_PRE + falutId;
        // 从redis中获取该key对应的对象，因为配置的实体类定义在dao里面，common没有引入dao，所以没法用配置的实体类来接收，只能用Object
        Object obj = valueOperations.get(key);
        return obj;
    }

    @SuppressWarnings("unchecked")
    public <T> T getObjByKey(String key, Class<T> c) {
        Object obj = valueOperations.get(key);
        return (T) obj;
    }
}
