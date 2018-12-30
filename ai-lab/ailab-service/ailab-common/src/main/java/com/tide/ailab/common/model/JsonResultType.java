package com.tide.ailab.common.model;

/**
 * json返回对象类型
 * @author user
 */
public enum JsonResultType {
    /**
     * 错误
     */
    ERROR(-1),

    /**
     * 成功
     */
    SUCCESS(0),

    /**
     * 处理超时
     */
    TIMEOUT(1),

    /**
     * SESSION过期
     */
    SESSIONTIMEOUT(2);

    private int value;

    /**
     * 获取枚举值
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     * 构造函数
     * @param value
     */
    JsonResultType(int value) {
        this.value = value;
    }

    /**
     * 整形转换为枚举
     * @param value 整形值
     * @return 枚举值
     */
    public static JsonResultType parse(int value) {
        JsonResultType rtn = JsonResultType.ERROR;
        switch (value) {
            case -1:
                rtn = ERROR;
                break;
            case 0:
                rtn = SUCCESS;
                break;
            case 1:
                rtn = TIMEOUT;
                break;
            case 2:
                rtn = SESSIONTIMEOUT;
                break;
            default:
                rtn = ERROR;
        }
        return rtn;
    }
}
