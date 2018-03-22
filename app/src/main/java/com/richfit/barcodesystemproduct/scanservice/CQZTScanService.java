package com.richfit.barcodesystemproduct.scanservice;

import android.content.Context;
import android.util.Log;

import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeFailureEvent;
import com.honeywell.aidc.BarcodeReadEvent;
import com.honeywell.aidc.BarcodeReader;
import com.honeywell.aidc.ScannerNotClaimedException;
import com.honeywell.aidc.ScannerUnavailableException;
import com.honeywell.aidc.TriggerStateChangeEvent;
import com.honeywell.aidc.UnsupportedPropertyException;
import com.richfit.barcodesystemproduct.home.HomeActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 川庆钻探扫描服务
 * Created by monday on 2017/12/6.
 */

public class CQZTScanService extends BaseScanService implements IScanService, BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener {

    private static BarcodeReader barcodeReader = null;
    private static AidcManager manager = null;

    @Override
    public void beforeInitService(Context context) {
        Log.e("yff", "beforeInitService");
        new Thread(new Runnable() {
            @Override
            public void run() {
                AidcManager.create(context, new AidcManager.CreatedCallback() {
                    @Override
                    public void onCreated(AidcManager aidcManager) {
                        manager = aidcManager;
                        barcodeReader = aidcManager.createBarcodeReader();
                        Log.e("yff", "beforeInitService barcodeReader" + barcodeReader);
                        setBarcodeReaderProperties();
                    }
                });
            }
        }).start();
    }

    @Override
    public void initScanService(Context context) {
        Log.e("yff", "initScanService，barcodeReader = " + barcodeReader);

        if (barcodeReader == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AidcManager.create(context, new AidcManager.CreatedCallback() {
                        @Override
                        public void onCreated(AidcManager aidcManager) {
                            manager = aidcManager;
                            barcodeReader = aidcManager.createBarcodeReader();
                            setBarcodeReaderProperties();
                            openScanService();
                        }
                    });
                }
            }).start();
        }

    }

    @Override
    public void openScanService() {
        Log.e("yff", "openScanService: " + barcodeReader);
        if (barcodeReader != null) {
            try {
                barcodeReader.claim();
            } catch (ScannerUnavailableException e) {
                e.printStackTrace();
                Log.e("yff", "Scanner unavailable : " + e.getMessage());
            }
        }
    }

    @Override
    public void closeScanService() {
        if (barcodeReader != null) {
            barcodeReader.release();
        }
    }

    @Override
    public void destroyScanService() {
        super.destroyScanService();
        if (barcodeReader != null) {
            barcodeReader.removeBarcodeListener(this);
            barcodeReader.removeTriggerListener(this);
        }
    }

    @Override
    public void startScan() {

    }

    @Override
    public void stopScan() {

    }

    @Override
    public void releaseScanService() {
        if (barcodeReader != null) {
            barcodeReader.close();
            barcodeReader = null;
        }

        if (manager != null) {
            manager.close();
            manager = null;
        }
    }

    @Override
    public void onBarcodeEvent(BarcodeReadEvent event) {
        publicScanData(event.getBarcodeData());
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
        Log.e("yff", "扫描出错 : No data " + barcodeFailureEvent.getTimestamp());
    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent event) {
        try {
            barcodeReader.aim(event.getState());
            barcodeReader.light(event.getState());
            barcodeReader.decode(event.getState());

        } catch (ScannerNotClaimedException e) {
            e.printStackTrace();
            Log.e("yff", "onTriggerEvent : Scanner is not claimed");
        } catch (ScannerUnavailableException e) {
            e.printStackTrace();
            Log.e("yff", "onTriggerEvent : Scanner unavailable");
        }
    }

    private void setBarcodeReaderProperties() {
        if (barcodeReader != null) {

            barcodeReader.addBarcodeListener(this);
            try {
                barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                        BarcodeReader.TRIGGER_CONTROL_MODE_CLIENT_CONTROL);
            } catch (UnsupportedPropertyException e) {
                Log.e("yff", "Failed to apply properties : " + e.getMessage());
            }
            barcodeReader.addTriggerListener(this);

            Map<String, Object> properties = new HashMap<String, Object>();
            properties.put(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_GS1_128_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_QR_CODE_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_CODE_39_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_DATAMATRIX_ENABLED, true);
            properties.put(BarcodeReader.PROPERTY_UPC_A_ENABLE, true);
            properties.put(BarcodeReader.PROPERTY_EAN_13_ENABLED, false);
            properties.put(BarcodeReader.PROPERTY_AZTEC_ENABLED, false);
            properties.put(BarcodeReader.PROPERTY_CODABAR_ENABLED, false);
            properties.put(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, false);
            // pdf417
            properties.put(BarcodeReader.PROPERTY_PDF_417_ENABLED, true);
            // Set Max Code 39 barcode length
            properties.put(BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH, 10);
            // Turn on center decoding
            properties.put(BarcodeReader.PROPERTY_CENTER_DECODE, true);
            // Disable bad read response, handle in onFailureEvent
            properties.put(BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED, false);
            // Apply the settings
            barcodeReader.setProperties(properties);
        }

    }
}
