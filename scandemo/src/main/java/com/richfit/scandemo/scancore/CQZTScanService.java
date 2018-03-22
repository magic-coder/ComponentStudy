package com.richfit.scandemo.scancore;

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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 川庆钻探扫描服务
 * Created by monday on 2017/12/6.
 */

public class CQZTScanService implements IScanService, BarcodeReader.BarcodeListener,
        BarcodeReader.TriggerListener {

    private AidcManager manager;
    private BarcodeReader barcodeReader;

    @Override
    public void initScanService(Context context) {
        AidcManager.create(context, new AidcManager.CreatedCallback() {

            @Override
            public void onCreated(AidcManager aidcManager) {
                manager = aidcManager;
                barcodeReader = manager.createBarcodeReader();
                Log.e("yff","onCreated : " + barcodeReader);
                Log.e("yff","onCreated : " + Thread.currentThread().getName());
                setBarcodeReaderProperties();
                openScanService();
            }
        });
    }

    @Override
    public void openScanService() {
        Log.e("yff","openScanService: " + barcodeReader);
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
            // release the scanner claim so we don't get any scanner
            // notifications while paused.
            barcodeReader.release();
        }
    }

    @Override
    public void destroyScanService() {
        if (barcodeReader != null) {
            // close BarcodeReader to clean up resources.
            barcodeReader.close();
            barcodeReader = null;
            // unregister barcode event listener
            barcodeReader.removeBarcodeListener(this);
            // unregister trigger state change listener
            barcodeReader.removeTriggerListener(this);
        }

        if (manager != null) {
            // close AidcManager to disconnect from the scanner service.
            // once closed, the object can no longer be used.
            manager.close();
        }
    }

    @Override
    public void startScan() {

    }

    @Override
    public void stopScan() {

    }

    @Override
    public void onBarcodeEvent(BarcodeReadEvent event) {
        List<String> list = new ArrayList<String>();
        list.add("Barcode data: " + event.getBarcodeData());
        list.add("Character Set: " + event.getCharset());
        list.add("Code ID: " + event.getCodeId());
        list.add("AIM ID: " + event.getAimId());
        list.add("Timestamp: " + event.getTimestamp());
        Log.e("yff","扫描到的数据 : " + list.toString());
    }

    @Override
    public void onFailureEvent(BarcodeFailureEvent barcodeFailureEvent) {
        Log.e("yff","扫描出错 : No data " + barcodeFailureEvent.getTimestamp());
    }

    @Override
    public void onTriggerEvent(TriggerStateChangeEvent event) {
        try {
            // only handle trigger presses
            // turn on/off aimer, illumination and decoding
            barcodeReader.aim(event.getState());
            barcodeReader.light(event.getState());
            barcodeReader.decode(event.getState());

        } catch (ScannerNotClaimedException e) {
            e.printStackTrace();
            Log.e("yff","onTriggerEvent : Scanner is not claimed");
        } catch (ScannerUnavailableException e) {
            e.printStackTrace();
            Log.e("yff","onTriggerEvent : Scanner unavailable");
        }
    }

    private void setBarcodeReaderProperties() {
        if (barcodeReader != null) {

            // register bar code event listener
            barcodeReader.addBarcodeListener(this);

            // set the trigger mode to client control
            try {
                barcodeReader.setProperty(BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                        BarcodeReader.TRIGGER_CONTROL_MODE_CLIENT_CONTROL);
            } catch (UnsupportedPropertyException e) {
                Log.e("yff", "Failed to apply properties : " + e.getMessage());
            }
            // register trigger state change listener
            barcodeReader.addTriggerListener(this);

            Map<String, Object> properties = new HashMap<String, Object>();
            // Set Symbologies On/Off
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
