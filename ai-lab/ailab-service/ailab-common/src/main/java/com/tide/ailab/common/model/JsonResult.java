package com.tide.ailab.common.model;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 返回给前台的json结果
 * @author User
 */
public class JsonResult {

    /**
     * 构造函数
     */
    public JsonResult() {
        this.type = JsonResultType.ERROR;
    }

    /**
     * 构造函数
     * @param type 结果类型
     */
    public JsonResult(JsonResultType type) {
        this.type = type;
    }

    /**
     * 设置结果类型
     * @param type
     */
    public void setType(JsonResultType type) {
        this.type = type;
    }

    /**
     * 获得结果类型
     * @return
     */
    public JsonResultType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 增加传出的结果
     * @param key
     * @param value
     */
    public void add(String key, Object value) {
        if (this.items.containsKey(key)) {
            this.items.remove(key);
            this.items.put(key, value);
        } else {
            this.items.put(key, value);
        }
    }

    /**
     * 移除传出的结果
     * @param key
     */
    public void remove(String key) {
        this.items.remove(key);
    }

    /**
     * 输出JSON
     * @return
     */
    public String toJSON() {
        this.items.put(TAGCODE, this.getType().getValue());
        if (type == JsonResultType.SESSIONTIMEOUT) {
            this.items.put(TAGURL, "");
        }

        this.items.put(TAGMSG, this.getMessage());
        JSONObject json = new JSONObject();
        json.putAll(this.items);
        return JSONObject.toJSONString(json, SerializerFeature.WriteNullStringAsEmpty);
    }

    /**
     * 输出参数列表
     * @return
     */
    public Map<String, Object> getResult() {
        this.items.put(TAGCODE, this.getType().getValue());
        this.items.put(TAGMSG, this.getMessage());
        return items;
    }

    /**
     * 结果类型
     */
    private JsonResultType type;
    private String message = null;
    private static final String TAGCODE = "code";
    private static final String TAGMSG = "msg";
    private static final String TAGURL = "url";
    private Map<String, Object> items = new HashMap<String, Object>();
}
