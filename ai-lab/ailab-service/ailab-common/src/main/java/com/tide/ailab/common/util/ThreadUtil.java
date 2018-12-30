package com.tide.ailab.common.util;

/**
 * 线程常用类
 * @author User
 */
public class ThreadUtil {
    /**
     * 暂停多少秒
     * @param time
     */
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }
}
