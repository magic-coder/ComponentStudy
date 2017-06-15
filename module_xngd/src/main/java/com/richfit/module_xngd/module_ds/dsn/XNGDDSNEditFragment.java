package com.richfit.module_xngd.module_ds.dsn;


import com.richfit.module_xngd.R;
import com.richfit.sdk_wzck.base_dsn_edit.BaseDSNEditFragment;
import com.richfit.sdk_wzck.base_dsn_edit.imp.DSNEditPresenterImp;

/**
 * Created by monday on 2017/3/27.
 */

public class XNGDDSNEditFragment extends BaseDSNEditFragment<DSNEditPresenterImp> {

    @Override
    protected String getInvType() {
        return "1";
    }

    @Override
    protected String getInventoryQueryType() {
        return getString(R.string.inventoryQueryTypePrecise);
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSNEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initDataLazily() {

    }
}
