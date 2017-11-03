package com.richfit.module_xngd.module_rg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

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
    public void onBindViewHolder(ViewHolder holder, int viewType) {
        super.onBindViewHolder(holder, viewType);
        if (holder.getItemViewType() == Global.CHILD_NODE_HEADER_TYPE) {
            holder.setText(R.id.quantity, "实退数量");
        }
    }

    @Override
    protected void initView() {
        super.initView();
        TextView actQuantity = mView.findViewById(R.id.actQuantity);
        if (actQuantity != null)
            actQuantity.setText("应退数量");
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
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        return menus.subList(0, 1);
    }

    @Override
    protected String getSubFunName() {
        return "采购退货";
    }
}
