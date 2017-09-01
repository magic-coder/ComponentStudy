package com.richfit.module_mcq.module_xxcx;

import android.text.TextUtils;

import com.richfit.domain.bean.InventoryEntity;
import com.richfit.module_mcq.R;
import com.richfit.module_mcq.adapter.MCQInvNDetailAdapter;
import com.richfit.sdk_xxcx.inventory_query_n.detail.InvNQueryDetailFragment;

import java.util.List;

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
            mInvNQueryDetailAdapter = new MCQInvNDetailAdapter(mActivity, com.richfit.sdk_xxcx.R.layout.xxcx_item_invn_query_detail, allNodes);
            mRecyclerView.setAdapter(mInvNQueryDetailAdapter);
        } else {
            mInvNQueryDetailAdapter.addAll(allNodes);
        }
    }

}
