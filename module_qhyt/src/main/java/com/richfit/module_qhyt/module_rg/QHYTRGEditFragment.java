package com.richfit.module_qhyt.module_rg;


import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

/**
 * Created by monday on 2017/2/23.
 */

public class QHYTRGEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {


    @Override
    protected String getInvType() {
        return "";
    }


    @Override
    protected String getInventoryQueryType() {
        return getString(R.string.inventoryQueryTypeSAPLocation);
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initDataLazily() {

    }
}
