package com.richfit.module_hc.module_ms;

import android.view.View;

import com.richfit.sdk_wzyk.base_ms_edit.BaseMSEditFragment;
import com.richfit.sdk_wzyk.base_ms_edit.imp.MSEditPresenterImp;

/**
 * Created by monday on 2017/10/18.
 */

public class HCMSEditFragment extends BaseMSEditFragment<MSEditPresenterImp> {
    @Override
    public void initPresenter() {
        mPresenter = new MSEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        llLocationType.setVisibility(View.VISIBLE);
    }

    @Override
    public void initDataLazily() {

    }
}
