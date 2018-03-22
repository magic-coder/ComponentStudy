package com.richfit.module_zycj.module_yk315;

import android.text.TextUtils;
import android.view.View;

import com.richfit.sdk_wzyk.base_msn_collect.BaseMSNCollectFragment;
import com.richfit.sdk_wzyk.base_msn_collect.imp.MSNCollectPresenterImp;

/**
 * Created by monday on 2017/11/3.
 */

public class ZYCJMS315CollectFragment extends BaseMSNCollectFragment<MSNCollectPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        super.initView();
        //隐藏接收仓位和接收批次
        llRecLocation.setVisibility(View.INVISIBLE);
        llRecBatchFlag.setVisibility(View.INVISIBLE);
        //修改发出仓位和发出批次为接收批次
        tvSendLocName.setText("接收仓位");
        tvSendBatchFlagName.setText("接收批次");
    }

    @Override
    protected boolean checkHeaderData() {
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先选择工厂");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.recInvId)) {
            showMessage("请先选择接收库位");
            return false;
        }
        return true;
    }

    @Override
    protected boolean getWMOpenFlag() {
        return false;
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

}
