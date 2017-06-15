package com.richfit.sdk_xxcx.inventory_query_n.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailFragment;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.sdk_xxcx.R;
import com.richfit.sdk_xxcx.adapter.InvNQueryDetailAdapter;
import com.richfit.sdk_xxcx.inventory_query_n.detail.imp.InvNQueryDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/5/25.
 */

public class IInvNQueryDetailFragment extends BaseDetailFragment<InvNQueryDetailPresenterImp,InventoryEntity>
        implements InvNQueryDetailView {

    InvNQueryDetailAdapter mInvNQueryDetailAdapter;

    @Override
    protected int getContentId() {
        return R.layout.xxcx_fragment_invn_query_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new InvNQueryDetailPresenterImp(mActivity);
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
    public void showInventory(List<InventoryEntity> allNodes) {
        if (mInvNQueryDetailAdapter == null) {
            mInvNQueryDetailAdapter = new InvNQueryDetailAdapter(mActivity, R.layout.xxcx_item_invn_query_detail, allNodes);
            mRecyclerView.setAdapter(mInvNQueryDetailAdapter);
        } else {
            mInvNQueryDetailAdapter.addAll(allNodes);
        }
    }

    @Override
    public void loadInventoryFail(String message) {
        showMessage(message);
    }


    @Override
    public void deleteNode(InventoryEntity node, int position) {

    }

    @Override
    public void editNode(InventoryEntity node, int position) {

    }

    @Override
    public void showNodes(List<InventoryEntity> allNodes) {

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
    public void submitBarcodeSystemFail(String message) {

    }

    @Override
    public void submitSAPSuccess() {

    }

    @Override
    public void submitSAPFail(String[] messages) {

    }

    @Override
    public void upAndDownLocationFail(String[] messages) {

    }

    @Override
    public void upAndDownLocationSuccess() {

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
