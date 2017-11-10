package com.richfit.sdk_wzrk.base_as_detail;

import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailFragment;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.TreeNode;
import com.richfit.sdk_wzrk.R;
import com.richfit.sdk_wzrk.R2;
import com.richfit.sdk_wzrk.adapter.ASYDetailAdapter;

import java.util.List;

import butterknife.BindView;


/**
 * 物资基础类，从这里开始确定入库明细界面的布局。
 * 注意这里已经到了物资入库，已经确定了明细界面数据的具体类型，所以
 * 可以提供有关于明细数据的相关数据。
 * Created by monday on 2017/3/17.
 */

public abstract class BaseASDetailFragment<P extends IASDetailPresenter> extends
        BaseDetailFragment<P, RefDetailEntity> implements IASDetailView<RefDetailEntity> {



    @Override
    protected int getContentId() {
        return R.layout.wzrk_fragment_base_asy_detail;
    }

    /**
     * 判断有参考情况下，抬头需要采集的基本字段信息。
     * 子类如果需要检测更多的字段应该重写该方法。
     */
    @Override
    protected void initDataLazily() {

        if (mRefData == null) {
            showMessage("请现在抬头界面获取单据数据");
            return;
        }

        if (TextUtils.isEmpty(mRefData.recordNum)) {
            showMessage("请现在抬头界面输入验收清单号");
            return;
        }

        if (TextUtils.isEmpty(mRefData.bizType)) {
            showMessage("未获取到业务类型");
            return;
        }

        if (TextUtils.isEmpty(mRefData.refType)) {
            showMessage("未获取到单据类型");
            return;
        }
        //开始刷新
        startAutoRefresh();
    }

    /**
     * 显示物资入库明细界面，注意这里显示的标准界面
     *
     * @param allNodes
     */
    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        if (mAdapter == null) {
            mAdapter = new ASYDetailAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    /**
     * 修改明细里面的子节点。注意如果该明细界面不具有父子节点结构那么需要重写该方法。
     *
     * @param node
     * @param position
     */
    @Override
    public void editNode(RefDetailEntity node, int position) {
        String state = (String) SPrefUtil.getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账,不允许修改");
            return;
        }
        mPresenter.editNode(null, null, mRefData, node, mCompanyCode, mBizType,
                mRefType, getSubFunName(), -1);
    }

    /**
     * 删除子节点信息。
     *
     * @param node
     * @param position
     */
    @Override
    public void deleteNode(RefDetailEntity node, int position) {
        String state = (String) SPrefUtil.getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账,不允许删除");
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
                node.locationId, mRefData.refType, mRefData.bizType, node.refLineId, Global.USER_ID, position, mCompanyCode);
    }

    /**
     * 删除明细节点成功。如果不具有父子节点结构的明细界面，那么子类需要重写
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
     * 显示过账，上传等底部菜单之前进行必要的检查。注意子类可以根据自己的需求
     * 自行添加检查的字段。父类仅仅做了最基本的检查。
     *
     * @return
     */
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
        String state = (String) SPrefUtil.getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage(getString(R.string.msg_detail_on_location));
            return;
        }
        mShowMsg.setLength(0);
        mExtraTansMap.clear();
        mPresenter.submitData2BarcodeSystem(mRefData.refCodeId, mTransId, mBizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, null);
    }

    /**
     * 第一步过账成功显示物料凭证
     */
    @Override
    public void submitBarcodeSystemSuccess() {
        showSuccessDialog(mShowMsg);
    }


    /**
     * 2.数据上传
     */
    protected void submit2SAP(String transToSapFlag) {
        String state = (String) SPrefUtil.getData(mBizType + mRefType, "0");
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
     * 第二步数据上传成功
     */
    @Override
    public void submitSAPSuccess() {
        setRefreshing(false, "上架成功");
        showSuccessDialog(mShowMsg);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        //注意这里必须清除单据数据
        mRefData = null;
        mShowMsg.setLength(0);
        mTransId = "";
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }

    /**
     * 第三步转储入口
     *
     * @param transToSapFlag
     */
    protected void sapUpAndDownLocation(String transToSapFlag) {

    }


    @Override
    public void upAndDownLocationSuccess() {

    }

    @Override
    protected boolean checkTransStateBeforeRefresh() {
        String transferFlag = (String) SPrefUtil.getData(mBizType + mRefType, "0");
        if ("1".equals(transferFlag)) {
            showMessage(getString(R.string.msg_detail_on_location));
            return false;
        }
        return true;
    }

    /*子类返回修改模块的名称*/
    protected abstract String getSubFunName();
}
