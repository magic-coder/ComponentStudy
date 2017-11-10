package com.richfit.module_qysh.module_ms.dgms301;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzyk.base_msn_edit.BaseMSNEditFragment;
import com.richfit.sdk_wzyk.base_msn_edit.imp.MSNEditPresenterImp;

import java.util.HashMap;

/**
 * Created by monday on 2017/2/8.
 */

public class QYSHMSN301EditFragment extends BaseMSNEditFragment<MSNEditPresenterImp> {

    String mDeviceId;

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        super.initVariable(savedInstanceState);
        isOpenLocationType = false;
        isOpenRecLocationType = false;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MSNEditPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        super.initView();
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if(batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
        llRecBatchFlag.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        mDeviceId = bundle.getString(Global.EXTRA_DEVICE_ID_KEY);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        final String recLocation = getString(autoRecLoc);
        if (!TextUtils.isEmpty(recLocation) && recLocation.length() != 11) {
            showMessage("修改失败,请先检查接收仓位是否合理");
            return false;
        }
        if (TextUtils.isEmpty(mDeviceId)) {
            showMessage("设备Id为空");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.deviceId = mDeviceId;
        result.batchFlag = null;
        result.recBatchFlag = null;
        return result;
    }


    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.invType = "0";
        queryParam.queryType = "03";
        if(queryParam.extraMap == null)
            queryParam.extraMap = new HashMap<>();
        queryParam.extraMap.put("deviceId",mDeviceId);
        return queryParam;
    }

}
