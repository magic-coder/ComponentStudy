package com.richfit.sdk_xxcx.material_liaoqian.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailFragment;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_xxcx.R;
import com.richfit.sdk_xxcx.adapter.LQDetailAdapter;
import com.richfit.sdk_xxcx.material_liaoqian.detail.imp.LQDetailPresenterImp;

import java.util.List;

/**
 * 有仓位queryType 04 ；没有仓位 01
 * Created by monday on 2017/3/16.
 */

public class LQDetailFragment extends BaseDetailFragment<LQDetailPresenterImp, InventoryEntity>
        implements ILQDetailView {

    protected LQDetailAdapter mLQDetailAdapter;

    @Override
    protected int getContentId() {
        return R.layout.xxcx_fragment_lq_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new LQDetailPresenterImp(mActivity);
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


    /**
     * 检查抬头界面的必要的字段是否已经赋值
     */
    @Override
    public void initDataLazily() {
        if (mRefData == null) {
            showMessage("请先在抬头界面扫描料签");
            return;
        }

        if (TextUtils.isEmpty(mRefData.workCode)) {
            showMessage("扫描料签中未含有工厂信息");
            return;
        }

        if (TextUtils.isEmpty(mRefData.invCode)) {
            showMessage("扫描料签中未含有库存地点信息");
            return;
        }
        //开始刷新
        startAutoRefresh();
    }

    @Override
    public void onRefresh() {
        InventoryQueryParam param = provideInventoryQueryParam();
        mPresenter.getInventoryInfo(param.queryType, "", "", mRefData.workCode,
                mRefData.invCode, "", mRefData.materialNum, "", mRefData.location,
                mRefData.batchFlag, "", "", param.invType, "",param.extraMap);
    }

    @Override
    public void showInventory(List<InventoryEntity> allNodes) {
        if (mLQDetailAdapter == null) {
            mLQDetailAdapter = new LQDetailAdapter(mActivity, R.layout.xxcx_item_lq_detail, allNodes);
            mRecyclerView.setAdapter(mLQDetailAdapter);
        } else {
            mLQDetailAdapter.addAll(allNodes);
        }
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
    public void _onPause() {
        super._onPause();
    }

}
