package com.richfit.module_cq.module_ms_315;

import android.support.annotation.NonNull;

import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * 工厂内转储接收
 * Created by monday on 2017/6/30.
 */

public class CQMSY315HeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "3";
    }
}
