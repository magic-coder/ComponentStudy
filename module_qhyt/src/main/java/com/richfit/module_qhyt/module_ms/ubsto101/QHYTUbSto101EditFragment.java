package com.richfit.module_qhyt.module_ms.ubsto101;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

/**
 * Created by monday on 2017/2/15.
 */

public class QHYTUbSto101EditFragment extends BaseASEditFragment<ASEditPresenterImp> {

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
