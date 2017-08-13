package com.richfit.module_xngd.module_ms.ubsto;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_wzyk.base_ms_edit.BaseMSEditFragment;
import com.richfit.sdk_wzyk.base_ms_edit.imp.MSEditPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/7/20.
 */

public class XNGDMSYEditFragment extends BaseMSEditFragment<MSEditPresenterImp> {
    @Override
    public void initPresenter() {
        mPresenter = new MSEditPresenterImp(mActivity);
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
        param.queryType = "03";
        param.invType = "1";
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum",mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }
}
