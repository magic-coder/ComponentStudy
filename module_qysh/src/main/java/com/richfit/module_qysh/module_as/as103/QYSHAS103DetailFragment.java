package com.richfit.module_qysh.module_as.as103;

import android.text.TextUtils;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_qysh.R;
import com.richfit.module_qysh.adapter.QHSYAS103DetailAdapter;
import com.richfit.module_qysh.module_as.as103.imp.QHSYAS103DetailPresenterImp;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHAS103DetailFragment extends BaseASDetailFragment<QHSYAS103DetailPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new QHSYAS103DetailPresenterImp(mActivity);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    /**
     * 注意这必须从重写该方法，尽管明细界面和标准的一致，但是缺少删除和修改功能，所以必须重新
     * 给出一个新的适配器。另外103的业务是有参考单据，但是没有父子节点所以删除的时候需要特别
     * 对待。
     * @param allNodes
     */
    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        if (mAdapter == null) {
            mAdapter = new QHSYAS103DetailAdapter(mActivity, R.layout.qysh_item_as103_item, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    /**
     * 修改节点的数据，这里我们需要给出已经其他节点的上架仓位集合。
     * @param node
     * @param position
     */
    @Override
    public void editNode(final RefDetailEntity node, int position) {
        String state = (String) SPrefUtil.getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账,不允许删除");
            return;
        }
        if (TextUtils.isEmpty(node.transLineId)) {
            showMessage("该行还未进行数据采集");
            return;
        }
        //获取与该子节点的物料编码和发出库位一致的发出仓位和接收仓位列表
        if (mAdapter != null && QHSYAS103DetailAdapter.class.isInstance(mAdapter)) {
            QHSYAS103DetailAdapter adapter = (QHSYAS103DetailAdapter) mAdapter;
            ArrayList<String> locations = adapter.getLocations(position, 0);
            mPresenter.editNode(locations, null,null, node, mCompanyCode,
                    mBizType, mRefType, getSubFunName(), position);
        }
    }

    /**
     * 必须重写删除结点的回调方法，因为103有参考入库不具有父子结点的结构
     * @param node
     * @param position
     */
    @Override
    public void deleteNode(final RefDetailEntity node, int position) {
        String state = (String) SPrefUtil.getData(mBizType + mRefType, "0");
        if (!"0".equals(state)) {
            showMessage("已经过账,不允许删除");
            return;
        }
        if (TextUtils.isEmpty(node.transLineId)) {
            showMessage("该行还未进行数据采集");
            return;
        }
        mPresenter.deleteNode("N", node.transId, node.transLineId, node.locationId,
                mRefData.refType, mRefData.bizType,node.refLineId, Global.USER_ID, position, mCompanyCode);
    }

    @Override
    public void submitBarcodeSystemSuccess() {
        setRefreshing(false, "过账成功");
        showSuccessDialog(mShowMsg);
        if (mAdapter != null) {
            mAdapter.removeAllVisibleNodes();
        }
        mRefData = null;
        mTransId = "";
        mShowMsg.setLength(0);
        mPresenter.showHeadFragmentByPosition(BaseFragment.HEADER_FRAGMENT_INDEX);
    }

    /**
     * 一步，upload+transfer
     * @return
     */
    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        return menus.subList(0,1);
    }

    @Override
    protected String getSubFunName() {
        return "103物资入库";
    }
}
