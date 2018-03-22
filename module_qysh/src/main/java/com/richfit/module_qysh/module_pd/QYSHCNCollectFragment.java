package com.richfit.module_qysh.module_pd;

import android.text.TextUtils;
import android.util.Log;

import com.richfit.data.constant.Global;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzpd.checkn.collect.CNCollectFragment;

/**
 * Created by monday on 2017/8/10.
 */

public class QYSHCNCollectFragment extends CNCollectFragment {

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type, list);
        if (list != null && list.length == 12) {
            final String materialNum = list[4];
            final String location    = list[2];
            if (cbSingle.isChecked() && materialNum.equalsIgnoreCase(getString(etMaterialNum))) {
                saveCollectedData();
            } else if (!cbSingle.isChecked()) {
                etMaterialNum.setText(materialNum);
                etCheckLocation.setText(location);
                getCheckTransferInfoSingle(materialNum, location);
            }

        }
    }

    //增加工厂和库存地点的检查
    @Override
    public void initDataLazily() {
        if (mRefData == null) {
            showMessage("请先在抬头页面初始化本次盘点");
            return;
        }
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先在抬头界面选择工厂");
            return;
        }

        if (TextUtils.isEmpty(mRefData.invId)) {
            showMessage("请先在抬头界面选择库存");
            return;
        }
        super.initDataLazily();
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.workId = mRefData.workId;
        result.workCode = mRefData.workCode;
        result.invId = mRefData.invId;
        result.invCode = mRefData.invCode;
        return result;
    }

}
