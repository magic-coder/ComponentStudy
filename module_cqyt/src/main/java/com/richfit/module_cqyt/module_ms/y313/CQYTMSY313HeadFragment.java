package com.richfit.module_cqyt.module_ms.y313;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzyk.base_ms_head.BaseMSHeadFragment;
import com.richfit.sdk_wzyk.base_ms_head.imp.MSHeadPresenterImp;

/**
 * 工厂内转储发出
 * Created by monday on 2017/6/29.
 */

public class CQYTMSY313HeadFragment extends BaseMSHeadFragment<MSHeadPresenterImp>{


    @Override
    public void initPresenter() {
      mPresenter = new MSHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void initView() {
        super.initView();
        llWork.setVisibility(View.GONE);
        llInv.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "3";
    }
}
