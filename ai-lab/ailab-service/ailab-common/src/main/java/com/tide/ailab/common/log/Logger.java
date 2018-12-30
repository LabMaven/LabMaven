package com.tide.ailab.common.log;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;

import com.tide.ailab.common.util.PathUtil;



/**
 * 通用日志类，PC下调试使用
 * @author User
 */
public class Logger {

    private static final int STACKINDEX = 1;

    static {
        String path = PathUtil.getLibPath();
        String fileName = String.format("%slog4j.properties", path);
        File file = new File(fileName);
        if (file.exists()) {
            PropertyConfigurator.configure(fileName);
        }
    }

    public static void d(Object o) {
        String callerFQNC = Thread.currentThread().getStackTrace()[STACKINDEX].getClassName();
        String clsName = Thread.currentThread().getStackTrace()[STACKINDEX + 1].getClassName();
        Throwable e = (o instanceof Throwable) ? (Throwable) o : null;
        String msg = (o == null ? (e == null ? "" : (e.getMessage() == null ? "" : e.getMessage()))
                : o.toString());
        org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.DEBUG, msg, e);
    }

    public static void d(Object o, Object... args) {
        String callerFQNC = Thread.currentThread().getStackTrace()[STACKINDEX].getClassName();
        String clsName = Thread.currentThread().getStackTrace()[STACKINDEX + 1].getClassName();
        if (args != null && args.length > 0) {
            Object obj = args[0];
            if (obj instanceof Throwable) {
                Throwable e = (Throwable) obj;
                org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.DEBUG, o, e);
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append(o);
                for (Object tmp : args) {
                    sb.append(", ");
                    sb.append(tmp);
                }
                org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.DEBUG, sb.toString(), null);
            }
        } else {
            Throwable e = (o instanceof Throwable) ? (Throwable) o : null;
            String msg = (o == null ? (e == null ? "" : (e.getMessage() == null ? "" : e.getMessage()))
                    : o.toString());
            org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.DEBUG, msg, e);
        }
    }

    public static void i(Object o) {
        String callerFQNC = Thread.currentThread().getStackTrace()[STACKINDEX].getClassName();
        String clsName = Thread.currentThread().getStackTrace()[STACKINDEX + 1].getClassName();
        Throwable e = (o instanceof Throwable) ? (Throwable) o : null;
        String msg = (o == null ? (e == null ? "" : (e.getMessage() == null ? "" : e.getMessage()))
                : o.toString());
        org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.INFO, msg, e);
    }

    public static void i(Object o, Object... args) {
        String callerFQNC = Thread.currentThread().getStackTrace()[STACKINDEX].getClassName();
        String clsName = Thread.currentThread().getStackTrace()[STACKINDEX + 1].getClassName();
        if (args != null && args.length > 0) {
            Object obj = args[0];
            if (obj instanceof Throwable) {
                Throwable e = (Throwable) obj;
                org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.INFO, o, e);
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append(o);
                for (Object tmp : args) {
                    sb.append(", ");
                    sb.append(tmp);
                }
                org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.INFO, sb.toString(), null);
            }
        } else {
            Throwable e = (o instanceof Throwable) ? (Throwable) o : null;
            String msg = (o == null ? (e == null ? "" : (e.getMessage() == null ? "" : e.getMessage()))
                    : o.toString());
            org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.INFO, msg, e);
        }
    }

    public static void e(Object o) {
        String callerFQNC = Thread.currentThread().getStackTrace()[STACKINDEX].getClassName();
        String clsName = Thread.currentThread().getStackTrace()[STACKINDEX + 1].getClassName();
        Throwable e = (o instanceof Throwable) ? (Throwable) o : null;
        String msg = (o == null ? (e == null ? "" : (e.getMessage() == null ? "" : e.getMessage()))
                : o.toString());
        org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.ERROR, msg, e);
    }

    public static void e(Object o, Object... args) {
        String callerFQNC = Thread.currentThread().getStackTrace()[STACKINDEX].getClassName();
        String clsName = Thread.currentThread().getStackTrace()[STACKINDEX + 1].getClassName();
        if (args != null && args.length > 0) {
            Object obj = args[0];
            if (obj instanceof Throwable) {
                Throwable e = (Throwable) obj;
                org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.ERROR, o, e);
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append(o);
                for (Object tmp : args) {
                    sb.append(", ");
                    sb.append(tmp);
                }
                org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.ERROR, sb.toString(), null);
            }
        } else {
            Throwable e = (o instanceof Throwable) ? (Throwable) o : null;
            String msg = (o == null ? (e == null ? "" : (e.getMessage() == null ? "" : e.getMessage()))
                    : o.toString());
            org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.ERROR, msg, e);
        }
    }

    public static void w(Object o) {
        String callerFQNC = Thread.currentThread().getStackTrace()[STACKINDEX].getClassName();
        String clsName = Thread.currentThread().getStackTrace()[STACKINDEX + 1].getClassName();
        Throwable e = (o instanceof Throwable) ? (Throwable) o : null;
        String msg = (o == null ? (e == null ? "" : (e.getMessage() == null ? "" : e.getMessage()))
                : o.toString());
        org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.WARN, msg, e);
    }

    public static void w(Object o, Object... args) {
        String callerFQNC = Thread.currentThread().getStackTrace()[STACKINDEX].getClassName();
        String clsName = Thread.currentThread().getStackTrace()[STACKINDEX + 1].getClassName();
        if (args != null && args.length > 0) {
            Object obj = args[0];
            if (obj instanceof Throwable) {
                Throwable e = (Throwable) obj;
                org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.WARN, o, e);
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append(o);
                for (Object tmp : args) {
                    sb.append(", ");
                    sb.append(tmp);
                }
                org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.WARN, sb.toString(), null);
            }
        } else {
            Throwable e = (o instanceof Throwable) ? (Throwable) o : null;
            String msg = (o == null ? (e == null ? "" : (e.getMessage() == null ? "" : e.getMessage()))
                    : o.toString());
            org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.WARN, msg, e);
        }
    }

    public static void fatal(Object o) {
        String callerFQNC = Thread.currentThread().getStackTrace()[STACKINDEX].getClassName();
        String clsName = Thread.currentThread().getStackTrace()[STACKINDEX + 1].getClassName();
        Throwable e = (o instanceof Throwable) ? (Throwable) o : null;
        String msg = (o == null ? (e == null ? "" : (e.getMessage() == null ? "" : e.getMessage()))
                : o.toString());
        org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.FATAL, msg, e);
    }

    public static void fatal(Object o, Object... args) {
        String callerFQNC = Thread.currentThread().getStackTrace()[STACKINDEX].getClassName();
        String clsName = Thread.currentThread().getStackTrace()[STACKINDEX + 1].getClassName();
        if (args != null && args.length > 0) {
            Object obj = args[0];
            if (obj instanceof Throwable) {
                Throwable e = (Throwable) obj;
                org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.FATAL, o, e);
            } else {
                StringBuffer sb = new StringBuffer();
                sb.append(o);
                for (Object tmp : args) {
                    sb.append(", ");
                    sb.append(tmp);
                }
                org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.FATAL, sb.toString(), null);
            }
        } else {
            Throwable e = (o instanceof Throwable) ? (Throwable) o : null;
            String msg = (o == null ? (e == null ? "" : (e.getMessage() == null ? "" : e.getMessage()))
                    : o.toString());
            org.apache.log4j.Logger.getLogger(clsName).log(callerFQNC, Level.FATAL, msg, e);
        }
    }

    public static int setLevel(String level) {
        return 0;
    }

    public static String getLevelToString() {
        return null;
    }
}
