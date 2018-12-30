package com.tide.ailab.common.util;

import java.io.File;

import com.tide.ailab.common.log.Logger;


/**
 * 路径相关常用类
 * @author User
 */
public class PathUtil {

    /**
     * 获取调用类所在的路径
     * 调试环境获取bin目录，运行环境获取jar的目录，WEB项目获取classes目录
     * 返回的目录最后不包含目录分隔符
     * @return
     */
    public static String getBinOrJarPath() {
        String rtn = "";
        try {
            String invokeClassName = Thread.currentThread().getStackTrace()[2].getClassName();
            Class<?> invokeClazz = Class.forName(invokeClassName);
            String path = java.net.URLDecoder.decode(
                    invokeClazz.getProtectionDomain().getCodeSource().getLocation().getFile(), "utf-8");
            rtn = path.replace("/", File.separator);
            if (rtn.startsWith("file:")) {
                rtn = path.substring(5);
            }
            if (rtn.endsWith(".jar")) {
                rtn = path.substring(0, rtn.lastIndexOf(File.separator) + 1);
            } else if (rtn.endsWith(".class")) {
                String splitStr = "classes";
                rtn = path.substring(0, rtn.indexOf(splitStr) + splitStr.length());
            }
        } catch (Exception e) {
            Logger.e(e);
        }
        return rtn;
    }

    public static String getLibPath() {
        try {
            String path = java.net.URLDecoder.decode(
                    PathUtil.class.getProtectionDomain().getCodeSource().getLocation().getFile(), "utf-8");
            path = path.replace("/", File.separator);
            if (path.endsWith(File.separator)) {
                path = path.substring(0, path.length() - 1);
            }
            if (path.startsWith("file:")) {
                path = path.substring(5);
            }
            String libPath = "";
            if (path.endsWith(".jar")) {
                libPath = path.substring(0, path.lastIndexOf(File.separator));
            } else {
                libPath = path;
            }
            if (!libPath.endsWith(File.separator)) {
                return libPath + File.separator;
            } else {
                return libPath;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获得类所在的路径
     * @param clazz
     * @return
     */
    public static String getAppPath(Class<?> clazz) {
        String str = clazz.getProtectionDomain().getCodeSource().getLocation().getFile();
        try {
            str = java.net.URLDecoder.decode(str, "utf-8");
        } catch (Throwable e) {
            System.out.println(String.format("Decode URL throw exception %s", e.getMessage()));
            return "";
        }
        int intStart = str.indexOf("/");
        if (intStart < 0) {
            intStart = 0;
        }
        int intEnd = str.lastIndexOf("!/");
        if (intEnd < 0) {
            intEnd = str.lastIndexOf("/") + 1;
        } else {
            intEnd = str.substring(0, intEnd).lastIndexOf("/") + 1;
        }
        if (intEnd < 0) {
            intEnd = str.length();
        }
        str = str.substring(intStart, intEnd);
        return str;
    }

    public static void main(String[] args) {
        String filePath = "E:/10SVN/SCoP/V120/src/usms/server/usms.bem/bin/ServiceConfig.xml";
        filePath = filePath.replace('/', '\\');
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not exit.");
        }
    }

}
