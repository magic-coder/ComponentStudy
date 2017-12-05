package com.richfit.scandemo.scancore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * 默认的扫描实现类，该实现使用在青海，西南管道，
 * 长庆，庆阳，大连，煤层气等地区公司。也就是如果
 * 扫描能够使用广播的形式给出扫描data那么都可使用
 * 该service适配，否者需要自己实现一个扫描服务。
 * Created by monday on 2017/11/23.
 */

public class DefaultScanService implements IScanService {
    // 解码广播
    private final static String RECE_DATA_ACTION = "com.se4500.onDecodeComplete";
    // 调用扫描广播
    private final static String START_SCAN_ACTION = "com.geomobile.se4500barcode";
    // 停止
    private final static String STOP_SCAN_ACTION = "com.geomobile.se4500barcodestop";

    // 瑞飞手持
    private static final String WZ_RECT_DATA_ACTION = "com.android.scancontext";
    private static final String WZ_START_SCAN_ACTION = "android.intent.action.FUNCTION_BUTTON_DOWN";
    private static final String WZ_STOP_SCAN_ACTION = "android.intent.action.FUNCTION_BUTTON_UP";

    // 宿主上下文
    private WeakReference<Context> mContext;

    // 开始扫描标识
    private static boolean isStartScan = false;

    @Override
    public void initScanService(Context context) {
        this.mContext = new WeakReference<>(context);
        // 注册系统广播 接受扫描到的数据
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(WZ_RECT_DATA_ACTION);
        iFilter.addAction(RECE_DATA_ACTION);
        context.registerReceiver(receiver, iFilter);
    }

    @Override
    public void openScanService() {

    }

    @Override
    public void closeScanService() {

    }

    @Override
    public void destroyScanService() {
        Context context = mContext.get();
        if (context == null)
            return;
        context.unregisterReceiver(receiver);
        mContext = null;
        receiver = null;
    }

    @Override
    public void startScan() {
        if (!isStartScan) {
            isStartScan = true;
            Context context = mContext.get();
            if (context == null)
                return;
            Intent intent = new Intent();
            intent.setAction(START_SCAN_ACTION);
            intent.setAction(WZ_START_SCAN_ACTION);
            context.sendBroadcast(intent, null);
        }
    }

    @Override
    public void stopScan() {
        isStartScan = false;
        Context context = mContext.get();
        if (context == null)
            return;
        Intent intent = new Intent();
        intent.setAction(STOP_SCAN_ACTION);
        intent.setAction(WZ_STOP_SCAN_ACTION);
        context.sendBroadcast(intent);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context,
                              Intent intent) {
            String action = intent.getAction();
            Log.e("yff","action = " + action);
            if (action.equals(WZ_RECT_DATA_ACTION)) {
                String data = intent.getStringExtra("Scan_context");
                Log.e("yff","data = " + data);
            } else if (action.equals(RECE_DATA_ACTION)) {
                String data = intent.getStringExtra("se4500");
                data = data.replace("\n\r", "");
                data = data.replace("\n", "");
                data = data.replace("\r", "");
                data = data.replace("\u0000", "");
                Log.e("yff","data = " + data);
            }
        }
    };

}
