package com.richfit.common_lib.lib_mvp;

import android.app.Application;

import com.richfit.common_lib.lib_crash.CrashManager;
import com.richfit.common_lib.lib_crash.HttpCrashReport;
import com.richfit.common_lib.utils.SPrefUtil;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by monday on 2017/6/9.
 */

public abstract class BaseApplication extends Application {

    protected static RefWatcher mRefWatcher;
    private static Application app;
    public static String baseUrl;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化自己的全局配置
        app = this;
        initSPrefUtils();
        initCrashManage();
    }

    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

    public static Application getAppContext() {
        return app;
    }

    private void initSPrefUtils() {
        SPrefUtil.initSharePreference(getAppContext());
    }

    private void initCrashManage() {
        CrashManager crashManager = CrashManager.getInstance();
        HttpCrashReport httpCrashReport = new HttpCrashReport();
        crashManager.init(getAppContext(), httpCrashReport);
    }
}
