package com.richfit.module_cq.module_rg;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

/**
 * 采购退货抬头界面
 * Created by monday on 2017/12/5.
 */

public class CQRGHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {
    @Override
    protected void initPresenter() {
        mPresenter = new DSHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        //打开供应商
        llSuppier.setVisibility(View.VISIBLE);
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
