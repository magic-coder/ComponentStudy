package com.richfit.module_hc.module_ms;

import android.support.annotation.NonNull;

import com.richfit.sdk_wzyk.base_ms_head.BaseMSHeadFragment;
import com.richfit.sdk_wzyk.base_ms_head.imp.MSHeadPresenterImp;

/**
 * Created by monday on 2017/10/18.
 */

public class HCMSHeadFragmnent extends BaseMSHeadFragment<MSHeadPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new MSHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "3";
    }
}
