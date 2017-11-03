package com.richfit.module_hc.module_ms;

import android.view.View;

import com.richfit.sdk_wzyk.base_ms_collect.BaseMSCollectFragment;
import com.richfit.sdk_wzyk.base_ms_collect.imp.MSCollectPresenterImp;

/**
 * Created by monday on 2017/10/18.
 */

public class HCMSCollectFragment  extends BaseMSCollectFragment<MSCollectPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new MSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
