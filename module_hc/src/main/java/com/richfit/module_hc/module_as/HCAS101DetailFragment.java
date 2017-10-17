package com.richfit.module_hc.module_as;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;
import com.richfit.sdk_wzrk.base_as_detail.imp.ASDetailPresenterImp;

/**
 * Created by monday on 2017/10/12.
 */

public class HCAS101DetailFragment extends BaseASDetailFragment<ASDetailPresenterImp>{
    @Override
    public void initPresenter() {
        mPresenter = new ASDetailPresenterImp(mActivity);
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
        return "物资入库101";
    }
}
