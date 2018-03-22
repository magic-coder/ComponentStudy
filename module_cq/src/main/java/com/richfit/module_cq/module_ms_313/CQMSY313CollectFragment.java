package com.richfit.module_cq.module_ms_313;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_wzyk.base_ms_collect.BaseMSCollectFragment;
import com.richfit.sdk_wzyk.base_ms_collect.imp.MSCollectPresenterImp;

/**
 * Created by monday on 2017/6/30.
 */

public class CQMSY313CollectFragment extends BaseMSCollectFragment<MSCollectPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new MSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        //打开接收仓位和接收批次
       // llRecLocation.setVisibility(View.VISIBLE);
        llRecBatch.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.queryType = "01";
        return queryParam;
    }


    @Override
    protected int getOrgFlag() {
        return 0;
    }

}
