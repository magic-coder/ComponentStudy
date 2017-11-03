package com.richfit.sdk_wzyk.base_ms_detail;

import android.text.TextUtils;
import android.widget.TextView;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailFragment;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.TreeNode;
import com.richfit.sdk_wzyk.R;
import com.richfit.sdk_wzyk.R2;
import com.richfit.sdk_wzyk.adapter.MSYDetailAdapter;

import java.util.List;

import butterknife.BindView;

import static com.richfit.common_lib.utils.SPrefUtil.getData;

/**
 * 有参考移库的明细基类
 * Created by monday on 2017/2/10.
 */

public abstract class BaseMSDetailFragment<P extends IMSDetailPresenter> extends BaseDetailFragment<P, RefDetailEntity>
        implements IMSDetailView<RefDetailEntity> {

    /*处理寄售转自有业务。主要的逻辑是用户点击过账按钮之后系统自动检查该缓存(子节点)中是否有特殊库存标识是否
    * 等于K而且特殊库存编号不为空。如果满足以上的条件，那么系统自动调用转自有的接口。如果转自有成功修改成员变量
    * isTurnSuccess为true。如果业务在上传数据的时候有第二步，那么需要检查该字段*/
    /*是否需要寄售转自有*/
    protected boolean isNeedTurn = false;
    /*转自有是否成功*/
    protected boolean isTurnSuccess = false;

    @Override
    protected int getContentId() {
        return R.layout.wzyk_fragment_base_msy_detail;
    }

    @Override
    protected void initDataLazily() {

        if (mRefData == null) {
            showMessage("请现在抬头界面获取单据数据");
            return;
        }

        if (isEmpty(mRefData.recordNum)) {
            showMessage("请现在抬头界面输入参考单号");
            return;
        }

        if (isEmpty(mRefData.bizType)) {
            showMessage("未获取到业务类型");
            return;
        }

        if (isEmpty(mRefData.refType)) {
            showMessage("未获取到单据类型");
            return;
        }
        //这里先将寄售转自有的相关标记清空
        isNeedTurn = false;
        isTurnSuccess = false;
        startAutoRefresh();
    }

    /**
     * 显示物资移库明细界面，注意这里显示的标准界面
     *
     * @param allNodes
     */
    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        saveTurnFlag(allNodes);
        if (mAdapter == null) {
            mAdapter = new MSYDetailAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    @Override
    public void refreshComplete() {
        super.refreshComplete();
        if (!isNeedTurn && isTurnSuccess) {
            //如果寄售转自有成功后，系统自动去过账。
            submit2BarcodeSystem(mBottomMenus.get(0).transToSapFlag);
        }
    }

    /**
     * 获取是否该明细是否需要转自有。
     *
     * @param nodes
     */
    protected void saveTurnFlag(final List<RefDetailEntity> nodes) {
        //仅仅检查子节点
        for (RefDetailEntity node : nodes) {
            if (Global.CHILD_NODE_ITEM_TYPE == node.getViewType() &&
                    "Y".equalsIgnoreCase(node.specialConvert)) {
                isNeedTurn = true;
                break;
            }
        }
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
        mPresenter.editNode(null, null, mRefData, node, mCompanyCode, mBizType, mRefType,
                getSubFunName(), -1);
    }

    @Override
    public void deleteNode(final RefDetailEntity node, int position) {
        String state = (String) getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账,不允许删除");
            return;
        }
        if (TextUtils.isEmpty(node.transLineId)) {
            showMessage("该行还未进行数据采集");
            return;
        }
        TreeNode parentNode = node.getParent();
        String lineDeleteFlag;
        if (parentNode == null) {
            lineDeleteFlag = "N";
        } else {
            lineDeleteFlag = parentNode.getChildren().size() > 1 ? "N" : "Y";
        }
        mPresenter.deleteNode(lineDeleteFlag, node.transId, node.transLineId,
                node.locationId, mRefData.refType, mRefData.bizType,node.refLineId,Global.USER_ID,
                position, mCompanyCode);
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
    public void deleteNodeFail(String message) {
        showMessage("删除失败;" + message);
    }

    @Override
    public boolean checkDataBeforeOperationOnDetail() {
        if (mRefData == null) {
            showMessage("请先获取单据数据");
            return false;
        }
        if (TextUtils.isEmpty(mTransId)) {
            showMessage("未获取缓存标识");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.voucherDate)) {
            showMessage("请先选择过账日期");
            return false;
        }
        return true;
    }


    /**
     * 1.过账
     */
    @Override
    protected void submit2BarcodeSystem(String transToSapFlag) {
        //如果需要寄售转自有但是没有成功过，都需要用户需要再次寄售转自有
        if (isNeedTurn && !isTurnSuccess) {
            startTurnOwnSupplies("07");
            return;
        }
        String state = (String) SPrefUtil.getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账成功,请进行其他转储");
            return;
        }
        mShowMsg.setLength(0);
        mExtraTansMap.clear();
        mPresenter.submitData2BarcodeSystem(mRefData.refCodeId, mTransId, mBizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, null);
    }

    /**
     * 第一步的过账(Transfer 01)成功后，将状态标识设置为1，
     * 本次出库不在允许对该张单据进行任何操作。
     */
    @Override
    public void submitBarcodeSystemSuccess() {
        showSuccessDialog(mShowMsg);
    }

    /**
     * 2.数据上传
     */
    protected void submit2SAP(String transToSapFlag) {
        //如果没有进行第一步的过账，那么不允许数据上传
        String state = (String) getData(mBizType + mRefType, "0");
        if ("0".equals(state)) {
            showMessage("请先过账");
            return;
        }
        mShowMsg.setLength(0);
        mExtraTansMap.clear();
        mPresenter.submitData2SAP(mTransId, mRefData.bizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, null);
    }

    /**
     * 第二步(Transfer 05)成功后清除明细数据，跳转到抬头界面。
     */
    @Override
    public void submitSAPSuccess() {
        setRefreshing(false, "转储成功");
        showSuccessDialog(mShowMsg);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        mRefData = null;
        mShowMsg.setLength(0);
        mTransId = "";
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }


    /**
     * 3. 如果submitFlag:2那么分三步进行转储处理
     */
    @Override
    protected void sapUpAndDownLocation(String transToSapFlag) {

    }

    @Override
    public void upAndDownLocationSuccess() {

    }


    /**
     * 开始寄售转自有
     *
     * @param transToSapFlag
     */
    private void startTurnOwnSupplies(String transToSapFlag) {
        if (isEmpty(mTransId)) {
            showMessage("未获取到缓存,请先获取采集数据");
            return;
        }
        mShowMsg.setLength(0);
        mPresenter.turnOwnSupplies(mTransId, mRefData.bizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, null, -1);
    }

    /**
     * 寄售转自有成功
     */
    @Override
    public void turnOwnSuppliesSuccess() {
        isTurnSuccess = true;
        isNeedTurn = false;
        startAutoRefresh();
    }

    /**
     * 寄售转自有失败
     */
    @Override
    public void turnOwnSuppliesFail(String message) {
        showErrorDialog(TextUtils.isEmpty(message) ? "寄售转自有失败" : message);
        isTurnSuccess = false;
        isNeedTurn = true;
    }

    @Override
    protected boolean checkTransStateBeforeRefresh() {
        String transferKey = (String) SPrefUtil.getData(mBizType + mRefType, "0");
        if ("1".equals(transferKey)) {
            setRefreshing(false, "本次采集已经过账,请直接进行其他转储操作");
            return false;
        }
        return true;
    }

    protected abstract String getSubFunName();

}
