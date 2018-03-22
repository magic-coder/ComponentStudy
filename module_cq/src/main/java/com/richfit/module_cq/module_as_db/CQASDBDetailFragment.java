package com.richfit.module_cq.module_as_db;

import android.text.TextUtils;
import android.view.TextureView;

import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cq.adapter.As101Adapter;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;
import com.richfit.sdk_wzrk.base_as_detail.imp.ASDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/12/5.
 */

public class CQASDBDetailFragment extends BaseASDetailFragment<ASDetailPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new ASDetailPresenterImp(mActivity);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected String getSubFunName() {
        return "调拨出库";
    }

    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        if (mAdapter == null) {
            mAdapter = new As101Adapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }


    /**
     * 1.过账
     */
    @Override
    protected void submit2BarcodeSystem(String transToSapFlag) {
        //必须保证整单进行了采集才允许过账
        if (mAdapter instanceof As101Adapter) {
            As101Adapter adapter = (As101Adapter) mAdapter;
            if (!adapter.isTransferValid()) {
                showMessage("请先将所物料全部采集完毕");
                return;
            }
        }

        super.submit2BarcodeSystem(transToSapFlag);
    }

    @Override
    public void submitBarcodeSystemSuccess() {
        setRefreshing(false, "过账成功");
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

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        return menus.subList(0, 1);
    }
}
