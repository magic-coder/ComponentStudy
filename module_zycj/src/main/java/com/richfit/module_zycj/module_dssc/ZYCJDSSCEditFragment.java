package com.richfit.module_zycj.module_dssc;

import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

/**
 * Created by monday on 2018/1/2.
 */

public class ZYCJDSSCEditFragment extends BaseDSEditFragment<DSEditPresenterImp>{
    @Override
    protected void initPresenter() {
        mPresenter = new DSEditPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }
}
