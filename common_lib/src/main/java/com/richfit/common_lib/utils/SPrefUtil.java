package com.richfit.common_lib.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * sharedPreference工具类
 * Created by monday on 2016/9/30.
 */

public class SPrefUtil {

    public static final String PREF_NAME = "bcs_config";

    public static SharedPreferences sSPref;

    public static void initSharePreference(Context context) {
        if (sSPref != null)
            return;
        if (sSPref == null) {
            sSPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    private SPrefUtil() {
    }

    /**
     * 保存数据
     *
     * @param key
     * @param data
     */
    public static void saveData(String key, Object data) {
        if (sSPref == null)
            return;
        String type = data.getClass().getSimpleName();
        SharedPreferences.Editor editor = sSPref.edit();

        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        editor.apply();
    }

    /**
     * 读数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public static Object getData(String key, Object defValue) {
        if (sSPref == null)
            return null;
        String type = defValue.getClass().getSimpleName();
        if ("Integer".equals(type)) {
            return sSPref.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return sSPref.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return sSPref.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return sSPref.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return sSPref.getLong(key, (Long) defValue);
        }
        return null;
    }

}
