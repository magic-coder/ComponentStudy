package com.richfit.module_hc.module_ds;

import android.view.View;

import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

/**
 * Created by monday on 2017/10/18.
 */

public class HCDSEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {
    @Override
    public void initPresenter() {
        mPresenter = new DSEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        llLocationType.setVisibility(View.VISIBLE);
    }

    @Override
    public void initDataLazily() {

    }
}
