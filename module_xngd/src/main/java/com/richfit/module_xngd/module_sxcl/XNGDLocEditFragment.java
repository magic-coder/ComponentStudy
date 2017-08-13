package com.richfit.module_xngd.module_sxcl;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_sxcl.baseedit.LocQTEditFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/7/28.
 */

public class XNGDLocEditFragment extends LocQTEditFragment {

    /**
     * 更改获取库存维度
     * @return
     */
    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = "1";
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum", mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }
}
