package com.richfit.module_mcq.module_pd.detail;

import com.richfit.domain.bean.InventoryEntity;
import com.richfit.module_mcq.R;
import com.richfit.module_mcq.adapter.MCQBCDetailAdapter;
import com.richfit.sdk_wzpd.blind.detail.BCDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/8/29.
 */

public class MCQBCDetailFragment extends BCDetailFragment {

    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_bc_detail;
    }


    @Override
    public void showNodes(List<InventoryEntity> allNodes) {
        if (mAdapter == null) {
            mAdapter = new MCQBCDetailAdapter(mActivity, R.layout.mcq_item_bc_detail, allNodes);
            mRecycleView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }
}
