package com.richfit.module_cq.module_ds_xs;


import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.IDSCollectView;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

/**
 * Created by monday on 2017/1/20.
 */

public class CQDSXSCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {

    }
}
