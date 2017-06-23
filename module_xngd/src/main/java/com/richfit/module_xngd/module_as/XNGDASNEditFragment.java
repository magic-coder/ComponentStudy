package com.richfit.module_xngd.module_as;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.sdk_wzrk.base_asn_edit.BaseASNEditFragment;
import com.richfit.sdk_wzrk.base_asn_edit.imp.ASNEditPresenterImp;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDASNEditFragment extends BaseASNEditFragment<ASNEditPresenterImp>{
    @Override
    public void initPresenter() {
        mPresenter = new ASNEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

    }

    @Override
    public void initDataLazily() {

    }
}
