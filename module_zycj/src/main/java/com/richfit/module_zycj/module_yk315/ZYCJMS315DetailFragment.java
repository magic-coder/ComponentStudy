package com.richfit.module_zycj.module_yk315;

import android.view.View;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_zycj.R;
import com.richfit.sdk_wzyk.base_msn_detail.BaseMSNDetailFragment;
import com.richfit.sdk_wzyk.base_msn_detail.imp.MSNDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/11/3.
 */

public class ZYCJMS315DetailFragment extends BaseMSNDetailFragment<MSNDetailPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSNDetailPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        super.initView();
        //隐藏接收仓位和接收批次
        View recLocation = mView.findViewById(R.id.recLocation);
        if(recLocation != null) {
            recLocation.setVisibility(View.GONE);
        }

        View recBatchFlag = mView.findViewById(R.id.recBatchFlag);
        if(recBatchFlag != null) {
            recBatchFlag.setVisibility(View.GONE);
        }
        //修改发出仓位和发出批次
        TextView sendBatchFlag = mView.findViewById(R.id.sendBatchFlag);
        if(sendBatchFlag != null) {
            sendBatchFlag.setText("接收批次");
        }

        TextView sendLocation = mView.findViewById(R.id.sendLocation);
        if(sendLocation != null) {
            sendLocation.setText("接收仓位");
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected String getSubFunName() {
        return "315物资移库";
    }

    @Override
    protected boolean checkTransStateBeforeRefresh() {
        return true;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int viewType) {
        super.onBindViewHolder(holder,viewType);
        holder.setVisible(R.id.recLocation,false);
        holder.setVisible(R.id.recBatchFlag,false);
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

}
