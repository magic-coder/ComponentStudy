package com.richfit.sdk_wzyk.base_msn_detail;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailFragment;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzyk.R;
import com.richfit.sdk_wzyk.R2;
import com.richfit.sdk_wzyk.adapter.MSNDetailAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.richfit.common_lib.utils.SPrefUtil.getData;

/**
 * 注意对于无参考的明细界面，默认给出的PARENT_ITEM_NODE类型的节点。
 * 但是节点中并不存在节点类型信息。
 * Created by monday on 2016/11/20.
 */

public abstract class BaseMSNDetailFragment<P extends IMSNDetailPresenter> extends BaseDetailFragment<P, RefDetailEntity>
        implements IMSNDetailView<RefDetailEntity> {


    /*处理寄售转自有业务。主要的逻辑是用户点击过账按钮之后系统自动检查该缓存(子节点)中是否有特殊库存标识是否
    * 等于K而且特殊库存编号不为空。如果满足以上的条件，那么系统自动调用转自有的接口。如果转自有成功修改成员变量
    * isTurnSuccess为true。如果业务在上传数据的时候有第二步，那么需要检查该字段*/
    /*是否需要寄售转自有*/
    protected boolean isNeedTurn = false;
    /*转自有是否成功*/
    protected boolean isTurnSuccess = false;

    @Override
    protected int getContentId() {
        return R.layout.wzyk_fragment_msn_detail;
    }

    @Override
    protected void initDataLazily() {
        isNeedTurn = false;
        isTurnSuccess = false;
        startAutoRefresh();
    }

    /**
     * 显示标准无参考移库明细界面
     *
     * @param allNodes
     */
    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        saveTurnFlag(allNodes);
        if (mAdapter == null) {
            mAdapter = new MSNDetailAdapter(mActivity, R.layout.wzyk_item_msn_detail_item, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    /**
     * 刷新界面结束。注意如果用户切换界面(修改仓位等),那么系统不再自动过账
     */
    @Override
    final public void refreshComplete() {
        super.refreshComplete();
        if (!isNeedTurn && isTurnSuccess) {
            //如果寄售转自有成功后，系统自动去过账。
            submit2BarcodeSystem(mBottomMenus.get(0).transToSapFlag);
        }
    }

    protected void saveTransId(List<RefDetailEntity> allNodes) {
        for (RefDetailEntity node : allNodes) {
            if (!TextUtils.isEmpty(node.transId)) {
                mTransId = node.transId;
                break;
            }
        }
    }

    /**
     * 获取是否该明细是否需要转自有。
     *
     * @param nodes
     */
    private void saveTurnFlag(final List<RefDetailEntity> nodes) {
        for (RefDetailEntity node : nodes) {
            if ("Y".equalsIgnoreCase(node.specialConvert)) {
                isNeedTurn = true;
                break;
            }
        }
    }

    /**
     * 修改明细节点，注意无参考移库不具有父子节点的结构
     *
     * @param node
     * @param position
     */
    @Override
    public void editNode(final RefDetailEntity node, int position) {
        String state = (String) getData(mBizType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账,不允许删除");
            return;
        }
        //获取与该子节点的物料编码和发出库位一致的发出仓位和接收仓位列表
        if (mAdapter != null && MSNDetailAdapter.class.isInstance(mAdapter)) {
            MSNDetailAdapter adapter = (MSNDetailAdapter) mAdapter;
            ArrayList<String> sendLocations = adapter.getLocations(node.materialNum, node.invId, position, 0);
            ArrayList<String> recLocations = adapter.getLocations(node.materialNum, node.invId, position, 1);
            mPresenter.editNode(sendLocations, recLocations, null, node, mCompanyCode,
                    mBizType, mRefType, getSubFunName(), position);
        }
    }

    /**
     * 删除节点
     *
     * @param node
     * @param position
     */
    @Override
    public void deleteNode(final RefDetailEntity node, int position) {
        String state = (String) getData(mBizType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账,不允许删除");
            return;
        }
        if (TextUtils.isEmpty(node.transLineId)) {
            showMessage("该行还未进行数据采集");
            return;
        }
        mPresenter.deleteNode("N", node.transId, node.transLineId, node.locationId,
                mRefData.refType, mRefData.bizType,node.refLineId,Global.USER_ID, position, mCompanyCode);
    }

    /**
     * 删除成功后回到该方法，注意这里是无参考明细界面直接将该节点移除。
     *
     * @param position：节点在明细列表的位置
     */
    @Override
    public void deleteNodeSuccess(int position) {
        showMessage("删除成功");
        if (mAdapter != null) {
            mAdapter.removeItemByPosition(position);
        }
    }

    @Override
    public void deleteNodeFail(String message) {
        showMessage(message);
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
        String transferFlag = (String) getData(mBizType, "0");
        if ("1".equals(transferFlag)) {
            showMessage("本次采集已经过账,请先进行其他转储操作");
            return;
        }
        mExtraTansMap.clear();
        mPresenter.submitData2BarcodeSystem(mRefData.refCodeId, mTransId, mRefData.bizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, mExtraTansMap);
    }


    /**
     * 第一步过账成功后显示物料凭证
     */
    @Override
    public void submitBarcodeSystemSuccess() {
        showSuccessDialog(mShowMsg);
    }

    /**
     * 2.数据上传
     */
    protected void submit2SAP(String transToSapFlag) {
        String state = (String) SPrefUtil.getData(mBizType, "0");
        if ("0".equals(state)) {
            showMessage("请先过账");
            return;
        }
        mExtraTansMap.clear();
        mPresenter.submitData2SAP(mTransId, mRefData.bizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, mExtraTansMap);
    }


    /**
     * 第二步转储成功后跳转到抬头界面
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
    protected void startTurnOwnSupplies(String transToSapFlag) {
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

    /*子类返回修改模块的名称*/
    protected abstract String getSubFunName();
}
