package com.tide.ailab.common.util;

import java.security.Key;
import java.security.MessageDigest;
import java.security.Security;
import javax.crypto.Cipher;

import com.alibaba.fastjson.JSONObject;

import java.util.Random;

/**
 * 加密工具类
 * @author User
 * @version
 */
public class EncryptUtil {
    private static String mstr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", "test");
        json.put("password", md5("123456"));
        json.put("eisuconfigip", "10.72.7.86");
        json.put("eisuconfigmac", "04-d4-37-04-0d-c9");
        json.put("fsuid", "10.72.7.87");
        json.put("fsumac", "05-d4-37-04-0d-c9");
        json.put("time", System.currentTimeMillis());
        try {
            //System.out.println(System.currentTimeMillis());
            //String test = "{code:1,time:1234124312431,sessionid=\"9502c6e96dc51a4b1edc0b6407676\"}";
            String key = "ZNV!@omc"; //密钥只支持8位字符串
            System.out.println("加密前的字符：" + json.toJSONString());
            String dst = encrypt(json.toJSONString(), key);
            System.out.println("加密后的字符：" + dst);
            System.out.println("解密后的字符：" + decrypt(dst, key));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * MD5加密
     * @param source
     * @return
     */
    public static final String md5(String source) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = source.getBytes();
            // 使用MD5创建MessageDigest对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            // MD5 的计算结果是一个 128 位的长整数，用字节表示就是 16 个字节
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            // 从第一个字节开始，对 MD5的每一个字节转换成 16进制字符的转换
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                // 将没个数(int)b进行双字节加密
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 字符串加密
     * @param str 待加密的字符串
     * @return 加密后的字符串
     */
    public static String encode(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        byte[] buff = str.getBytes();
        int j, k, m;
        int len = mstr.length();
        StringBuilder sb = new StringBuilder();
        Random r = new Random();

        for (int i = 0; i < buff.length; i++) {
            j = (byte) r.nextInt(6);
            buff[i] = (byte) ((int) buff[i] ^ j);
            k = (int) buff[i] % len;
            m = (int) buff[i] / len;
            m = m * 8 + j;
            sb.append(mstr.substring(k, k + 1) + mstr.substring(m, m + 1));
        }

        return sb.toString();
    }

    /**
     * 字符串解密
     * @param str 待解密的字符串
     * @return 解密后的字符串
     */
    public static String decode(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        int j, k, m, n = 0;
        int len = mstr.length();
        byte[] buff = new byte[str.length() / 2];

        for (int i = 0; i < str.length(); i += 2) {
            k = mstr.indexOf(str.charAt(i));
            m = mstr.indexOf(str.charAt(i + 1));
            j = m / 8;
            m = m - j * 8;
            buff[n] = (byte) (j * len + k);
            buff[n] = (byte) ((int) buff[n] ^ m);
            n++;
        }
        return new String(buff);
    }

    public static String endeCode(String sPassword) {
        String sReturn = "";
        char[] pChar = sPassword.toCharArray();
        int nIndex = 0;

        for (nIndex = 0; nIndex < sPassword.length(); nIndex++) {
            pChar[nIndex] ^= (char) 7;
        }

        for (nIndex = 0; nIndex < sPassword.length(); nIndex++) {
            sReturn += pChar[nIndex];
        }

        return sReturn;
    }

    public static String md5Encode(String sPassword) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        char[] charArray = sPassword.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }

        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();

        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     * @param arrB 需要转换的byte数组
     * @return 转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    private static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     * @param strIn 需要转换的字符串
     * @return 转换后的byte数组
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>
     */
    private static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 加密字节数组
     * @param bytes 需加密的字节数组
     * @param key 加密密钥 注意：密钥长度不得超过8位!!!
     * @return 加密后的字节数组
     * @throws Exception
     */
    @SuppressWarnings("restriction")
    public static byte[] encrypt(byte[] bytes, String key) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, getKey(key.getBytes()));
        return cipher.doFinal(bytes);
    }

    /**
     * 加密字符串
     * @param source 需加密的字符串
     * @param key 加密密钥 注意：密钥长度不得超过8位!!!
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String encrypt(String source, String key) throws Exception {
        return byteArr2HexStr(encrypt(source.getBytes(), key));
    }

    /**
     * 解密字节数组
     * @param bytes 需解密的字节数组
     * @param key 解密密码 注意：密钥长度不得超过8位!!!
     * @return 解密后的字节数组
     * @throws Exception
     */
    @SuppressWarnings("restriction")
    public static byte[] decrypt(byte[] bytes, String key) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, getKey(key.getBytes()));
        return cipher.doFinal(bytes);
    }

    /**
     * 解密字符串
     * @param source 需解密的字符串
     * @param key
     * @return 解密后的字符串
     * @throws Exception
     */
    public static String decrypt(String source, String key) throws Exception {
        return new String(decrypt(hexStr2ByteArr(source), key));
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     * @param arrBTmp 构成该字符串的字节数组
     * @return 生成的密钥
     * @throws java.lang.Exception
     */
    private static Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }
}
