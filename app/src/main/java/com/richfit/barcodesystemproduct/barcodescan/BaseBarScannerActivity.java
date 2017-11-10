package com.richfit.barcodesystemproduct.barcodescan;

import android.text.TextUtils;
import android.view.KeyEvent;

import com.richfit.barcodesystemproduct.BuildConfig;
import com.richfit.common_lib.lib_mvp.BaseActivity;
import com.richfit.common_lib.lib_mvp.IPresenter;
import com.richfit.common_lib.utils.L;
import com.richfit.data.constant.Global;

import org.w3c.dom.Text;

public abstract class BaseBarScannerActivity<T extends IPresenter> extends BaseActivity<T>
        implements ScanUtil.OnScanListener {

    ScanUtil mScanUtil;

    @Override
    public void initVariables() {
        mScanUtil = new ScanUtil(this);
        mScanUtil.setOnScanListener(this);
    }

    @Override
    public void onDestroy() {
        mScanUtil.stopRecData();
        mScanUtil = null;
        super.onDestroy();
    }


    @Override
    public void getBarcode(String data) {
        filerBarCodeInfo(data);
    }


    /**
     * 更具条码的类型，获取条码的信息
     * 条码类型|物料编码|批次|验收人|采购订单号
     */
    private void filerBarCodeInfo(String info) {
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

        String[] a = barcodeInfo.split("\\|", -1);
        handleBarCodeScanResult("", a);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_F5:
                mScanUtil.startScan();
                break;
            case KeyEvent.KEYCODE_F4:
                mScanUtil.startScan();
                break;
            case KeyEvent.KEYCODE_1:
                mScanUtil.startScan();
                break;
            case KeyEvent.KEYCODE_FUNCTION:
                mScanUtil.startScan();
                break;
            case KeyEvent.KEYCODE_STEM_2:
                mScanUtil.startScan();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 停止扫描
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_F5:
                mScanUtil.stopScan();
                break;
            case KeyEvent.KEYCODE_F4:
                mScanUtil.stopScan();
                break;
            case KeyEvent.KEYCODE_1:
                mScanUtil.stopScan();
                break;
            case KeyEvent.KEYCODE_FUNCTION:
                mScanUtil.stopScan();
                break;
            case KeyEvent.KEYCODE_STEM_2:
                mScanUtil.stopScan();
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

}