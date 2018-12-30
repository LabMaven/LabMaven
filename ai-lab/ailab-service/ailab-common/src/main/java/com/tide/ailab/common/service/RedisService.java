package com.tide.ailab.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Redis处理
 * @author User
 * @param <T>
 */
@Service
@Scope("prototype")
public class RedisService<T> {
    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;

    @Resource
    protected HashOperations<String, String, T> hashOperations;

    /**
     * 添加
     * @param key
     * @param value
     */
    public void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取key对应的value
     * @param key
     * @return
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 批量写入键值对
     * @param map
     */
    public void multiSet(HashMap<String, T> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 批量获取健对应的值
     * @param keys
     * @return
     */
    public List<Object> multiGet(List<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 模式匹配键值，返回满足模式的所有的键值
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 删除key
     * @param key
     * @return
     */
    public void del(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 在hash中添加
     * @param key
     * @param hashKey
     * @param value
     */
    public void hset(String key, String hashKey, T value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * hash批量写入：向key中批量写入map
     * @param key
     * @param map
     */
    public void hmset(String key, Map<String, T> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取hash类型数据，当前key下hashKey对应的value
     * @param key
     * @param hashKey
     * @return
     */
    public T hget(String key, String hashKey) {
        return hashOperations.get(key, hashKey);
    }

    /**
     * 返回hash中指定key的所有hashKey
     * @param key
     * @return
     */
    public Set<String> hkeys(String pattern) {
        return hashOperations.keys(pattern);
    }

    /**
     * 获取hash中当前key下所有的hashKey对应的value
     * @return
     */
    public List<T> hgetall(String key) {
        return hashOperations.values(key);
    }

    /**
     * 判断键值key在hash中是否存在hashKey
     */
    public boolean hexist(String key, String hashKey) {
        return hashOperations.hasKey(key, hashKey);
    }

    /**
     * 查询hash中当前key下数量
     * @return
     */
    public long hlen(String key) {
        return hashOperations.size(key);
    }

    /**
     * 删除hash中键值
     * @param key
     * @param hashKey
     * @return
     */
    public long hdel(String key, String hashKey) {
        return hashOperations.delete(key, hashKey);
    }

    /**
     * 删除hash中当前key下所有值
     */
    public void hempty(String key) {
        Set<String> set = hashOperations.keys(key);
        set.stream().forEach(hashKey -> hashOperations.delete(key, hashKey));
    }

    /**
     * 设置某个键的过期时间
     * @param expire 过期时间(单位:秒),传入 -1 时表示不设置过期时间
     */
    public void expire(String key, long expire) {
        redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }
}
