package com.richfit.module_zycj.module_dssc;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

/**
 * Created by monday on 2018/1/2.
 */

public class ZYCJDSSCHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {
    @Override
    protected void initPresenter() {
        mPresenter = new DSHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        llCreator.setVisibility(View.INVISIBLE);
        llCustomer.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initDataLazily() {

    }


    @NonNull
    @Override
    protected String getMoveType() {
        return "2";
    }
}
