package com.richfit.scandemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.richfit.scandemo.scancore.IScanService;
import com.richfit.scandemo.scancore.ScanServiceFactory;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    IScanService scanService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.show_barcode);
        scanService = ScanServiceFactory.getScanService();
        scanService.initScanService(this);
    }

    @Override
    protected void onPostResume() {
        scanService.openScanService();
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        scanService.closeScanService();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        scanService.destroyScanService();
        scanService = null;
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
                scanService.stopScan();
                break;
            case KeyEvent.KEYCODE_F4:
                scanService.stopScan();
                break;
            case KeyEvent.KEYCODE_1:
                scanService.stopScan();
                break;
            case KeyEvent.KEYCODE_FUNCTION:
                scanService.stopScan();
                break;
            case KeyEvent.KEYCODE_STEM_2:
                scanService.stopScan();
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
