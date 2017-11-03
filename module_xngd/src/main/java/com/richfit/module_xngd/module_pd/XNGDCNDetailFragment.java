package com.richfit.module_xngd.module_pd;

import android.util.Log;
import android.view.View;

import com.richfit.domain.bean.InventoryEntity;
import com.richfit.module_xngd.R;
import com.richfit.module_xngd.adapter.XNGDCNDetailAdapter;
import com.richfit.module_xngd.module_pd.imp.XNGDDetailPresenterImp;
import com.richfit.sdk_wzpd.adapter.CNDetailAdapter;
import com.richfit.sdk_wzpd.checkn.detail.CNDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDCNDetailFragment extends CNDetailFragment<XNGDDetailPresenterImp> {

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_cn_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new XNGDDetailPresenterImp(mActivity);
    }

    @Override
    public void showNodes(List<InventoryEntity> allNodes) {
        if (allNodes == null || allNodes.size() <= 0)
            return;
        if (mAdapter == null) {
            mAdapter = new XNGDCNDetailAdapter(mActivity, R.layout.xngd_item_cn_detail, allNodes);
            mRecycleView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

}
