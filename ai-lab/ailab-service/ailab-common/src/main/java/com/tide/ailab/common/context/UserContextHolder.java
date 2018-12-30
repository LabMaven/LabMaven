package com.tide.ailab.common.context;

/**
 * @author User
 */
public final class UserContextHolder {

    private static final ThreadLocal<String> USER_THREAD_LOCAL = new ThreadLocal<String>() {

        @Override
        protected String initialValue() {
            return "";
        }
    };

    public static String getUserId() {
        return USER_THREAD_LOCAL.get();
    }

    public static void setUserId(String userId) {
        USER_THREAD_LOCAL.set(userId);
    }

    public static void reset() {
        USER_THREAD_LOCAL.remove();
    }

}
