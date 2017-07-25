package com.richfit.module_xngd.module_ms.ubsto;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.sdk_wzyk.base_ms_detail.BaseMSDetailFragment;
import com.richfit.sdk_wzyk.base_ms_detail.imp.MSDetailPresenterImp;

/**
 * Created by monday on 2017/7/20.
 */

public class XNGDMSYDetailFragment extends BaseMSDetailFragment<MSDetailPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new MSDetailPresenterImp(mActivity);
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
        return "ubsto转储";
    }
}
