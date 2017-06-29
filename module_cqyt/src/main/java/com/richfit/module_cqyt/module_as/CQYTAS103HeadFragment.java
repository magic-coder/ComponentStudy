package com.richfit.module_cqyt.module_as;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * Created by monday on 2017/6/29.
 */

public class CQYTAS103HeadFragment extends BaseASHeadFragment<ASHeadPresenterImp>{
    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void initView() {
        super.initView();
        llSupplier.setVisibility(View.VISIBLE);
        llSendWork.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }
}
