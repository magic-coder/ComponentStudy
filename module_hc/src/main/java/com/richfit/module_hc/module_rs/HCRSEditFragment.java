package com.richfit.module_hc.module_rs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

/**
 * Created by monday on 2017/10/18.
 */

public class HCRSEditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        tvActQuantityName.setText("应退数量");
        tvQuantityName.setText("实退数量");
    }

    @Override
    public void initDataLazily() {

    }
}
