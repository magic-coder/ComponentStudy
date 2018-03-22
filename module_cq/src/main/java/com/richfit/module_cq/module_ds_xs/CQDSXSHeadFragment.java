package com.richfit.module_cq.module_ds_xs;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

/**
 * Created by monday on 2017/1/20.
 */

public class CQDSXSHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new DSHeadPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        llCustomer.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {

    }


    @NonNull
    @Override
    protected String getMoveType() {
        return "2";
    }


}
