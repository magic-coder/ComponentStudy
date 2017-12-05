package com.richfit.scandemo.scancore;

import android.content.Context;

/**
 * 扫描服务接口，该接口描述了实现一个扫描的所有方法。
 * 包括初始化扫描服务，启动扫描服务，关闭扫描服务
 * Created by monday on 2017/11/23.
 */

public interface IScanService {
    /**
     * 初始化扫描服务
     * @param context
     */
    void initScanService(Context context);
    //onResume
    void openScanService();

    //onPause,onStop
    void closeScanService();

	//onDestroy	
    void destroyScanService();

    //keyDown
    void startScan();

	//keyUp
    void stopScan();
}
