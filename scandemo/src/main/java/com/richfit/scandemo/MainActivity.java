package com.richfit.scandemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ScanUtil mScanUtil;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.show_barcode);
        mScanUtil = new ScanUtil(this);
    }


    @Override
    public void onDestroy() {
        mScanUtil.stopRecData();
        mScanUtil = null;
        super.onDestroy();
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
                mScanUtil.startScan();
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
