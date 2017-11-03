package com.richfit.module_qysh.module_as.as103;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHAS103HeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        //打开供应商
        llSupplier.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initDataLazily() {

    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }
}
