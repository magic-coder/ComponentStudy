package com.richfit.module_qhyt.module_ds.ds_ww;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;


/**
 * 青海委外出库抬头界面
 * Created by monday on 2017/3/5.
 */

public class QHYTDSWWHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new DSHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        llSuppier.setVisibility(View.VISIBLE);
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
