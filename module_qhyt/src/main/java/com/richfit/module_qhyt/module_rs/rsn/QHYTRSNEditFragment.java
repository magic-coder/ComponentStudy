package com.richfit.module_qhyt.module_rs.rsn;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.sdk_wzrs.base_rsn_edit.BaseRSNEditFragment;
import com.richfit.sdk_wzrs.base_rsn_edit.imp.RSNEditPresenterImp;

/**
 * Created by monday on 2017/3/2.
 */

public class QHYTRSNEditFragment extends BaseRSNEditFragment<RSNEditPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new RSNEditPresenterImp(mActivity);
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

    @Override
    protected String getInventoryQueryType() {
        return null;
    }
}
