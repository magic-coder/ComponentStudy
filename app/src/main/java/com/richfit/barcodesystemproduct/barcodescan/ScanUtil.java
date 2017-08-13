package com.richfit.barcodesystemproduct.barcodescan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;

public class ScanUtil {

    //解码中文
    private static final String HEXSTRING = "0123456789ABCDEF";

    public interface OnScanListener {
        void getBarcode(String data);
    }

    private OnScanListener listener;

    // 解码广播
    private final static String RECE_DATA_ACTION = "com.se4500.onDecodeComplete";
    // 调用扫描广播
    private final static String START_SCAN_ACTION = "com.geomobile.se4500barcode";
    // 停止
    private final static String STOP_SCAN_ACTION = "com.geomobile.se4500barcodestop";

    // 瑞飞手持
    private static final String WZ_RECT_DATA_ACTION = "com.android.scancontext";
    private static final String WZ_START_SCAN_ACTION = "android.intent.action.FUNCTION_BUTTON_DOWN";
    private static final String WZ_STOP_SCAN = "android.intent.action.FUNCTION_BUTTON_UP";

    // 宿主上下文
    private WeakReference<Context> mContext;

    // 开始扫描标识
    private static boolean isStartScan = false;


    public ScanUtil(Context context) {
        this.mContext = new WeakReference<>(context);
        // 注册系统广播 接受扫描到的数据
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(WZ_RECT_DATA_ACTION);
        iFilter.addAction(RECE_DATA_ACTION);
        context.registerReceiver(receiver, iFilter);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context,
                              Intent intent) {
            String action = intent.getAction();
            Log.e("yff","action = " + action);
            if (action.equals(WZ_RECT_DATA_ACTION)) {
                String data = intent.getStringExtra("Scan_context");
                if (listener != null) {
                    listener.getBarcode(data);
                }
            } else if (action.equals(RECE_DATA_ACTION)) {
                String data = intent.getStringExtra("se4500");
                if (listener != null) {
                    data = data.replace("\n\r", "");
                    data = data.replace("\n", "");
                    data = data.replace("\r", "");
                    data = data.replace("\u0000", "");
                    listener.getBarcode(data);
                }
            }
        }
    };

    /**
     * 获取条码监听
     *
     * @param listener
     */
    public void setOnScanListener(OnScanListener listener) {
        this.listener = listener;
    }

    /**
     * 发送广播 调用系统扫描
     */
    public void startScan() {
        if (!isStartScan) {
            isStartScan = true;
            Context context = mContext.get();
            if (context == null)
                return;
            Intent intent = new Intent();
            intent.setAction(WZ_START_SCAN_ACTION);
            intent.setAction(START_SCAN_ACTION);
            context.sendBroadcast(intent, null);
        }
    }

    /**
     * 发送广播 停止系统扫描
     */
    public void stopScan() {
        isStartScan = false;
        Context context = mContext.get();
        if (context == null)
            return;
        Intent intent = new Intent();
        intent.setAction(STOP_SCAN_ACTION);
        intent.setAction(WZ_STOP_SCAN);
        context.sendBroadcast(intent);
    }

    /**
     * 解除广播注册，释放引用
     */
    public void stopRecData() {
        Context context = mContext.get();
        if (context == null)
            return;
        context.unregisterReceiver(receiver);
        mContext = null;
        receiver = null;
        listener = null;
    }


    /**
     * 条码内容加密
     *
     * @param char_in
     * @return
     */
    public static String CharTrans(String char_in) {
        String char_out = "";
        int char_length = 0;

        char_length = char_in.length();

        int flg_mod = char_length % 2;
        for (int i = 0; i < char_length - 1; i += 2) {
            char_out = char_out + char_in.substring(i + 1, i + 2);
            char_out = char_out + char_in.substring(i, i + 1);
        }

        if (flg_mod != 0) {
            char_out = char_out + char_in.substring(char_length - 1);
        }
        return char_out;
    }


    /**
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     */
    private static String decodeForChinese(String bytes) {
        String str = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream(
                bytes.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < bytes.length(); i += 2)
            baos.write((HEXSTRING.indexOf(bytes.charAt(i)) << 4 | HEXSTRING
                    .indexOf(bytes.charAt(i + 1))));
        try {
            str = new String(baos.toByteArray(), "GB2312");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
