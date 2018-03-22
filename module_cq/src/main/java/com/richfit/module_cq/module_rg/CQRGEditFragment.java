package com.richfit.module_cq.module_rg;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

/**
 * Created by monday on 2017/12/5.
 */

public class CQRGEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new DSEditPresenterImp(mActivity);
    }


    @Override
    public void initDataLazily() {

    }

    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.queryType = "01";
        return queryParam;
    }
}
