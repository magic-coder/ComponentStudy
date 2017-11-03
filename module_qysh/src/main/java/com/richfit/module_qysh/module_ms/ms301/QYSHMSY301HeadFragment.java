package com.richfit.module_qysh.module_ms.ms301;

import android.support.annotation.NonNull;

import com.richfit.sdk_wzyk.base_ms_head.BaseMSHeadFragment;
import com.richfit.sdk_wzyk.base_ms_head.imp.MSHeadPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHMSY301HeadFragment extends BaseMSHeadFragment<MSHeadPresenterImp>{

    @Override
    protected void initPresenter() {
        mPresenter = new MSHeadPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "3";
    }
}
