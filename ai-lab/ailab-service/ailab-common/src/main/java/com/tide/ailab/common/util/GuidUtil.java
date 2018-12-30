package com.tide.ailab.common.util;

import java.util.UUID;

/**
 * 随机数生成类
 * @author User
 * @version
 */
public class GuidUtil {

    /**
     * 返回java生成的uuid字符串
     * @return
     */
    public static String getGuid() {
        String guid = UUID.randomUUID().toString();
        return guid;
    }

    /**
     * 返回不带连字符(-)的UUID字符串
     * 由于java生成的uuid带有4个连字符(-)，总长度为36位
     * 此处返回不带连字符的32位UUID字符串
     * @return
     */
    public static String getGuidWithoutHyphen() {
        return getGuid().replaceAll("-", "");
    }

}
