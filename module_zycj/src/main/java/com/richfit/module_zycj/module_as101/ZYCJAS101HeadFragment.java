package com.richfit.module_zycj.module_as101;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * Created by monday on 2018/1/2.
 */

public class ZYCJAS101HeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    protected void initView() {
        super.initView();
        //打开供应商
        llSupplier.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }
}
