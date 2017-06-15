package com.richfit.module_xngd.module_ds.dsy;


import com.richfit.module_xngd.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

/**
 * Created by monday on 2017/1/20.
 */

public class XNGDDSXSCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {


    @Override
    protected String getInvType() {
        return "01";
    }

    @Override
    protected String getInventoryQueryType() {
        return getString(R.string.inventoryQueryTypeSAPLocation);
    }

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