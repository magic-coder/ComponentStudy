package com.richfit.module_xngd.module_ds.ds_ll;


import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/1/20.
 */

public class XNGDDSLLCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {

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


    @Override
    protected int getOrgFlag() {
        return getInteger(R.integer.orgNorm);
    }


    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        param.queryType = "03";
        param.invType = "1";
        Map<String,Object> extraMap = new HashMap<>();
        extraMap.put("invFlag",lineData.invFlag);
        extraMap.put("specialInvFlag",lineData.specialInvFlag);
        param.extraMap = extraMap;
        return param;
    }

}
