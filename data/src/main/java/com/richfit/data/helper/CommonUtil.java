package com.richfit.data.helper;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.richfit.domain.bean.SimpleEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import okhttp3.Request;
import okio.Buffer;
import okio.ByteString;

/**
 * Created by monday on 2016/12/30.
 */

public class CommonUtil {

    private final static int LENGTH = 11;

    private CommonUtil() {

    }

    // 转化十六进制编码为字符串
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            s = new String(baKeyword, "gb2312");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static String toHexString(String ss) {
        String str = null;
        try {
            byte[] tbyte = ss.getBytes("GB2312");
            String s = new String(tbyte, "ISO-8859-1");
            str = "";
            for (int i = 0; i < s.length(); i++) {
                int ch = (int) s.charAt(i);
                String s4 = Integer.toHexString(ch);
                str = str + s4;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ss;
        }
        return str.toUpperCase();
    }

    public static String toUpperCase(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        return str.toUpperCase();
    }

    public static String Obj2String(Object object) {
        if (object == null) {
            return "";
        }
        return object.toString().trim();
    }

    public static void putAll(Map<String, Object> originMap, Map<String, Object> targetMap) {
        if (originMap != null && targetMap != null) {
            originMap.putAll(targetMap);
        }
    }

    public final static int convertToInt(Object value, int defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }

    public final static int convertToInt(Integer value) {
        if (value == null) {
            return -1;
        }
        return value.intValue();
    }

    public final static float convertToFloat(Object value, float defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Float.valueOf(value.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(value.toString()).intValue();
            } catch (Exception e1) {
                return defaultValue;
            }
        }
    }


    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 根据Request生成哈希值
     *
     * @param request
     * @return
     */
    public static String getHash(Request request) {
        StringBuilder str = new StringBuilder();
        str.append('[');
        str.append(request.method());
        str.append(']');
        str.append('[');
        str.append(request.url());
        str.append(']');

        try {
            Buffer buffer = new Buffer();
            request.body().writeTo(buffer);
            str.append(buffer.readByteString().sha1().hex());
        } catch (IOException e) {
            return "";
        }

        str.append('-');
        str.append(ByteString.of(request.headers().toString().getBytes()).sha1().hex());

        return str.toString();
    }


    /**
     * 获取当前的日期
     *
     * @param pattern
     * @return
     */
    public static String getCurrentDate(String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        String date = df.format(new Date());
        return date;
    }

    public static long getSystemDate() {
        return System.currentTimeMillis();
    }

    /**
     * 生成随机的32id
     */
    public static String getUUID() {
        UUID id = UUID.randomUUID();
        String uuid = id.toString();
        uuid = uuid.replace("-", "");
        return uuid.toUpperCase();
    }

    /**
     * 把毫秒转化成日期
     *
     * @param dateFormat(日期格式，例如：MM/ dd/yyyy HH:mm:ss)
     * @param millSec(毫秒数)
     * @return
     */
    public static String transferLongToDate(String dateFormat, Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = new Date(millSec);
        return sdf.format(date);
    }


    /**
     * 将yyyy/MM/dd->yyyy-MM-dd
     */
    public static String getDate(String oldDate) {
        if (TextUtils.isEmpty(oldDate)) {
            return oldDate;
        }
        String[] flag = oldDate.split("-");
        if (flag.length > 0 && flag.length == 3) {
            return oldDate;
        }
        SimpleDateFormat sdfx = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfh = new SimpleDateFormat("yyyy-MM-dd");

        String newDate = "";
        try {
            newDate = sdfh.format(sdfx.parse(oldDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDate;
    }

    /**
     * 比较时间。如果currentDate比0001/01/01要晚，那么返回true，否者返回false
     */
    public static boolean compareDate(String currentDate) {
        String myString = "0001/01/01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
        boolean flag = false;
        try {
            Date d = sdf.parse(myString);
            Date now = sdf.parse(currentDate);
            flag = d.before(now);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 将yyyy-MM-dd->yyyy/MM/dd
     */
    public static String getDate2(String oldDate) {
        if (TextUtils.isEmpty(oldDate)) {
            return oldDate;
        }
        String[] flag = oldDate.split("/");
        if (flag.length > 0 && flag.length == 3) {
            return oldDate;
        }
        SimpleDateFormat sdfh = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd");

        String newDate = "";
        try {
            newDate = sdfh.format(sdfx.parse(oldDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDate;
    }

    /**
     * 将字符串装换成Date对象。
     *
     * @param dateString：
     * @return
     */
    public static Date parseDate(final String dateString, String pattern) {
        if (TextUtils.isEmpty(dateString))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date date = sdf.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 得到几天后的时间
     *
     * @param d
     * @param day
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * md5加密
     *
     * @param key
     * @return
     */
    public static String MD5(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    /**
     * 获取当前apk的版本号
     */
    public static String getCurrentVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getCurrentVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }


    public static String getMacAddress() {
        String macSerial = null;
        String str = "";

        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }


    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 物资条码补零
     */
    public static String addZeros(String num) {
        int length = num.length();
        StringBuffer sb = new StringBuffer();
        if (length == LENGTH) {
            return num;
        }
        if (length < LENGTH) {
            for (int i = 0; i < LENGTH - length; i++) {
                sb.append("0");
            }
            sb.append(num);
        }
        return sb.toString();
    }


    public static List<String> toStringArray(List<SimpleEntity> list, boolean withName) {
        List<String> strs = new ArrayList<>();
        if (list == null || list.size() == 0)
            return strs;
        for (SimpleEntity simpleEntity : list) {
            if (withName) {
                strs.add(simpleEntity.code + "_" + simpleEntity.name);
            } else {
                strs.add(simpleEntity.code);
            }
        }
        return strs;
    }

}
