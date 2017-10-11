package com.richfit.module_cqyt.module_ws.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailFragment;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.adapter.WSAdapter;
import com.richfit.sdk_wzrk.adapter.ASNDetailAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/9/21.
 */

public class WSDetailFragment extends BaseDetailFragment<WSDetailPresenterImp, RefDetailEntity>
        implements IWSDetailView<RefDetailEntity> {

    @Override
    protected int getContentId() {
        return R.layout.cqyt_fragment_ws_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new WSDetailPresenterImp(mActivity);
    }


    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        mRefData = null;
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
            showMessage("请先在抬头界面输入相应的数据");
            return;
        }
        if (TextUtils.isEmpty(mRefData.workCode)) {
            showMessage("请先在抬头界面选择工厂");
            return;
        }
        if (TextUtils.isEmpty(mRefData.voucherDate)) {
            showMessage("请先在抬头界面选择取样日期");
            return;
        }
        startAutoRefresh();
    }

    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        //显示明细数据
        for (RefDetailEntity node : allNodes) {
            if (!TextUtils.isEmpty(node.transId)) {
                mTransId = node.transId;
                break;
            }
        }
        if (mAdapter == null) {
            mAdapter = new WSAdapter(mActivity, R.layout.cqyt_item_ws_item, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }


    @Override
    public void deleteNode(RefDetailEntity node, int position) {
        String state = (String) SPrefUtil.getData(mBizType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账,不允许删除");
            return;
        }
        if (TextUtils.isEmpty(node.transLineId)) {
            showMessage("该行还未进行数据采集");
            return;
        }
        mPresenter.deleteNode("N", node.transId, node.transLineId, node.locationId,
                mRefData.refType, mRefData.bizType, node.refLineId, Global.USER_ID, position, mCompanyCode);
    }

    @Override
    public void editNode(RefDetailEntity node, int position) {
        String state = (String) SPrefUtil.getData(mBizType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账,不允许修改");
            return;
        }
        //获取与该子节点的物料编码和发出库位一致的发出仓位和接收仓位列表
        mPresenter.editNode(null, null, null, node, mCompanyCode, mBizType, mRefType,
                "物资取样", position);
    }


    @Override
    public void refreshComplete() {

    }

    @Override
    public void deleteNodeSuccess(int position) {
        showMessage("删除成功");
        if (mAdapter != null) {
            mAdapter.removeItemByPosition(position);
        }
    }

    @Override
    protected void submit2BarcodeSystem(String transToSapFlag) {
        String transferFlag = (String) SPrefUtil.getData(mBizType, "0");
        if ("1".equals(transferFlag)) {
            showMessage(getString(com.richfit.sdk_wzrk.R.string.msg_detail_off_location));
            return;
        }
        mShowMsg.setLength(0);
        mExtraTansMap.clear();
        mPresenter.submitData2BarcodeSystem("", mTransId, mBizType, mRefType, mRefData.voucherDate,
                mRefData.voucherDate, transToSapFlag, mExtraTansMap);
    }

    @Override
    public void submitBarcodeSystemSuccess() {
        showSuccessDialog(mShowMsg);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        mRefData = null;
        mShowMsg.setLength(0);
        mTransId = "";
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }


    @Override
    public void submitSAPSuccess() {

    }

    @Override
    public void upAndDownLocationSuccess() {

    }


    @Override
    protected boolean checkTransStateBeforeRefresh() {
        String state = (String) SPrefUtil.getData(mBizType, "0");
        if (!"0".equals(state)) {
            showMessage(getString(R.string.msg_detail_on_location));
            return false;
        }
        return true;
    }


    @Override
    protected void submit2SAP(String tranToSapFlag) {

    }

    @Override
    protected void sapUpAndDownLocation(String transToSapFlag) {

    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        return tmp.subList(0, 1);
    }
}
