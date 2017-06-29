package com.richfit.module_xngd.module_xxcx;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_xxcx.material_liaoqian.detail.LQDetailFragment;

/**
 * Created by monday on 2017/6/29.
 */

public class XNGDLQDetailFragment extends LQDetailFragment{

    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = "1";
        return param;
    }
}
