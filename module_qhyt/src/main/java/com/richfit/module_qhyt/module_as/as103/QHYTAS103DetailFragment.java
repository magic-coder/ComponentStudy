package com.richfit.module_qhyt.module_as.as103;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_qhyt.R;
import com.richfit.module_qhyt.adapter.QHYTAS103DetailAdapter;
import com.richfit.module_qhyt.module_as.as103.imp.QHYTAS103DetailPresenterImp;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 103入库只有过账。注意103是有参考入库，但是没有父子节点结构。
 * 所以我们需要重写Presenter层的获取整单缓存的逻辑
 * 在删除节点的时候需要区别无参考的入库明细，也就是删除成功仅仅
 * 刷新节点的某些字段。另外103入库没有批次，和上架信息。
 * Created by monday on 2017/2/17.
 */

public class QHYTAS103DetailFragment extends BaseASDetailFragment<QHYTAS103DetailPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new QHYTAS103DetailPresenterImp(mActivity);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

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
            mAdapter = new QHYTAS103DetailAdapter(mActivity, R.layout.qhyt_item_as103_item,
                    allNodes);
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
        if (mAdapter != null && QHYTAS103DetailAdapter.class.isInstance(mAdapter)) {
            QHYTAS103DetailAdapter adapter = (QHYTAS103DetailAdapter) mAdapter;
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
     * 第一步过账成功，重写该方法直接跳转到抬头页面。
     */
    @Override
    public void submitBarcodeSystemSuccess() {
        showMessage("过账成功");
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


    /**
     * 只有过账按钮
     *
     * @return
     */
    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        return menus.subList(0, 1);
    }



    @Override
    public boolean checkTransStateBeforeRefresh() {
        //由于103只有一步过账，所以直接任何情况下进入该页面都需要刷新
        return true;
    }

    @Override
    protected String getSubFunName() {
        return "物资入库-103";
    }
}


