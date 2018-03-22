package com.richfit.module_zycj.module_dssc;

import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

/**
 * Created by monday on 2018/1/2.
 */

public class ZYCJDSSCCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp>{
    @Override
    protected void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
