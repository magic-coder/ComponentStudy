package com.richfit.scandemo.scancore;

/**
 * Created by monday on 2017/11/23.
 */

public class ScanServiceFactory {


    public static IScanService getScanService() {
        String appName = "r2000";
        switch (appName) {
            case "qhyt":
                return new DefaultScanService();
            case "r2000":
                return new R200ScanService();
            default:
                return new DefaultScanService();
        }
    }
}
