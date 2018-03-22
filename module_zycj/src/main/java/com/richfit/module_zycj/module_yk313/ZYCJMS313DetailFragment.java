package com.richfit.module_zycj.module_yk313;

import android.view.View;

import com.richfit.common_lib.lib_adapter_rv.base.ViewHolder;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_zycj.R;
import com.richfit.sdk_wzyk.base_msn_detail.BaseMSNDetailFragment;
import com.richfit.sdk_wzyk.base_msn_detail.imp.MSNDetailPresenterImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/11/3.
 */

public class ZYCJMS313DetailFragment extends BaseMSNDetailFragment<MSNDetailPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSNDetailPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        super.initView();
        View recLocation = mView.findViewById(R.id.recLocation);
        if(recLocation != null) {
            recLocation.setVisibility(View.GONE);
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
        return "313物资移库";
    }

    @Override
    protected boolean checkTransStateBeforeRefresh() {
        return true;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int viewType) {
        super.onBindViewHolder(holder,viewType);
        holder.setVisible(R.id.recLocation,false);
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
