package com.richfit.module_cq.module_xxcx;

import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_xxcx.material_liaoqian.detail.LQDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/6/29.
 */

public class CQLQDetailFragment extends LQDetailFragment {


    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "01";
        return param;
    }
}
