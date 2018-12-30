package com.tide.ailab.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;

import com.tide.ailab.common.log.Logger;

/**
 * 日期时间处理公共类.
 * 
 * @author User
 */
public class DateTimeUtil {

    private static ResourceBundle resource = null;
    private static final String MONTHFORMATTPL = "yyyyMM";
    private static final String DATEFORMATTPL = "yyyy-MM-dd";
    private static final String DATETIMEFORMATTPL = "yyyy-MM-dd HH:mm:ss";
    private static final String DATETIMEMSFORMATTPL = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String TIMESIMPLEFORMATTPL = "HH:mm";
    private static final String TIMEFORMATTPL = "HH:mm:ss";
    private static final SimpleDateFormat MONTHFORMAT = new SimpleDateFormat(MONTHFORMATTPL);
    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat(DATEFORMATTPL);
    private static final SimpleDateFormat DATETIMEFORMAT = new SimpleDateFormat(DATETIMEFORMATTPL);
    private static final SimpleDateFormat DATETIMEMSFORMAT = new SimpleDateFormat(DATETIMEMSFORMATTPL);
    private static final SimpleDateFormat TIMESIMPLEFORMAT = new SimpleDateFormat(TIMESIMPLEFORMATTPL);
    private static final SimpleDateFormat TIMEFORMAT = new SimpleDateFormat(TIMEFORMATTPL);

    static {
        try {
            resource = ResourceBundle.getBundle("global");
        } catch (Exception e) {
        }
    }

    /**
     * 日期格式。
     * 
     * @return 日期格式
     */
    public static String getDatePattern() {
        return DATEFORMATTPL;
    }

    /**
     * 时间格式。
     * 
     * @return 时间格式
     */
    public static String getTimePattern() {
        return TIMEFORMATTPL;
    }

    /**
     * 简单时间格式。
     * 
     * @return 简单时间格式
     */
    public static String getSimpleTimePattern() {
        return TIMESIMPLEFORMATTPL;
    }

    /**
     * 日期时间格式。
     * 
     * @return 时间日期格式
     */
    public static String getDateTimePattern() {
        return DATETIMEFORMATTPL;
    }

    /**
     * 获取当前时间字符串
     * 
     * @return
     */
    public static String getDateTimeString() {
        return getDateTimeString(false);
    }

    /**
     * 获取当前日期字符串
     * 
     * @return
     */
    public static String getDateString() {
        return getDateString(new Date());
    }

    /**
     * 获取当前日期字符串
     * 
     * @param date
     * @return
     */
    public static String getDateString(Date date) {
        return DATEFORMAT.format(date);
    }

    /**
     * 获取当前时间字符串
     * 
     * @return
     */
    public static String getTimeString() {
        return getTimeString(new Date());
    }

    /**
     * 获取当前时间字符串
     * 
     * @param date
     * @return
     */
    public static String getTimeString(Date date) {
        return TIMEFORMAT.format(date);
    }

    /**
     * 获取当前时间字符串
     * 
     * @return
     */
    public static String getSimpleTimeString() {
        return getSimpleTimeString(new Date());
    }

    /**
     * 获取当前时间字符串
     * 
     * @param date
     * @return
     */
    public static String getSimpleTimeString(Date date) {
        return TIMESIMPLEFORMAT.format(date);
    }

    /**
     * 获取当前时间字符串
     * 
     * @param withMillisecond
     * @return
     */
    public static String getDateTimeString(boolean withMillisecond) {
        return getDateTimeString(new Date(), withMillisecond);
    }

    /**
     * 获取当前时间字符串
     * 
     * @param date
     * @return
     */
    public static String getDateTimeString(Date date) {
        return getDateTimeString(date, false);
    }

    /**
     * 获取当前时间字符串
     * 
     * @param date
     * @param withMillisecond
     * @return
     */
    public static String getDateTimeString(Date date, boolean withMillisecond) {
        return withMillisecond ? DATETIMEMSFORMAT.format(date) : DATETIMEFORMAT.format(date);
    }

    /**
     * 日期转为字符串.
     * 
     * @param date 要格式化的日期
     * @return 日期字符串
     */
    public static String format(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(getDateTimePattern());
        return sdf.format(date);
    }

    /**
     * 字符串转换为日期时间.
     * 
     * @param source 日期字符串
     * @return 日期
     */
    public static Date parse(String source) {
        if (source == null) {
            return null;
        }
        Date rtn = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(getDateTimePattern());
            rtn = sdf.parse(source);
        } catch (Exception e) {
            Logger.e(e);
        }
        return rtn;
    }

    /**
     * 获得WEB服务器UTC时间。
     * 
     * @return 返回WEB服务器UTC时间
     */
    public static Date getWebServerUTCDate() {
        Calendar calendar = new GregorianCalendar();
        TimeZone zone = calendar.getTimeZone();
        Date date = getUTCDate(calendar.getTime(), zone);
        return date;
    }

    /**
     * 获得UTC时间.
     * 
     * @param date 本地时间
     * @param zone 本地所在时区
     * @return UTC时间
     */
    public static Date getUTCDate(Date date, TimeZone zone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(zone);
        calendar.add(Calendar.MILLISECOND, -zone.getRawOffset());
        return calendar.getTime();
    }

    /**
     * 获得本地时间.
     * 
     * @param date UTC时间
     * @param zone 本地所在时区
     * @return 本地时间
     */
    public static Date getLocalDate(Date date, TimeZone zone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(zone);
        calendar.add(Calendar.MILLISECOND, zone.getRawOffset());
        return calendar.getTime();
    }

    /**
     * 字符串转换为时间
     * 
     * @param source
     * @return
     */
    public static Date getDate(String source) {
        return getDate(source, false);
    }

    /**
     * 获得当前时间的日期
     * 
     * @return Date 不包括time，只有日期
     */
    public static Date getCurDate() {
        String curDateStr = getDateString();
        Date curDate = parse(curDateStr + " 00:00:00");
        return curDate;
    }

    /**
     * 获取昨天的日期
     * 
     * @return
     */
    public static Date getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1); // 得到前一天
        Date date = calendar.getTime();
        String datestring = getDateString(date);
        Date yesterdayDate = parse(datestring + " 00:00:00");
        return yesterdayDate;
    }

    /**
     * 字符串转换为时间
     * 
     * @param source
     * @param withMillisecond
     * @return
     */
    public static Date getDate(String source, boolean withMillisecond) {
        try {
            return withMillisecond ? DATETIMEMSFORMAT.parse(source) : DATETIMEFORMAT.parse(source);
        } catch (ParseException e) {
            Logger.e(e);
        }
        return null;
    }

    /**
     * 字符串转换为时间
     * 
     * @param source
     * @return
     */
    public static Date getDate(String source, String pattern) {
        try {
            return DATETIMEMSFORMAT.parse(source);
        } catch (ParseException e) {
            Logger.e(e);
        }
        return null;
    }

    /**
     * @param date 要格式话的日期 yyyy-MM-dd HH:mm:ss
     * @return 今天 HH:mm:ss or 昨天 HH:mm:ss or MM-dd HH:mm:ss
     * @throws ParseException
     */
    public static String convertDateFormat(String date) throws ParseException {
        SimpleDateFormat allformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat datetimeformat = new SimpleDateFormat("MM-dd HH:mm:ss");

        Date myDate = allformat.parse(date);
        Calendar cal = Calendar.getInstance();
        String dateNow = dateformat.format(cal.getTime());
        cal.add(Calendar.DATE, -1);
        String dateYesterday = dateformat.format(cal.getTime());

        if (dateformat.format(myDate).equals(dateNow)) {
            return resource.getString("today") + " " + timeformat.format(myDate);
        } else if (dateformat.format(myDate).equals(dateYesterday)) {
            return resource.getString("yesterday") + " " + timeformat.format(myDate);
        } else {
            return datetimeformat.format(myDate);
        }
    }

    /**
     * 两个日期相隔的天数
     * 
     * @param c1 :起始日期
     * @param c2 :结束日期
     * @return c2-c1相隔的天数
     */
    public static double daysBetween(Calendar c1, Calendar c2) {
        // 将时分秒都置0
        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH),
            c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE), c1.get(Calendar.SECOND));

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.set(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH),
            c2.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.MINUTE), c2.get(Calendar.SECOND));

        long time1 = startTime.getTimeInMillis();
        long time2 = endTime.getTimeInMillis();
        double betweenDays = (time2 - time1) / (1000 * 3600 * 24 + 0d);
        return betweenDays;
    }

    /**
     * 两个日期相隔的天数
     * 
     * @param beginDate :起始日期
     * @param endDate :结束日期
     * @return beginDate-endDate相隔的天数
     */
    public static int daysBetween(Date beginDate, Date endDate) {
        Calendar startTime = Calendar.getInstance();
        startTime.setTime(clearTime(beginDate));

        Calendar endTime = Calendar.getInstance();
        endTime.setTime(clearTime(endDate));

        long time1 = startTime.getTimeInMillis();
        long time2 = endTime.getTimeInMillis();
        long betweenDays = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(betweenDays));
    }

    /**
     * 清空日期的时分秒
     * 
     * @param time
     * @return 返回时间为00:00:00的日期对象
     */
    public static Date clearTime(Date time) {
        Date date = null;
        if (null == time) {
            return date;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(getDatePattern());
            date = sdf.parse(sdf.format(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 两个日期相隔的秒数
     * 
     * @param c1 起始日期
     * @param c2 结束日期
     * @return c2- c1相隔的秒数
     */
    public static long sencondsBetween(Calendar c1, Calendar c2) {
        Calendar startTime = Calendar.getInstance();
        startTime.clear();
        startTime.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH),
            c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE), c1.get(Calendar.SECOND));

        Calendar endTime = Calendar.getInstance();
        endTime.clear();
        endTime.set(c2.get(Calendar.YEAR), c2.get(Calendar.MONTH), c2.get(Calendar.DAY_OF_MONTH),
            c2.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.MINUTE), c2.get(Calendar.SECOND));

        long time1 = startTime.getTimeInMillis();
        long time2 = endTime.getTimeInMillis();
        long betweenSeconds = (time2 - time1) / 1000L;
        return betweenSeconds;
    }

    /**
     * 复制一个calendar 避免调用add的时候原始对象产生副作用
     * 
     * @return
     */
    public static Calendar cloneCalendar(Calendar cal) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
        c.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));
        return c;
    }

    /**
     * 获取当前系统时间，带时分秒
     * 
     * @return
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 根据日期和时间进行拼接
     * 
     * @param date 格式 yyyy-MM-dd
     * @param time 格式 hh:mm
     * @return
     */
    public static Date concatDateTime(Date date, String time) {
        return parse(getDateString(date) + " " + time + ":00");
    }

    /**
     * 获取到月份的时间：yyyyMM
     * 
     * @return
     */
    public static String getMonthDateString() {
        return MONTHFORMAT.format(new Date());
    }

    /**
     * 获取当前的月份时间
     * 
     * @return
     */
    public static Date getCurMonthDate() {
        Date rtn = null;
        try {
            String curDate = getMonthDateString();
            rtn = MONTHFORMAT.parse(curDate);
        } catch (Exception e) {
            Logger.e(e);
        }
        return rtn;
    }

    public static boolean isFirstDayOfMonth() {
        boolean isFirstDay = false;
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        if (today == 1) {
            isFirstDay = true;
        }
        return isFirstDay;
    }

    public static String getBeforeMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1); // 得到前一个月
        Date beforMonthDate = calendar.getTime(); // 结果
        System.out.println(MONTHFORMAT.format(beforMonthDate));
        return MONTHFORMAT.format(beforMonthDate);
    }

    /**
     * 获取一年内时间字符串列表(时间格式：yyyyMM)
     * 
     * @return
     */
    public static List<String> getYearDateList() {
        List<String> yearDates = new ArrayList<String>();
        Date startDate = getCurMonthDate();
        Calendar dd = Calendar.getInstance(); // 定义日期实例
        dd.setTime(startDate); // 设置日期起始时间
        for (int i = 0; i < 12; i++) {
            String str = MONTHFORMAT.format(dd.getTime());
            yearDates.add(str);
            dd.add(Calendar.MONTH, 1); // 进行当前日期月份加1
        }
        return yearDates;
    }

    public static boolean isValidDate(String str, String pattern) {
        boolean convertSuccess = true;
        // 指定时间格式由外部传入
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证时间，
            // 比如 1）2017/02/29会被接受，并转换成2017/03/01
            // 2）2017-07-01 66:66:66会被接受，并转换成2017-07-03 19:07:06
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }
    
    public static Date utcToLocal(Date utlTime) {
    	DATETIMEFORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    	String utlTimeStr = DATETIMEFORMAT.format(utlTime);
    	
    	System.out.println(TimeZone.getDefault());
    	DATETIMEFORMAT.setTimeZone(TimeZone.getDefault());
        Date locatlDate = null;
        try {
            locatlDate = DATETIMEFORMAT.parse(utlTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return locatlDate;
    }
    
    public static void main(String[] args) {
    	Date date = DateTimeUtil.parse("2018-12-03 13:15:00");
    	Date date2 = DateTimeUtil.getLocalDate(date, TimeZone.getDefault());
    	
    	System.out.println(DateTimeUtil.format(date2));
    	
    }
}
