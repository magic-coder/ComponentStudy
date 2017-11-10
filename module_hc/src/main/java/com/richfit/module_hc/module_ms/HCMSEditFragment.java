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
    public void initDataLazily() {

    }

    @Override
    protected void initView() {
        super.initView();
        //打开接收批次和接收仓位
        llRecLocation.setVisibility(View.VISIBLE);
        llRecBatch.setVisibility(View.VISIBLE);
    }
}
