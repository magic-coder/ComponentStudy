package com.richfit.module_xngd.module_xxcx;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_xxcx.inventory_query_n.detail.InvNQueryDetailFragment;

/**
 * Created by monday on 2017/6/29.
 */

public class XNGDINVNDetailFragment extends InvNQueryDetailFragment {

    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = "1";
        return param;
    }
}
