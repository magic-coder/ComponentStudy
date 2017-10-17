package com.richfit.module_hc.module_as;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

/**
 * Created by monday on 2017/10/12.
 */

public class HCAS101EditFragment extends BaseASEditFragment<ASEditPresenterImp>{
    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
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
