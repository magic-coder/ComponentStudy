package com.richfit.module_xngd.module_ms.ubsto;

import android.view.View;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_wzyk.base_ms_collect.BaseMSCollectFragment;
import com.richfit.sdk_wzyk.base_ms_collect.imp.MSCollectPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/7/20.
 */

public class XNGDMSYCollectFragment extends BaseMSCollectFragment<MSCollectPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new MSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        llRecBatch.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

    }

    @Override
    protected int getOrgFlag() {
        return 0;
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
