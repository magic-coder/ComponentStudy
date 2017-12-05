package com.richfit.module_qysh.module_pd;

import android.text.TextUtils;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzpd.checkn.edit.CNEditFragment;

/**
 * Created by monday on 2017/11/28.
 */

public class QYSHCNEditFragment extends CNEditFragment {

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("盘点工厂为空");
            return false;
        }


        if (TextUtils.isEmpty(mRefData.invId)) {
            showMessage("盘点库存地点为空");
            return false;
        }
        return checkCollectedDataBeforeSave();
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
