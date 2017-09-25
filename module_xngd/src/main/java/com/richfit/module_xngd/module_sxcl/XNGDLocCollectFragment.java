package com.richfit.module_xngd.module_sxcl;

import android.text.TextUtils;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_sxcl.basecollect.BaseLocQTCollectFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/7/28.
 */

public class XNGDLocCollectFragment extends BaseLocQTCollectFragment {


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.invFlag = mRefData.invFlag;
        result.specialInvFlag = mRefData.specialInvFlag;
        result.projectNum = mRefData.projectNum;
        return result;
    }
    /**
     * 更改获取库存维度
     *
     * @return
     */
    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = TextUtils.isEmpty(mRefData.invType) ? "1" : mRefData.invType;
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum", mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }
}
