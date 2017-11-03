package com.richfit.module_xngd.module_xxcx;

import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.module_xngd.R;
import com.richfit.module_xngd.adapter.XNGDXXCXLQDetailAdapter;
import com.richfit.sdk_xxcx.material_liaoqian.detail.LQDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/6/29.
 */

public class XNGDLQDetailFragment extends LQDetailFragment {

    @Override
    protected int getContentId() {
        return R.layout.xngd_framgent_xxcx_lq_detail;
    }

    @Override
    public void showInventory(List<InventoryEntity> allNodes) {
        if (mLQDetailAdapter == null) {
            mLQDetailAdapter = new XNGDXXCXLQDetailAdapter(mActivity, R.layout.xngd_item_xxcx_lq_detail, allNodes);
            mRecyclerView.setAdapter(mLQDetailAdapter);
        } else {
            mLQDetailAdapter.addAll(allNodes);
        }
    }

    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = "1";
        return param;
    }
}
