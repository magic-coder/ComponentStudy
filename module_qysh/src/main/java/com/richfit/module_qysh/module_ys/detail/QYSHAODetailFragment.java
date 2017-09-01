package com.richfit.module_qysh.module_ys.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailFragment;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_qysh.R;
import com.richfit.module_qysh.adapter.QYSHAOAdapter;
import com.richfit.module_qysh.module_ys.detail.imp.AODetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2016/11/24.
 */

public class QYSHAODetailFragment extends BaseDetailFragment<AODetailPresenterImp, RefDetailEntity>
        implements IAODetailView<RefDetailEntity> {

    @Override
    protected int getContentId() {
        return R.layout.qysh_fragment_ao_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new AODetailPresenterImp(mActivity);
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
            showMessage("请现在抬头界面获取单据数据");
            return;
        }

        if (TextUtils.isEmpty(mRefData.recordNum)) {
            showMessage("请现在抬头界面输入验收单号");
            return;
        }

        if (TextUtils.isEmpty(mRefData.bizType)) {
            showMessage("未获取到业务类型");
            return;
        }

        startAutoRefresh();
    }


    @Override
    public void deleteNode(RefDetailEntity node, int position) {
        if ("Y".equals(node.lineInspectFlag)) {
            showMessage("该行已经过账,不允许删除");
            return;
        }
        if (TextUtils.isEmpty(node.totalQuantity) || "0".equals(node.totalQuantity)) {
            showMessage("该行还未进行数据采集!");
            return;
        }
        mPresenter.deleteNode("N", mRefData.recordNum, node.lineNum, node.refLineId,
                mRefData.refType, mRefData.bizType, Global.USER_ID, position, mCompanyCode);
    }

    @Override
    public void editNode(RefDetailEntity node, int position) {
        if ("Y".equals(node.lineInspectFlag)) {
            showMessage("该行已经过账,不允许编辑");
            return;
        }
        if (TextUtils.isEmpty(node.totalQuantity) || "0".equals(node.totalQuantity)) {
            showMessage("该行还未进行数据采集!");
            return;
        }
        mPresenter.editNode(null, null, null, node, mCompanyCode, mBizType, mRefType, "验收结果修改", position);
    }

    @Override
    public void deleteNodeSuccess(int position) {
        showMessage("删除成功");
        if (mAdapter != null) {
            mAdapter.removeNodeByPosition(position);
        }
        startAutoRefresh();
    }

    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        if (mAdapter == null) {
            mAdapter = new QYSHAOAdapter(mActivity, R.layout.qhsy_item_ao_item, allNodes);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    /**
     * 自动下拉刷新
     */
    @Override
    public void onRefresh() {
        //单号
        final String recordNum = mRefData.recordNum;
        //业务类型
        final String bizType = mRefData.bizType;
        //单据类型
        final String refType = mRefData.refType;
        //移动类型
        final String moveType = mRefData.moveType;
        //获取缓存累计数量缓存
        mPresenter.getReference(mRefData, recordNum, refType, bizType,
                moveType, "", Global.USER_ID);
    }


    @Override
    public void refreshComplete() {

    }

    @Override
    public boolean checkDataBeforeOperationOnDetail() {
        if (mRefData == null) {
            showMessage("请先获取验收清单");
            return false;
        }
        if (TextUtils.isEmpty(mRefData.refCodeId)) {
            showMessage("请先在抬头界面获取单据数据");
            return false;
        }

        if (TextUtils.isEmpty(mBizType)) {
            showMessage("未获取到业务类型");
            return false;
        }

        if (TextUtils.isEmpty(mRefType)) {
            showMessage("未获取到单据类型");
            return false;
        }

        if (mRefData.billDetailList == null || mRefData.billDetailList.size() == 0) {
            showMessage("该验收清单没有明细数据,不需要过账");
            return false;
        }
        return true;
    }

    /**
     * 显示过账，数据上传等菜单对话框。
     *
     * @param companyCode
     */
    @Override
    public void showOperationMenuOnDetail(final String companyCode) {
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(mActivity);
        dialog.setTitle("提示");
        dialog.setMessage("您真的需要过账该张验收单据吗?");
        dialog.setPositiveButton("确定", (dialogInterface, i) -> {
            submit2BarcodeSystem("");
            dialogInterface.dismiss();
        });
        dialog.setNegativeButton("取消", (dialogInterface, i) -> {
            dialogInterface.dismiss();
        });
        dialog.show();
    }

    /**
     * 第一步将数据上传到条码系统
     *
     * @param tranToSapFlag
     */
    @Override
    protected void submit2BarcodeSystem(String tranToSapFlag) {
        mExtraTansMap.clear();
        mExtraTansMap.put("refNum", mRefData.recordNum);
        mExtraTansMap.put("inspectionType",mRefData.inspectionType);
        mPresenter.submitData2BarcodeSystem(mRefData.refCodeId, mRefData.transId,
                mBizType, mRefType, Global.USER_ID, mRefData.voucherDate, tranToSapFlag, mExtraTansMap);
    }

    public void submitBarcodeSystemSuccess() {
        setRefreshing(false, "数据上传成功");
        showSuccessDialog(mShowMsg);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        //注意这里必须清除单据数据
        mRefData = null;
        mTransId = "";
        mShowMsg.setLength(0);
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }

    //第二步将数据上传到SAP
    @Override
    protected void submit2SAP(String tranToSapFlag) {

    }

    @Override
    public void submitSAPSuccess() {

    }

    //第三步将数据上传到SAP
    @Override
    protected void sapUpAndDownLocation(String transToSapFlag) {

    }

    @Override
    public void upAndDownLocationSuccess() {

    }


    @Override
    protected boolean checkTransStateBeforeRefresh() {
        return true;
    }
}
