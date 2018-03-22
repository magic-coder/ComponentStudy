package com.richfit.barcodesystemproduct.scanservice;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/12/6.
 */

public abstract  class BaseScanService implements IScanService {

    private static Map<String,BarCodeData> dataPool = new HashMap<>();

    @Override
    public void publicScanData(String data) {
        BarCodeData barCodeData = dataPool.get(data);
        if(barCodeData == null) {
            barCodeData = new BarCodeData();
        }
        barCodeData.setData(data);
        dataPool.put(data,barCodeData);
        EventBus.getDefault().post(barCodeData);
    }

    @Override
    public void destroyScanService() {
        evict();
    }

    /**
     * 清除数据池
     */
    protected void evict() {
        dataPool.clear();
    }

}
