package com.richfit.module_hc.module_ms;

import android.view.View;

import com.richfit.sdk_wzyk.base_ms_collect.BaseMSCollectFragment;
import com.richfit.sdk_wzyk.base_ms_collect.imp.MSCollectPresenterImp;

/**
 * 如果接收仓位没有值，那么接收仓储类型不会保存
 * Created by monday on 2017/10/18.
 */

public class HCMSCollectFragment  extends BaseMSCollectFragment<MSCollectPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new MSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        super.initView();
        //打开接收批次和接收仓位
        llRecLocation.setVisibility(View.VISIBLE);
        llRecBatch.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
