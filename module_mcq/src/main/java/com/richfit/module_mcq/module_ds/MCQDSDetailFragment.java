package com.richfit.module_mcq.module_ds;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_mcq.adapter.MCQDSDetailAdapter;
import com.richfit.sdk_wzck.base_ds_detail.BaseDSDetailFragment;
import com.richfit.sdk_wzck.base_ds_detail.imp.DSDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/8/31.
 */

public class MCQDSDetailFragment extends BaseDSDetailFragment<DSDetailPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new DSDetailPresenterImp(mActivity);
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
            mAdapter = new MCQDSDetailAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }


    @Override
    protected String getSubFunName() {
        return "物资下架";
    }
}
