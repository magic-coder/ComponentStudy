package com.richfit.scandemo.scancore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.speedata.libuhf.IUHFService;
import com.speedata.libuhf.UHFManager;
import com.speedata.libuhf.bean.Tag_Data;
import com.speedata.libuhf.utils.SharedXmlUtil;

import junit.runner.Version;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * R2000的RFID扫描
 * Created by monday on 2017/11/23.
 */

public class R200ScanService implements IScanService {

    private IUHFService iuhfService;
    private PowerManager pM = null;
    private PowerManager.WakeLock wK = null;
    private int init_progress = 0;
    private String model;

    //选卡
    private boolean inSearch = false;
    private long scant = 0;
    private static Handler handler = null;
    private List<EpcDataBase> firm = new ArrayList<>();

    @Override
    public void initScanService(Context context) {
        try {
            iuhfService = UHFManager.getUHFService(context);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "模块不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        model = SharedXmlUtil.getInstance(context).read("model", "");
        Log.e("yff","model = " + model);
        newWakeLock(context);
        newHandler();
       // EventBus.getDefault().register(this);
    }

    @Override
    public void openScanService() {
        try {
            if (iuhfService != null) {
                if (openDev()) return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeScanService() {
        try {
            if (iuhfService != null) {
                iuhfService.CloseDev();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroyScanService() {
        wK.release();
        //注销广播、对象制空
        UHFManager.closeUHFService();
      //  EventBus.getDefault().unregister(this);
    }

    @Override
    public void startScan() {
        Log.d("yff","startScan; inSearch = " + inSearch);
        if (inSearch) {
            inSearch = false;
            int inventoryStop = iuhfService.inventory_stop();
            if (inventoryStop != 0) {
                Log.e("yff", "停止失败");
            }
        } else {
            inSearch = true;
            scant = 0;
            iuhfService.inventory_start(handler);
        }
    }

    @Override
    public void stopScan() {
        Log.d("yff","stopScan; inSearch = " + inSearch);
        if (inSearch) {
            iuhfService.inventory_stop();
            inSearch = false;
        }
    }


    private void newWakeLock(Context context) {
        init_progress++;
        pM = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (pM != null) {
            wK = pM.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, "lock3992");
            if (wK != null) {
                wK.acquire();
                init_progress++;
            }
        }

        if (init_progress == 1) {
            Log.e("yff", "wake lock init failed");
        }
    }

    private boolean openDev() {
        if (iuhfService.OpenDev() != 0) {
            return true;
        }
        return false;
    }


    private void newHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    scant++;

                    ArrayList<Tag_Data> ks = (ArrayList<Tag_Data>) msg.obj;
                    int i, j;
                    for (i = 0; i < ks.size(); i++) {
                        for (j = 0; j < firm.size(); j++) {
                            if (ks.get(i).epc.equals(firm.get(j).epc)) {
                                firm.get(j).valid++;
                                firm.get(j).setRssi(ks.get(i).rssi);
                                break;
                            }
                        }
                        if (j == firm.size()) {
                            firm.add(new EpcDataBase(ks.get(i).epc, 1,
                                    ks.get(i).rssi, ks.get(i).tid));
                        }
                    }
                    for (EpcDataBase epcDataBase : firm) {
                        Log.d("yff",epcDataBase.toString());
                    }
                }
            }
        };
    }
}
