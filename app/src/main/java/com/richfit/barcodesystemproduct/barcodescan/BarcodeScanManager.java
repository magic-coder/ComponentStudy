package com.richfit.barcodesystemproduct.barcodescan;

import android.content.Context;
import android.media.ToneGenerator;
import android.os.Handler;

import com.motorolasolutions.adc.decoder.BarCodeReader;
import com.richfit.common_lib.lib_eventbus.EventBusUtil;

import java.lang.ref.WeakReference;
import java.util.concurrent.Semaphore;

import io.reactivex.disposables.CompositeDisposable;

/**
 * 统一管理扫描，实现的目标是采用Suspension+Future模式，每一次用户扫描都封装成一个
 * Task，立即返回Result,用户可以通过Result获取异步的结果
 * Created by monday on 2017/6/16.
 */

public class BarcodeScanManager {


    private WeakReference<Context> wContext;



    //扫描模块
    static {
        System.loadLibrary("IAL");
        System.loadLibrary("SDL");
        System.loadLibrary("barcodereader44");
    }


    private static final String HEXSTRING = "0123456789ABCDEF";
    //使用服务作为扫描条码
    private static boolean isServiceDL = false;
    private boolean isStartScan = false;

    //大庆手持的扫码广播
    private static final String DQ_RECE_DATA_ACTION = "com.se4500.onDecodeComplete";
    private static final String DQ_START_SCAN_ACTION = "com.geomobile.se4500barcode";
    private static final String DQ_STOP_SCAN = "com.geomobile.se4500barcode.poweroff";

    //物资公司的扫码广播
    private static final String WZ_RECT_DATA_ACTION = "com.android.scancontext";
    private static final String WZ_START_SCAN_ACTION = "android.intent.action.FUNCTION_BUTTON_DOWN";
    private static final String WZ_STOP_SCAN = "android.intent.action.FUNCTION_BUTTON_UP";

    private static final int STATE_IDLE = 0;
    private static final int STATE_DECODE = 1;
    private static final int STATE_SNAPSHOT = 4;

    private static boolean stateIsDecoding = false;
    private ToneGenerator tg = null;
    private static BarCodeReader bcr = null;
    private boolean beepMode = true;
    private int trigMode = BarCodeReader.ParamVal.AUTO_AIM;
    private static int state = STATE_IDLE;
    private int decodes = 0;
    private int motionEvents = 0;
    private int modechgEvents = 0;
    private static long decode_start = 0;
    private static long decode_end = 0;
    private boolean isnotdecode = true;
    private static Handler mHandler = null;
    private Semaphore mSemaphore;
    private CompositeDisposable mCompositeDisposable;

    public BarcodeScanManager(Context context) {
        this.wContext = new WeakReference<>(context);
        initBarcodeScan();
    }

    /**
     * 初始化扫描需要的相关数据
     */
    private void initBarcodeScan() {
        mSemaphore = new Semaphore(1);
        EventBusUtil.register(this);
        state = STATE_IDLE;
        //注意这里handler使用的是MainLooper
        mHandler = new Handler();
        mCompositeDisposable = new CompositeDisposable();
    }

    public void openBarcodeReader() {

    }




}
