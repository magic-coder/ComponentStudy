package com.richfit.sdk_xxcx.inventory_query_n.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailFragment;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_xxcx.R;
import com.richfit.sdk_xxcx.adapter.InvNQueryDetailAdapter;
import com.richfit.sdk_xxcx.inventory_query_n.detail.imp.InvNQueryDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/5/25.
 */

public class InvNQueryDetailFragment extends BaseDetailFragment<InvNQueryDetailPresenterImp, InventoryEntity>
        implements IInvNQueryDetailView {

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
        if (mRefData == null) {
            showMessage("请现在抬头界面选择必要的信息");
            return;
        }

        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请现在抬头界面选择工厂");
            return;
        }

        if (TextUtils.isEmpty(mRefData.invId)) {
            showMessage("请现在抬头界面选择库存地点");
            return;
        }

        if (TextUtils.isEmpty(mRefData.bizType)) {
            showMessage("未获取到业务类型");
            return;
        }

        //开始加载
        startAutoRefresh();
    }

    @Override
    public void onRefresh() {
        InventoryQueryParam param = provideInventoryQueryParam();
        mPresenter.getInventoryInfo(param.queryType, mRefData.workId,mRefData.invId,
                mRefData.workCode, mRefData.invCode, "", mRefData.materialNum, "", mRefData.location,
                mRefData.batchFlag, "", "", param.invType, "", param.extraMap);
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
