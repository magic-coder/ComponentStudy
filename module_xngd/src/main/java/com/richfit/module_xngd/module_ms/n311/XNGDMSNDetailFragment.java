package com.richfit.module_xngd.module_ms.n311;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.sdk_wzyk.base_msn_detail.BaseMSNDetailFragment;
import com.richfit.sdk_wzyk.base_msn_detail.imp.MSNDetailPresenterImp;

/**
 * Created by monday on 2017/7/20.
 */

public class XNGDMSNDetailFragment extends BaseMSNDetailFragment<MSNDetailPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new MSNDetailPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected String getSubFunName() {
        return "311无参考移库";
    }

    @Override
    protected boolean checkTransStateBeforeRefresh() {
        return true;
    }
}
