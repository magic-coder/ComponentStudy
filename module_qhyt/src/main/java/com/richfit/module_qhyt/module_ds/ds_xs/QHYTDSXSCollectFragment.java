package com.richfit.module_qhyt.module_ds.ds_xs;


import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

/**
 * Created by monday on 2017/1/20.
 */

public class QHYTDSXSCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {

    @Override
    protected int getOrgFlag() {
        return getInteger(R.integer.orgNorm);
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
