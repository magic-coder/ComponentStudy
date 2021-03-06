package com.richfit.module_qhyt.module_as.as_ww_component.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailFragment;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_qhyt.R;
import com.richfit.module_qhyt.adapter.QHYTASWWCAdapter;

import java.util.List;

import static com.richfit.common_lib.utils.SPrefUtil.getData;

/**
 * 青海委外出库组件明细界面。注意委外入库的组件仅仅提供明细的删除和修改功能
 * Created by monday on 2017/3/10.
 */

public class QHYTASWWCDetailFragment extends BaseDetailFragment<QYHTASWWCDetailPresenterImp, RefDetailEntity>
        implements QYHTASWWCDetailContract.IQingHaiWWCDetailView<RefDetailEntity> {


    //当前委外入库真正操作的行号，该行号用来获取对应的行明细
    String mSelectedRefLineNum;

    @Override
    protected int getContentId() {
        return R.layout.qhyt_fragment_wwc_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new QYHTASWWCDetailPresenterImp(mActivity);
    }


    /**
     * 组件首先读取产成品委外入库正在采集的单据行号
     *
     * @param savedInstanceState
     */
    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        super.initVariable(savedInstanceState);
        Bundle bundle = getArguments();
        mRefDetail = null;
        if (bundle != null) {
            mSelectedRefLineNum = bundle.getString(Global.EXTRA_REF_LINE_NUM_KEY);
        }
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        if (mRefData == null) {
            showMessage("请现在抬头界面获取单据数据");
            return;
        }

        if (TextUtils.isEmpty(mRefData.recordNum)) {
            showMessage("请现在抬头界面输入验收清单号");
            return;
        }

        if (TextUtils.isEmpty(mBizType)) {
            showMessage("未获取到业务类型");
            return;
        }

        if (TextUtils.isEmpty(mRefType)) {
            showMessage("未获取到单据类型");
            return;
        }

        if (TextUtils.isEmpty(mSelectedRefLineNum)) {
            showMessage("未获取到明细行行号");
            return;
        }
        startAutoRefresh();
    }

    @Override
    public void initDataLazily() {
        initData();
    }

    /**
     * 自动下拉刷新。注意这我们重写了onRefresh方法，因为组件获取整单缓存需要先获取
     * 单据数据，所以组件的getTransferInfo接口的参数较多,为了不修改IBaseDetailPresenter接口
     * 里面对应的方法，在QingHaiWWCDetailContract里面增加自己独有的接口方法。
     */
    @Override
    public void onRefresh() {
        //单据抬头id
        final String refCodeId = mRefData.refCodeId;
        //清除缓存id
        mTransId = "";
        //清除过账凭证
        mShowMsg.setLength(0);
        //获取缓存累计数量不对
        mRefDetail = null;
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        if (lineData != null) {
            mPresenter.getTransferInfo(mRefData.recordNum, refCodeId, mBizType, mRefType,
                    "", lineData.refLineId, Global.USER_ID);
        }
    }

    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        for (RefDetailEntity node : allNodes) {
            if (!TextUtils.isEmpty(node.transId)) {
                mTransId = node.transId;
                break;
            }
        }
        mRefDetail = allNodes;
        if (mAdapter == null) {
            mAdapter = new QHYTASWWCAdapter(mActivity, R.layout.qhyt_item_wwc_item,
                    allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    @Override
    public void refreshComplete() {

    }

    /**
     * 修改明细里面的子节点
     *
     * @param node
     * @param position
     */
    @Override
    public void editNode(final RefDetailEntity node, int position) {
        String state = (String) getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账,不允许修改");
            return;
        }
        mPresenter.editNode(null, null, mRefData, node, mCompanyCode, mBizType,
                mRefType, "委外入库组件", position);
    }

    @Override
    public void deleteNode(final RefDetailEntity node, int position) {
        String state = (String) getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账,不允许修改");
            return;
        }
        if (TextUtils.isEmpty(node.transLineId)) {
            showMessage("该行还未进行数据采集");
            return;
        }
        mPresenter.deleteNode("N", node.transId, node.transLineId,
                node.locationId, mRefData.refType, mRefData.bizType,
                node.refLineId,Global.USER_ID, position, mCompanyCode);
    }

    /**
     * 注意该业务相当于有参考，但是没有父子节点结构的明细删除逻辑
     *
     * @param position：节点在明细列表的位置
     */
    @Override
    public void deleteNodeSuccess(int position) {
        showMessage("删除成功");
        if (mAdapter != null) {
            mAdapter.removeNodeByPosition(position);
        }
        startAutoRefresh();
    }

    /**
     * 由于没有过账数据上传等逻辑所以这里直接刷新界面接口
     *
     * @return
     */
    @Override
    protected boolean checkTransStateBeforeRefresh() {
        return true;
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
    public boolean isNeedShowFloatingButton() {
        return false;
    }

    /**
     * 通过单据行的行号得到该行在单据明细列表中的位置
     *
     * @param lineNum:单据行号
     * @return 返回该行号对应的行明细在明细列表的索引
     */
    protected int getIndexByLineNum(String lineNum) {
        int index = -1;
        if (TextUtils.isEmpty(lineNum))
            return index;

        if (mRefData == null || mRefData.billDetailList == null
                || mRefData.billDetailList.size() == 0)
            return index;

        for (RefDetailEntity detailEntity : mRefData.billDetailList) {
            index++;
            if (lineNum.equalsIgnoreCase(detailEntity.lineNum))
                break;

        }
        return index;
    }

    /**
     * 获取行明细
     *
     * @param lineNum:单据行号
     * @return
     */
    protected RefDetailEntity getLineData(String lineNum) {
        if(TextUtils.isEmpty(lineNum))
            return null;
        int lineIndex = getIndexByLineNum(lineNum);
        if (lineIndex < 0) {
            mRefData.billDetailList.get(0);
        }
        return mRefData.billDetailList.get(lineIndex);
    }
}
