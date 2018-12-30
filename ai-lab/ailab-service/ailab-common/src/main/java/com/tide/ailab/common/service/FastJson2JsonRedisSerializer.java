package com.tide.ailab.common.service;

import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 用fastjson将对象序列化为json，或者将json反序列化为对象
 * @author User
 * @param <T>
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    /**
     * 将对象序列化为二进制数组，该方法由springboot完成调用
     * @param t 需要序列化的对象
     * @return
     */
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }

        if ("String".equals(t.getClass().getSimpleName())) {

            return t.toString().getBytes(DEFAULT_CHARSET);

        } else {

            String json = JSON.toJSONString(t, SerializerFeature.WriteClassName);

            return json.getBytes(DEFAULT_CHARSET);
        }
    }

    /**
     * 将json序列化之后的二进制数组，反序列化为对象，该方法由springboot完成调用
     * @param bytes json对应的二进制数组
     * @return
     */
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        if ((!str.startsWith("\"")) && (!str.startsWith("{"))) {
            str = "\"" + str;
        }
        if ((!str.endsWith("\"")) && (!str.endsWith("}"))) {
            str = str + "\"";
        }

        T t = JSON.parseObject(str, clazz);

        return t;
    }

}
