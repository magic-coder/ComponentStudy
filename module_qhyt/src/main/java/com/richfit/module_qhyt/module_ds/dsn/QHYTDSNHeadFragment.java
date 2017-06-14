package com.richfit.module_qhyt.module_ds.dsn;


import com.richfit.sdk_wzck.base_dsn_head.BaseDSNHeadFragment;
import com.richfit.sdk_wzck.base_dsn_head.imp.DSNHeadPresenterImp;

/**
 * Created by monday on 2017/3/27.
 */

public class QHYTDSNHeadFragment extends BaseDSNHeadFragment<DSNHeadPresenterImp> {

    @Override
    protected int getOrgFlag() {
        return 0;
    }


    @Override
    public void initPresenter() {
        mPresenter = new DSNHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }
}
