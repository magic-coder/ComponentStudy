package com.richfit.module_zycj.module_dsxs;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

/**
 * Created by monday on 2018/1/2.
 */

public class ZYCJDSXSHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {
    @Override
    protected void initPresenter() {
        mPresenter = new DSHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        //关闭创建人，打开客户
        llCreator.setVisibility(View.INVISIBLE);
        llCustomer.setVisibility(View.VISIBLE);
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
