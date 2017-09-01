package com.richfit.module_mcq.module_dscx.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailFragment;
import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailPresenterImp;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_mcq.R;
import com.richfit.module_mcq.adapter.DSCXAdapter;

import java.util.List;

/**
 * Created by monday on 2017/8/28.
 */

public class DSCXDetailFragment extends BaseDetailFragment<BaseDetailPresenterImp,RefDetailEntity> {

    @Override
    protected int getContentId() {
        return R.layout.mcq_fragment_dscx_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new BaseDetailPresenterImp(mActivity);
    }

    @Override
    public void deleteNode(RefDetailEntity node, int position) {

    }

    @Override
    public void editNode(RefDetailEntity node, int position) {

    }

    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        if (mAdapter == null) {
            mAdapter = new DSCXAdapter(mActivity, R.layout.mcq_item_dscx_head,allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    @Override
    public void refreshComplete() {

    }

    @Override
    public void deleteNodeSuccess(int position) {

    }

    @Override
    public void submitBarcodeSystemSuccess() {

    }

    @Override
    public void submitSAPSuccess() {

    }

    @Override
    public void upAndDownLocationSuccess() {

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
    public void initDataLazily() {

    }

    @Override
    protected boolean checkTransStateBeforeRefresh() {
        return false;
    }

    @Override
    protected void submit2BarcodeSystem(String tranToSapFlag) {

    }

    @Override
    protected void submit2SAP(String tranToSapFlag) {

    }

    @Override
    protected void sapUpAndDownLocation(String transToSapFlag) {

    }
}
