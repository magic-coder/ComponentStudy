package com.richfit.module_xngd.module_ds.dsn;


import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzck.base_dsn_edit.BaseDSNEditFragment;
import com.richfit.sdk_wzck.base_dsn_edit.imp.DSNEditPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/3/27.
 */

public class XNGDDSNEditFragment extends BaseDSNEditFragment<DSNEditPresenterImp> {

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

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.glAccount = mRefData.glAccount;
        return result;
    }

    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = mRefData.invType;
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum", mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }
}
