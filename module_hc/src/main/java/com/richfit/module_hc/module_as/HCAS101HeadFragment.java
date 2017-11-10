package com.richfit.module_hc.module_as;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * Created by monday on 2017/10/12.
 */

public class HCAS101HeadFragment extends BaseASHeadFragment<ASHeadPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    protected void initView() {
        super.initView();
        //显示供应商
        llSupplier.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }
}
