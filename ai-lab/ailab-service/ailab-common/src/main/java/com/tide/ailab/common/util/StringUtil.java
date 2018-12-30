package com.tide.ailab.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tide.ailab.common.log.Logger;


/**
 * String Util
 * @author User
 */
public class StringUtil {

    /**
     * 格式化输出字符串
     * @param format
     * @param args
     * @return
     */
    public static String format(String format, Object... args) {
        return String.format(format, args);
    }

    /**
     * is null or its length is 0 or it is made by space
     *
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 判断是否为空或空字符串
     * @param source
     * @return
     */
    public static boolean isNullOrEmpty(String source) {
        return (source == null || source.length() == 0);
    }

    /**
     * 判断是否为空或者空白字符串
     * @param source
     * @return
     */
    public static boolean isNullOrWhiteSpace(String source) {
        return (source == null || source.trim().length() == 0);
    }

    /**
     * 判断是否为空字符串
     * @param source
     * @return
     */
    public static boolean isEmpty(String source) {
        return (source != null && source.length() == 0);
    }

    /**
     * 判断是否为空白字符串
     * @param source
     * @return
     */
    public static boolean isWhiteSpace(String source) {
        return (source != null && source.trim().length() == 0 && source.length() > 0);
    }

    /**
     * 判断字符串是否包含非法字符
     * @param str
     * @return
     */
    public static boolean existIllegalCharacter(String str) {
        Pattern p = Pattern.compile("[a-zA-Z0-9_-]+");   // a~z、A~Z、0~9、下划线、横线
        Matcher m = p.matcher(str);
        return !m.matches();
    }

    /**
     * compare two string
     * @param actual
     * @param expected
     * @return
     * @see ObjectUtil#isEquals(Object, Object)
     */
    public static boolean isEquals(String actual, String expected) {
        return ObjectUtil.isEquals(actual, expected);
    }

    /**
     * null string to empty string
     *
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(String str) {
        return (str == null ? "" : str);
    }

    /**
     * capitalize first letter
     *
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str
                : new StringBuilder(str.length()).append(Character.toUpperCase(c)).append(str.substring(1))
                        .toString();
    }

    /**
     * encoded in utf-8
     *
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     *
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     *
     * @param href
     * @return
     *         <ul>
     *         <li>if href is null, return ""</li>
     *         <li>if not match regx, return source</li>
     *         <li>return the last string that match regx</li>
     *         </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * process special char in html
     *
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3"mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;"mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtil.isEmpty(source) ? source
                : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&")
                        .replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     *
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char) (source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     *
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char) 12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char) (source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    public static String encodeHTML(String source) {
        StringBuffer sb = new StringBuffer();
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char ch = source.charAt(i);
            if (ch == '<') {
                sb.append("&lt;");
            } else if (ch == '>') {
                sb.append("&gt;");
            } else if (ch == '"') {
                sb.append("&quot;");
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    /**
     * 在不足指定长度字符串的左边填充指定字符.
     * @param source
     * @param pad
     * @param length
     * @return
     */
    public static String padLeft(String source, char pad, int length) {
        int slen = source.length();
        if (slen < length) {
            byte[] bs = new byte[length];
            byte[] ss = source.getBytes();
            Arrays.fill(bs, (byte) (pad & 0xff));
            System.arraycopy(ss, 0, bs, length - ss.length, ss.length);
            return new String(bs);
        } else {
            return source.substring(slen - length);
        }
    }

    /**
     * 在不足指定长度字符串的右边填充指定字符.
     * @param source
     * @param pad
     * @param length
     * @return
     */
    public static String padRight(String source, char pad, int length) {
        int slen = source.length();
        if (slen < length) {
            byte[] bs = new byte[length];
            byte[] ss = source.getBytes();
            Arrays.fill(bs, (byte) (pad & 0xff));
            System.arraycopy(ss, 0, bs, 0, ss.length);
            return new String(bs);
        } else {
            return source.substring(0, length);
        }
    }

    /**
     * 处理传到页面上的字符串，对特殊字符进行转义.
     * @author Young
     * @param source
     * @return
     */
    public static String exscapeCharacter(String source) {
        // source = source.replace("&", "&amp;");
        source = source.replace("<", "&lt;");
        source = source.replace(">", "&gt;");
        source = source.replace("\"", "&quot;");
        // source = source.replace("\'", "&apos;");
        return source;
    }

    public static String jsToHTML(String source) {
        if (isNullOrEmpty(source)) {
            return "";
        }
        source = source.replace("&", "&amp;");
        source = source.replace("\"", "&quot;");
        source = source.replace("<", "&lt;");
        source = source.replace(">", "&gt;");
        // source = source.replace("'", "&#39;");
        source = source.replace("\\", "\\\\");
        return source;
    }

    public static String htmlToJs(String source) {
        if (isNullOrEmpty(source)) {
            return "";
        }
        source = source.replace("&amp;", "&");
        source = source.replace("&quot;", "\"");
        source = source.replace("&lt;", "<");
        source = source.replace("&gt;", ">");
        // source = source.replace("&#39;", "'");
        // source = source.replace("\\\\", "\\");
        return source;
    }

    public static String toJson(String source) {
        source = source.replace("'", "\\'");
        source = source.replace("\"", "\\\"");
        source = source.replace("\r\n", "\\uoood\\uoooa");
        source = source.replace("\n", "\\uoooa");
        return source;
    }

    public static String join(Object obj[], String splitString) {
        String s = "";
        for (Object o : obj) {
            s += splitString + o.toString();
        }
        if (s.length() > 0) {
            s = s.substring(1);
        }
        return s;
    }

    public static String subString(String str, int maxLength) {
        if (isNullOrEmpty(str)) {
            return str;
        }
        if (str.length() > maxLength) {
            return str.substring(0, maxLength);
        } else {
            return str;
        }
    }

    /**
     * 将字符串进行字节截取
     * @param str
     * @param len
     * @return
     */
    public static String subStringByByte(String str, int len) {
        String result = null;
        if (str != null) {
            byte[] a = str.getBytes();
            if (a.length <= len) {
                result = str;
            } else if (len > 0) {
                result = new String(a, 0, len);
                int length = result.length();
                if (str.charAt(length - 1) != result.charAt(length - 1)) {
                    if (length < 2) {
                        result = null;
                    } else {
                        result = result.substring(0, length - 1);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 模糊查询时，转义%和_通配符
     * @param source
     * @return
     */
    public static String escapeLike(String source) {
        if (isNullOrEmpty(source)) {
            return source;
        }
        return source.replace("%", "\\%").replace("_", "\\_");
    }

    /**
     * 模糊查询时，转义正则表达式元素字符
     * @param source
     * @return
     */
    public static String escapeRegexp(String source) {
        if (isNullOrEmpty(source)) {
            return source;
        }
        String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
        for (String key : fbsArr) {
            if (source.contains(key)) {
                source = source.replace(key, "\\" + key);
            }
        }
        return source;
    }

    public static String byte2String(byte[] buff) {
        StringBuffer sbuf = new StringBuffer();
        for (int i = 0; i < buff.length; i++) {
            int tmp = buff[i] & 0XFF;
            String str = Integer.toHexString(tmp);
            if (str.length() == 1) {
                sbuf.append("0" + str);
            } else {
                sbuf.append(str);
            }

        }
        return sbuf.toString();
    }

    public static byte[] string2byte(String str) {
        byte[] result = new byte[str.length() / 2];
        int index = 0;
        for (int i = 0; i < str.length(); i += 2) {
            result[index++] = (byte) Integer.parseInt(str.substring(i, i + 2), 16);
        }
        return result;
    }

    public static void main(String[] args) {
        int s[] = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
        Logger.d(s);
    }
}
