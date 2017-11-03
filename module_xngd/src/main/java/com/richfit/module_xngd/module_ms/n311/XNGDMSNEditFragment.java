package com.richfit.module_xngd.module_ms.n311;

import android.text.TextUtils;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzyk.base_msn_edit.BaseMSNEditFragment;
import com.richfit.sdk_wzyk.base_msn_edit.imp.MSNEditPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/7/20.
 */

public class XNGDMSNEditFragment extends BaseMSNEditFragment<MSNEditPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new MSNEditPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.specialInvFlag = mRefData.specialInvFlag;
        result.specialInvNum = mRefData.projectNum;
        result.invFlag = mRefData.invFlag;
        result.projectNum = mRefData.projectNum;
        return  result;
    }

    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = TextUtils.isEmpty( mRefData.invType) ? "1" :  mRefData.invType;
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum", mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }
}
