package com.richfit.sdk_wzrk.base_asn_detail;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailFragment;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_wzrk.R;
import com.richfit.sdk_wzrk.adapter.ASNDetailAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2016/11/27.
 */

public abstract class BaseASNDetailFragment<P extends IASNDetailPresenter> extends BaseDetailFragment<P, RefDetailEntity>
        implements IASNDetailView<RefDetailEntity> {

    @Override
    protected int getContentId() {
        return R.layout.wzrk_fragment_asn_detail;
    }

    @Override
    protected void initDataLazily() {
        if (mRefData == null) {
            setRefreshing(false, "获取明细失败,请现在抬头界面选择相应的参数");
            return;
        }

        if (TextUtils.isEmpty(mRefData.workId)) {
            setRefreshing(false, "获取明细失败,请先选择工厂");
            return;
        }
        startAutoRefresh();
    }


    @Override
    public void showNodes(List<RefDetailEntity> nodes) {
        for (RefDetailEntity node : nodes) {
            if (!TextUtils.isEmpty(node.transId)) {
                mTransId = node.transId;
                break;
            }
        }
        if (mAdapter == null) {
            mAdapter = new ASNDetailAdapter(mActivity, R.layout.wzrk_item_asn_parent_item, nodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(nodes);
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
        String state = (String) SPrefUtil.getData(mBizType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账,不允许修改");
            return;
        }
        //获取与该子节点的物料编码和发出库位一致的发出仓位和接收仓位列表
        if (mAdapter != null && ASNDetailAdapter.class.isInstance(mAdapter)) {
            ASNDetailAdapter adapter = (ASNDetailAdapter) mAdapter;
            ArrayList<String> Locations = adapter.getLocations(position, 0);
            mPresenter.editNode(Locations, null, null, node, mCompanyCode, mBizType, mRefType,
                    "其他入库-无参考", position);
        }
    }


    @Override
    public void deleteNode(final RefDetailEntity node, int position) {
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
                mRefData.refType, mRefData.bizType,node.refLineId,Global.USER_ID, position, mCompanyCode);
    }

    /**
     * 重写删除成功的回调，因为父类实现实现的有参考的逻辑
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
        String transferFlag = (String) SPrefUtil.getData(mBizType, "0");
        if ("1".equals(transferFlag)) {
            showMessage(getString(R.string.msg_detail_off_location));
            return;
        }
        mShowMsg.setLength(0);
        mExtraTansMap.clear();
        mExtraTansMap.put("supplierNum",mRefData.supplierNum);
        mPresenter.submitData2BarcodeSystem("",mTransId, mBizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, transToSapFlag, mExtraTansMap);
    }

    /**
     * 过账成功显示物料凭证
     */
    @Override
    public void submitBarcodeSystemSuccess() {
        showSuccessDialog(mShowMsg);
    }

    /**
     * 2.上架处理
     */
    @Override
    protected void submit2SAP(String tranToSapFlag) {
        String state = (String) SPrefUtil.getData(mBizType, "0");
        if ("0".equals(state)) {
            showMessage("请先过账");
            return;
        }
        mShowMsg.setLength(0);
        mExtraTansMap.clear();
        mPresenter.submitData2SAP(mTransId, mBizType, mRefType, Global.USER_ID,
                mRefData.voucherDate, tranToSapFlag, mExtraTansMap);
    }

    /**
     * 上架成功后跳转到抬头屏幕
     */
    @Override
    public void submitSAPSuccess() {
        setRefreshing(false, "数据上传成功");
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
     * 这里重写的目的是只显示两个按钮
     *
     * @return
     */
    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        return menus.subList(0, 2);
    }

    @Override
    protected void sapUpAndDownLocation(String transToSapFlag) {

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
}
