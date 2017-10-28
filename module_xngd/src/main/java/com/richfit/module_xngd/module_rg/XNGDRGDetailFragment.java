package com.richfit.module_xngd.module_rg;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.R;
import com.richfit.module_xngd.adapter.XNGDDSDetailAdapter;
import com.richfit.module_xngd.module_rg.imp.XNGDRDDetailPresenterImp;
import com.richfit.sdk_wzck.adapter.DSYDetailAdapter;
import com.richfit.sdk_wzck.base_ds_detail.BaseDSDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/6/20.
 */

public class XNGDRGDetailFragment extends BaseDSDetailFragment<XNGDRDDetailPresenterImp> {

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int viewType) {
        if(viewType == Global.CHILD_NODE_HEADER_TYPE || viewType == Global.CHILD_NODE_ITEM_TYPE) {
            holder.setVisible(R.id.tv_location_type, false);
        }

        if(holder.getItemViewType() == Global.CHILD_NODE_HEADER_TYPE) {
            holder.setText(R.id.quantity,"实退数量");
        }
    }

    @Override
    protected void initView() {
        actQuantityName.setText("应退数量");
        super.initView();
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initPresenter() {
        mPresenter = new XNGDRDDetailPresenterImp(mActivity);
    }

    /**
     * 如果不是标准的出库，需要重写该方法。
     */
    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        saveTurnFlag(allNodes);
        if (mAdapter == null) {
            mAdapter = new XNGDDSDetailAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        return menus.subList(0, 1);
    }

    /**
     * 第一步过账成功后直接跳转
     */
    @Override
    public void submitBarcodeSystemSuccess() {
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
    protected String getSubFunName() {
        return "采购退货";
    }
}
