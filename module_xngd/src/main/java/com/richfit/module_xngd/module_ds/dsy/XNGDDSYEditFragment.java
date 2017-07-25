package com.richfit.module_xngd.module_ds.dsy;


import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/1/20.
 */

public class XNGDDSYEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {


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

    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        param.queryType = "03";
        param.invType = "1";
        Map<String,Object> extraMap = new HashMap<>();
        extraMap.put("invFlag",lineData.invFlag);
        extraMap.put("specialInvFlag",lineData.specialInvFlag);
        param.extraMap = extraMap;
        return param;
    }
}
