package com.richfit.module_xngd.module_rg;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/6/20.
 */

public class XNGDRGCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        quantityName.setText("实退数量");
        actQuantityName.setText("应退数量");
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
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        param.queryType = "03";
        param.invType = mRefData.invType;
        Map<String,Object> extraMap = new HashMap<>();
        extraMap.put("invFlag",lineData.invFlag);
        extraMap.put("specialInvFlag",lineData.specialInvFlag);
        param.extraMap = extraMap;
        return param;
    }
}
