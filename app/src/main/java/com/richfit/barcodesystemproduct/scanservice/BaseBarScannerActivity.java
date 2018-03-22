package com.richfit.barcodesystemproduct.scanservice;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;

import com.richfit.barcodesystemproduct.BuildConfig;
import com.richfit.common_lib.lib_mvp.BaseActivity;
import com.richfit.common_lib.lib_mvp.IPresenter;
import com.richfit.common_lib.utils.L;
import com.richfit.data.constant.Global;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.ThreadMode;

public abstract class BaseBarScannerActivity<T extends IPresenter> extends BaseActivity<T>{

    IScanService scanService;

    @Override
    public void initVariables() {
        //初始化扫描服务
        scanService = ScanServiceFactory.getScanService();
        scanService.initScanService(this);
        //注册EventBus
        EventBus.getDefault().register(this);
    }

    /**
     * 开启扫描服务
     */
    @Override
    public void onResume() {
        super.onResume();
        scanService.openScanService();
    }

    /**
     * 关闭扫描服务
     */
    @Override
    protected void onPause() {
        scanService.closeScanService();
        super.onPause();
    }

    /**
     * 销毁扫描服务和EventBus
     */
    @Override
    public void onDestroy() {
        scanService.destroyScanService();
        scanService = null;
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_F5:
                scanService.startScan();
                break;
            case KeyEvent.KEYCODE_F4:
                scanService.startScan();
                break;
            case KeyEvent.KEYCODE_1:
                scanService.startScan();
                break;
            case KeyEvent.KEYCODE_FUNCTION:
                scanService.startScan();
                break;
            case KeyEvent.KEYCODE_STEM_2:
                scanService.startScan();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *
     * 对扫描结果进行解密
     * @param barCodeData
     */
    @org.greenrobot.eventbus.Subscribe(threadMode = ThreadMode.MAIN)
    public void decodeScanResult(BarCodeData barCodeData) {
        String info = barCodeData.getData();
        if (TextUtils.isEmpty(info)) {
            return;
        }
        L.e("扫描条码的原始内容 = " + info);
        //仓位和单据不加密
        int length = info.split("\\|", -1).length;
        String barcodeInfo;
        if (length <= 2) {
            barcodeInfo = info;
        } else {
            switch (BuildConfig.APP_NAME) {
                //庆阳单独处理，而且必须保证前两个竖线没有任何值
                case Global.QYSH:
                    if (TextUtils.isEmpty(info)) {
                        showMessage("单据条码信息为空");
                        return;
                    }
                    String tmp[] = info.split("\\|", -1);
                    //处理料签
                    if(tmp.length == 12) {
                        barcodeInfo = ScanUtil.CharTrans(info);
                    } else {
                        String materialNum = tmp[Global.MATERIAL_POS];
                        String contentForSecondPos = tmp[1];
                        if (TextUtils.isEmpty(materialNum)) {
                            showMessage("获取物料条码为空");
                            return;
                        }
                        if (materialNum.length() <= 10 || !TextUtils.isEmpty(contentForSecondPos)) {
                            //说明此时扫描到的是加密的
                            barcodeInfo = ScanUtil.CharTrans(info);
                        } else {
                            barcodeInfo = info;
                        }
                    }
                    break;
                default:
                    //默认物料是加密的
                    barcodeInfo = ScanUtil.CharTrans(info);
                    break;
            }
        }
        if (TextUtils.isEmpty(barcodeInfo)) {
            showMessage("单据条码信息为空");
            return;
        }
        Log.d("yff","解码之后的条码内容" + barcodeInfo);
        String[] a = barcodeInfo.split("\\|", -1);
        handleBarCodeScanResult("", a);
    }
}