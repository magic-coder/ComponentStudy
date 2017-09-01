package com.richfit.module_mcq.module_as;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_mcq.R;
import com.richfit.module_mcq.adapter.MCQASDetailAdapter;
import com.richfit.module_mcq.module_as.imp.MCQASDetailPresenterImp;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/8/31.
 */

public class MCQASDetailFragment extends BaseASDetailFragment<MCQASDetailPresenterImp> {

    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_as_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MCQASDetailPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        if (mAdapter == null) {
            mAdapter = new MCQASDetailAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    @Override
    protected String getSubFunName() {
        return "物资上架";
    }
}
