package com.richfit.module_qhyt.module_as.as103;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;


/**
 * 青海油田103入库抬头界面
 * Created by monday on 2017/2/17.
 */

public final class QHYTAS103HeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        llSupplier.setVisibility(View.VISIBLE);
        llSendWork.setVisibility(View.GONE);
    }

    @Override
    public void initDataLazily() {

    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }
}
