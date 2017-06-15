package com.richfit.module_xngd.module_ds.dsn;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.sdk_wzck.base_dsn_detail.BaseDSNDetailFragment;
import com.richfit.sdk_wzck.base_dsn_detail.imp.DSNDetailPresenterImp;


/**
 * Created by monday on 2017/3/27.
 */

public class XNGDDSNDetailFragment extends BaseDSNDetailFragment<DSNDetailPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new DSNDetailPresenterImp(mActivity);
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
}
