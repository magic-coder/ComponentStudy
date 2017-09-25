package com.richfit.module_mcq.module_xxcx;

import android.text.TextUtils;

import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.module_mcq.R;
import com.richfit.module_mcq.adapter.MCQInvNDetailAdapter;
import com.richfit.sdk_xxcx.inventory_query_n.detail.InvNQueryDetailFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/8/28.
 */

public class MCQInvNDetailFragment extends InvNQueryDetailFragment {

    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_invn_detail;
    }

    @Override
    public void initDataLazily() {
        if (mRefData == null) {
            showMessage("请现在抬头界面选择必要的信息");
            return;
        }

        if (TextUtils.isEmpty(mRefData.location)) {
            showMessage("请先在抬头界面输入仓位");
            return;
        }
        super.initDataLazily();
    }


    @Override
    public void showInventory(List<InventoryEntity> allNodes) {
        if (mInvNQueryDetailAdapter == null) {
            mInvNQueryDetailAdapter = new MCQInvNDetailAdapter(mActivity,
                    R.layout.mcq_item_invn_head, allNodes);
            mRecyclerView.setAdapter(mInvNQueryDetailAdapter);
        } else {
            mInvNQueryDetailAdapter.addAll(allNodes);
        }
    }

    //这里将locationType放在了抬头
    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("locationType", mRefData.locationType);
        queryParam.extraMap = extraMap;
        return queryParam;
    }
}