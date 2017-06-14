package com.richfit.module_qhyt.module_ds.dsn;


import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzck.base_dsn_collect.BaseDSNCollectFragment;
import com.richfit.sdk_wzck.base_dsn_collect.imp.DSNCollectPresenterImp;

/**
 * Created by monday on 2017/3/27.
 */

public class QHYTDSNCollectFragment extends BaseDSNCollectFragment<DSNCollectPresenterImp> {

    @Override
    protected String getInvType() {
        return "01";
    }

    @Override
    protected String getInventoryQueryType() {
        return getString(R.string.inventoryQueryTypeSAPLocation);
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {

    }
}
