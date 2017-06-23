package com.richfit.module_qhyt.module_ds.ds_xs;


import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

/**
 * Created by monday on 2017/1/20.
 */

public class QHYTDSXSEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new DSEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initDataLazily() {

    }
}
