package com.richfit.module_xngd.module_rs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_xngd.R;
import com.richfit.module_xngd.adapter.XNGDRSDetailAdapter;
import com.richfit.module_xngd.module_rs.imp.XNGDRSDetailPresenterImp;
import com.richfit.sdk_wzrk.adapter.ASYDetailAdapter;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/6/21.
 */

public class XNGDRSDetailFragment extends BaseASDetailFragment<XNGDRSDetailPresenterImp> {

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_rs_detail;
    }


    @Override
    public void initPresenter() {
        mPresenter = new XNGDRSDetailPresenterImp(mActivity);
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
    public void onBindViewHolder(ViewHolder viewHolder, int viewType) {
        super.onBindViewHolder(viewHolder, viewType);
        if (viewType == Global.CHILD_NODE_HEADER_TYPE) {
            viewHolder.setText(R.id.quantity, "实退数量");
        }
    }

    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        if (mAdapter == null) {
            mAdapter = new XNGDRSDetailAdapter(mActivity, allNodes);
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
        return "退库申请";
    }
}