package com.tide.ailab.common.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.tide.ailab.common.log.Logger;


/**
 * Object ObjectUtil
 * @author User
 */
public class ObjectUtil {

    /**
     * 判断是否为空
     * @param source
     * @return
     */
    public static boolean isNull(Object source) {
        return source == null;
    }

    /**
     * compare two object
     * @param actual
     * @param expected
     * @return
     *         <ul>
     *         <li>if both are null, return true</li>
     *         <li>return actual.{@link Object#equals(Object)}</li>
     *         </ul>
     */
    public static boolean isEquals(Object actual, Object expected) {
        return actual == expected || (actual == null ? expected == null : actual.equals(expected));
    }

    /**
     * compare two object
     * <ul>
     * <strong>About result</strong>
     * <li>if v1 > v2, return 1</li>
     * <li>if v1 = v2, return 0</li>
     * <li>if v1 < v2, return -1</li>
     * </ul>
     * <ul>
     * <strong>About rule</strong>
     * <li>if v1 is null, v2 is null, then return 0</li>
     * <li>if v1 is null, v2 is not null, then return -1</li>
     * <li>if v1 is not null, v2 is null, then return 1</li>
     * <li>return v1.{@link Comparable#compareTo(Object)}</li>
     * </ul>
     * @param v1
     * @param v2
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <V> int compare(V v1, V v2) {
        return v1 == null ? (v2 == null ? 0 : -1) : (v2 == null ? 1 : ((Comparable) v1).compareTo(v2));
    }

    /**
     * 从jar包中获取第一个子类
     * @param <T>
     * @param jarPath jar包的路径,不含文件分隔符
     * @param jarName jar包名称
     * @param superClazz 父类
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Object> T getFirstSubClass(String jarPath, String jarName, Class<T> superClazz) {
        JarFile jarFile;
        T t = null;
        try {
            jarFile = new JarFile(jarPath + File.separator + jarName);
        } catch (IOException e) {
            Logger.e(e.getMessage());
            return null;
        }
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String name = jarEntry.getName();
            if (name.endsWith(".class") && !name.contains("$")) {
                String className = name.replace("/", ".").substring(0, name.lastIndexOf("."));
                try {
                    Class<?> c = Class.forName(className);
                    if (c.getSuperclass() == superClazz) {
                        t = (T) c.newInstance();
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
        try {
            jarFile.close();
        } catch (IOException e) {
            jarFile = null;
        }

        return t;
    }

    /**
     * 从jar包中查找并加载第一个实现类,实现类需要带上默认的构造器
     * @param <T>
     * @param jarPath jar包的路径,不含文件分隔符
     * @param jarName jar包名称
     * @param interfaceClazz 接口
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Object> T getFirstImpl(String jarPath, String jarName, Class<T> interfaceClazz) {
        JarFile jarFile;
        T t = null;
        try {
            jarFile = new JarFile(jarPath + File.separator + jarName);
        } catch (IOException e) {
            Logger.e(e.getMessage());
            return null;
        }
        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String name = jarEntry.getName();
            if (name.endsWith(".class") && !name.contains("$")) {
                String className = name.replace("/", ".").substring(0, name.lastIndexOf("."));
                try {
                    Class<?> c = Class.forName(className);
                    for (Class<?> clazz : c.getInterfaces()) {
                        if (clazz == interfaceClazz) {
                            t = (T) c.newInstance();
                            break;
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        try {
            jarFile.close();
        } catch (IOException e) {
            jarFile = null;
        }
        return t;
    }

    /**
     * 从jar包中查找加载响应的类
     * @param <T>
     * @param jarPath jar包的路径
     * @param jarName jar包名称
     * @param superClazz 父类
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Object> T loadObject(String jarPath, String jarName, Class<T> superClazz) {
        try {
            @SuppressWarnings("resource")
            JarFile jarFile = new JarFile(jarPath + File.separator + jarName);
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                String name = jarEntry.getName();
                if (name.endsWith(".class") && !name.contains("$")) {
                    String className = name.replace("/", ".").substring(0, name.lastIndexOf("."));
                    try {
                        Class<?> c = Class.forName(className);
                        if (c.getSuperclass() == superClazz) {
                            return (T) c.newInstance();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从jar包中查找加载所有相应的类
     * @param <T>
     * @param jarPath jar包的路径
     * @param jarName jar包名称
     * @param superClazz 父类
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Object> List<T> loadAll(String jarPath, String jarName, Class<T> superClazz) {
        List<T> list = new ArrayList<T>();
        try {
            @SuppressWarnings("resource")
            JarFile jarFile = new JarFile(jarPath + File.separator + jarName);
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                String name = jarEntry.getName();
                if (name.endsWith(".class")) {
                    String className = name.replace("/", ".").substring(0, name.lastIndexOf("."));
                    try {
                        Class<?> c = Class.forName(className);
                        if (c.getSuperclass() == superClazz) {
                            list.add((T) c.newInstance());
                        }
                    } catch (Exception e) {
                        Logger.e("the className " + className + " is not matched!");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
