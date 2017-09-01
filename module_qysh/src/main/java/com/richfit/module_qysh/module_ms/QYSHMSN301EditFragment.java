package com.richfit.module_qysh.module_ms;

import android.text.TextUtils;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzyk.base_msn_edit.BaseMSNEditFragment;
import com.richfit.sdk_wzyk.base_msn_edit.imp.MSNEditPresenterImp;

/**
 * Created by monday on 2017/2/8.
 */

public class QYSHMSN301EditFragment extends BaseMSNEditFragment<MSNEditPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new MSNEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

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
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.invType = "0";
        queryParam.queryType = "03";
        return queryParam;
    }

}
