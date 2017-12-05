package com.richfit.barcodesystemproduct;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.richfit.common_lib.lib_crash.CrashManager;
import com.richfit.common_lib.lib_crash.HttpCrashReport;
import com.richfit.common_lib.lib_mvp.BaseApplication;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.repository.DataInjection;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;


/**
 * Created by monday on 2017/3/17.
 */

public class BarcodeSystemApplication extends BaseApplication {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Http的BaseUrl
        baseUrl = generateBaseUrl();
        //初始化全局模块
        DataInjection.getRepository(this, baseUrl);
        //开启日志处理模块
        if (!BuildConfig.DEBUG) {
            initCrashManage(this);
        }
    }

    private String generateBaseUrl() {
        String baseUrl = null;
        baseUrl = (String) SPrefUtil.getData("base_url", "");
        if (!TextUtils.isEmpty(baseUrl)) {
            //说明用户手动设置过Url
            return baseUrl;
        }

        //如果没有手动设置过Url
        if (BuildConfig.DEBUG) {
            switch (BuildConfig.APP_NAME) {
                case Global.QYSH:
                    //庆阳测试地址
                    baseUrl = "http://11.11.177.100:9087/lhbk_middleware/MobileProcess/";
                    break;
                case Global.QHYT:
                    //青海油田培训系统
                    baseUrl = "http://10.82.53.52:8080/ktbk_middleware/MobileProcess/";
                    break;
                case Global.XNGD:
                    //西南管道
                    baseUrl = "http://11.11.47.29:8085/gdbk_middleware/MobileProcess/";
                    break;
                case Global.CQYT:
                    //长庆油田
                    baseUrl = "http://11.11.177.98:9087/ktbk_middleware/MobileProcess/";
                    break;
                case Global.DLSH:
                    //大连石化
                    baseUrl = "http://10.82.53.52:8080/ktbk_middleware/MobileProcess/";
                    break;
                   //煤层气
                case Global.MCQ:
                    baseUrl = "http://11.11.177.98:9087/ktbk_middleware/MobileProcess/";
//                    baseUrl = "http://10.88.53.5:8080/gdbk_middleware/MobileProcess/";
                    break;
                    //韩城
                case Global.HANC:
                    baseUrl = "http://11.11.177.98:9087/ktbk_middleware/MobileProcess/";
                    break;
                    //川庆
                case Global.CQZT:
                    baseUrl = "http://11.11.136.206:9087/gcjs_middleware/MobileProcess/";
                    break;
            }
        } else {
            //正式地址
            baseUrl = BuildConfig.SERVER_URL;
        }

        return baseUrl;
    }

    /**
     * 初始化Crash日志收集
     */
    private void initCrashManage(Application application) {
        CrashManager crashManager = CrashManager.getInstance();
        HttpCrashReport httpCrashReport = new HttpCrashReport();
        crashManager.init(application, httpCrashReport);
    }


}
