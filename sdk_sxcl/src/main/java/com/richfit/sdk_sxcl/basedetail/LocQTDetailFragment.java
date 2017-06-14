package com.richfit.sdk_sxcl.basedetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailFragment;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.TreeNode;
import com.richfit.sdk_sxcl.R;
import com.richfit.sdk_sxcl.adapter.LocQTDetailAdapter;
import com.richfit.sdk_sxcl.basedetail.imp.LocQTPresenterImp;

import java.util.ArrayList;
import java.util.List;

import static com.richfit.common_lib.utils.SPrefUtil.getData;

/**
 * Created by monday on 2017/5/25.
 */

public class LocQTDetailFragment extends BaseDetailFragment<LocQTPresenterImp, RefDetailEntity>
        implements ILocQTDetailView {

    @Override
    protected int getContentId() {
        return R.layout.sxcl_fragment_locqt_detail;
    }

    @Override
    public void initPresenter() {
        mPresenter = new LocQTPresenterImp(mActivity);
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
        startAutoRefresh();
    }


    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        if (mAdapter == null) {
            mAdapter = new LocQTDetailAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    @Override
    public void refreshComplete() {
        setRefreshing(true, "获取明细缓存成功");
    }

    @Override
    public void editNode(RefDetailEntity node, int position) {
        String state = (String) getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账,不允许修改");
            return;
        }
        mPresenter.editNode(null, null, mRefData, node, mCompanyCode, mBizType,
                mRefType, "上下架处理", -1);
    }

    @Override
    public void deleteNode(RefDetailEntity node, int position) {
        String state = (String) getData(mBizType, "0");
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
                node.locationId, mRefData.refType, mRefData.bizType, position, mCompanyCode);
    }

    @Override
    public void deleteNodeSuccess(int position) {
        showMessage("删除成功");
        if (mAdapter != null) {
            mAdapter.removeNodeByPosition(position);
        }
        startAutoRefresh();
    }

    protected List<BottomMenuEntity> provideDefaultBottomMenu() {
        ArrayList<BottomMenuEntity> menus = new ArrayList<>();
        BottomMenuEntity menu = new BottomMenuEntity();
        menu.menuName = "上下架处理";
        menu.menuImageRes = R.mipmap.icon_transfer;
        menu.transToSapFlag = "05";
        menus.add(menu);
        return menus;
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
            showMessage("请先采集数据");
            return false;
        }
        return true;
    }

    @Override
    protected void submit2BarcodeSystem(String transToSapFlag) {
        String state = (String) SPrefUtil.getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage(getString(R.string.msg_detail_off_location));
            return;
        }
        mTransNum = "";
        mPresenter.submitData2BarcodeSystem(mTransId, mBizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, null);
    }

    @Override
    public void submitBarcodeSystemSuccess() {
        setRefreshing(false, "下架成功");
        showSuccessDialog(mTransNum);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        //注意这里必须清除单据数据
        mRefData = null;
        mTransNum = "";
        mTransId = "";
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }

    @Override
    public void submitBarcodeSystemFail(String message) {
        mTransNum = "";
        showErrorDialog(message);
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
        return true;
    }


    @Override
    protected void submit2SAP(String tranToSapFlag) {

    }

    @Override
    protected void sapUpAndDownLocation(String transToSapFlag) {

    }

}
