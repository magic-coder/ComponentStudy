package com.richfit.module_cq.module_rs;

import android.widget.TextView;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cq.R;
import com.richfit.module_cq.adapter.As101Adapter;
import com.richfit.module_cq.adapter.RsyAdapter;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;
import com.richfit.sdk_wzrk.base_as_detail.imp.ASDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/12/5.
 */

public class CQRSDetailFragment extends BaseASDetailFragment<ASDetailPresenterImp> {

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_rsy_detail;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ASDetailPresenterImp(mActivity);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initView() {
        super.initView();
        TextView actQuantity = mView.findViewById(R.id.actQuantity);
        if(actQuantity != null) {
            actQuantity.setText("应退数量");
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void showNodes(List<RefDetailEntity> allNodes) {
        saveTransId(allNodes);
        if (mAdapter == null) {
            mAdapter = new RsyAdapter(mActivity, allNodes);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemEditAndDeleteListener(this);
            mAdapter.setAdapterStateListener(this);
        } else {
            mAdapter.addAll(allNodes);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int viewType) {
        if(viewType == Global.CHILD_NODE_HEADER_TYPE) {
            viewHolder.setText(R.id.quantity,"实退数量");
        }
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
        return menus.subList(0,1);
    }

    @Override
    protected String getSubFunName() {
        return "物资退库";
    }
}
