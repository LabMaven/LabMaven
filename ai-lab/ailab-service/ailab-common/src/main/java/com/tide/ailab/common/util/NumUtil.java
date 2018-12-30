package com.tide.ailab.common.util;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * 数字处理常用类
 * @author User
 */
public class NumUtil {
    public static void main(String[] args) throws Exception {
        byte[] bytes = float2bytes(987654321.2345678901f);
        float f = bytes2float(bytes);
        System.out.println(f);
        System.out.println(bytes2hex(bytes, true));
        System.out.println(bytes2hex(bytes, false));

        // 翻转字节数组
        byte[] buf = new byte[] { 0x10, 0x0A, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        System.out.println(bytes2hex(buf, true));
        System.out.println(bytes2hex(buf, false));

        // 16进制转字节
        // 字节转16进制字符串
        // 16进制字符串转10进制
        System.out.println(hex2int("ac"));
        // 10进制转16进制字符串
        System.out.println(int2hex(172111116333333L));

        System.out.println(intstring2hex("010400000001000000010100000052942910348516417866466"));
        System.out.println(hex2intstring("01040000000100000001010000005294291034851641786646a"));

        String str = "AC";
        byte[] b = hex2bytes(str);
        System.out.println(b.length);
        String tmp = bytes2hex(b);
        System.out.println(tmp);
        System.out.println("--");
    }

    /**
     * 格式化显示
     * @param value
     * @return
     */
    public static String format(float value) {
        return decimalFormat2.format(value);
    }

    /**
     * 格式化显示
     * @param value
     * @return
     */
    public static String format(float value, String pattern) {
        if (pattern.equals("0.0")) {
            return decimalFormat1.format(value);
        } else {
            return decimalFormat2.format(value);
        }
    }

    /**
     * 判断是否为整数.
     * @param source 传入的字符串
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String source) {
        boolean rtn = false;
        if (source != null && !source.trim().equals("")) {
            rtn = patternInteger.matcher(source).matches();
        }
        return rtn;
    }

    /**
     * 是否为指定范围的整数.
     * @param source
     * @param minValue
     * @param maxValue
     * @return
     */
    public static boolean isInteger(String source, Integer minValue, Integer maxValue) {
        boolean rtn = false;
        if (source != null && !source.trim().equals("")) {
            rtn = patternInteger.matcher(source).matches();
            if (rtn) {
                long value = Long.parseLong(source);
                if (value < minValue || value > maxValue) {
                    rtn = false;
                }
            }
        }
        return rtn;
    }

    /**
     * 判断是否为浮点数，包括double和float.
     * @param source 传入的字符串
     * @return 是浮点数返回true,否则返回false
     */
    public static boolean isDouble(String source) {
        boolean rtn = false;
        if (source != null && !source.trim().equals("")) {
            rtn = patternDouble.matcher(source).matches();
        }
        return rtn;
    }

    /**
     * 浮点转换为字节
     * @param f
     * @return
     */
    public static byte[] float2bytes(float f) {
        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }
        // 翻转数组
        int len = b.length;
        // 建立一个与源数组元素类型相同的数组
        byte[] dest = new byte[len];
        // 为了防止修改源数组，将源数组拷贝一份副本
        System.arraycopy(b, 0, dest, 0, len);
        byte temp;
        // 将顺位第i个与倒数第i个交换
        for (int i = 0; i < len / 2; ++i) {
            temp = dest[i];
            dest[i] = dest[len - i - 1];
            dest[len - i - 1] = temp;
        }
        return dest;
    }

    /**
     * 字节转换为浮点
     * @param b 字节（至少4个字节）
     * @return
     */
    public static float bytes2float(byte[] b) {
        int l;
        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * int类型转byte[]类型
     */
    public static byte[] int2bytes(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    /**
     * 将4字节的byte数组转成int值
     */
    public static int bytes2int(byte[] b) {
        byte[] a = new byte[4];
        int i = a.length - 1, j = b.length - 1;
        // 从b的尾部(即int值的低位)开始copy数据
        for (; i >= 0; i--, j--) {
            // 如果b.length不足4,则将高位补0
            a[i] = j >= 0 ? b[j] : 0;
        }
        // &0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
        int v0 = (a[0] & 0xff) << 24;
        int v1 = (a[1] & 0xff) << 16;
        int v2 = (a[2] & 0xff) << 8;
        int v3 = (a[3] & 0xff);
        return v0 + v1 + v2 + v3;
    }

    /**
     * 长16进制转长10进制
     * @param value
     * @return
     */
    public static String hex2intstring(String value) {
        return (new BigInteger(value, 16)).toString(10);
    }

    /**
     * 长10进制转长16进制
     * @param value
     * @return
     */
    public static String intstring2hex(String value) {
        return (new BigInteger(value, 10)).toString(16);
    }

    /**
     * 16进制转10进制
     * @param hex
     * @return
     */
    public static Long hex2int(String hex) {
        return Long.parseLong(hex, 16);
    }

    /**
     * 10进制转16进制
     * @param value
     * @return
     */
    public static String int2hex(Long value) {
        return Long.toHexString(value).toUpperCase();
    }

    /**
     * 字节转16进制
     * @param b
     * @return
     */
    public static String bytes2hex(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    /**
     * 字节转16进制
     * @param bytes
     * @param bigEndian true表示高位Byte放在内存中Word区域的低地址处，false表示低位Byte放在内存中Word区域的低地址处
     * @return
     */
    public static String bytes2hex(byte[] bytes, boolean bigEndian) {
        if (!bigEndian) {
            int len = bytes.length;
            int halflen = len / 2;
            for (int i = 0; i < halflen; i++) {
                byte b = bytes[i];
                bytes[i] = bytes[len - i - 1];
                bytes[len - i - 1] = b;
            }
        }
        return bytes2hex(bytes);
    }

    /**
     * 16进制转字节
     * @param hexString
     * @return
     */
    public static byte[] hex2bytes(String hexString) {
        return new BigInteger(hexString, 16).toByteArray();
    }

    /**
     * 一个将字节转化为十六进制ASSIC码的函数.
     * @param ib
     * @return
     */
    public static String byteHEX(byte ib) {
        char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        char[] ob = new char[2];
        ob[0] = digit[(ib >>> 4) & 0X0F];
        ob[1] = digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }

    /**
     * 获取随机数
     * @param min 最小值
     * @param max 最大值
     * @return
     */
    public static int getRandNum(int min, int max) {
        int randNum = min + (int) (Math.random() * ((max - min) + 1));
        return randNum;
    }

    /**
     * 获取六位数字
     * @return
     */
    public static String getSixLengthNum() {
        int verifyCode = getRandNum(1, 999999);
        DecimalFormat df = new DecimalFormat(strformat);
        return df.format(verifyCode);
    }

    /** 构造方法的字符格式这里如果小数不足2位，会以0补足 */
    private static DecimalFormat decimalFormat1 = new DecimalFormat("0.0");
    private static DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
    private static Pattern patternInteger = Pattern.compile("^[-\\+]?[\\d]*$");
    private static Pattern patternDouble = Pattern.compile("^[-\\+]?[\\d]*(.[\\d]+((e|E)[\\d]+)?)?$");
    private static String strformat = "000000";
}
