package com.richfit.barcodesystemproduct.scanservice;

import android.util.Log;

import com.richfit.barcodesystemproduct.BuildConfig;

/**
 * Created by monday on 2017/11/23.
 */

public class ScanServiceFactory {

    public static IScanService getScanService() {
        Log.d("yff","获取条码扫描服务;appName:"+BuildConfig.APP_NAME);
        switch (BuildConfig.APP_NAME) {
            case "cqzt":
                return new CQZTScanService();
            default:
                return new DefaultScanService();
        }
    }
}
