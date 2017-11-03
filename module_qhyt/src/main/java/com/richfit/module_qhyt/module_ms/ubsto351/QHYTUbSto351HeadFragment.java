package com.richfit.module_qhyt.module_ms.ubsto351;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzyk.base_ms_head.BaseMSHeadFragment;
import com.richfit.sdk_wzyk.base_ms_head.imp.MSHeadPresenterImp;

/**
 * 青海UB-Sto351移库抬头界面(采购订单)。
 * 发出工厂没有发出库位
 * Created by monday on 2017/2/10.
 */

public class QHYTUbSto351HeadFragment extends BaseMSHeadFragment {


    @Override
    public void initPresenter() {
        mPresenter = new MSHeadPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        llInv.setVisibility(View.GONE);
        super.initView();
    }

    @Override
    public void initDataLazily() {

    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "4";
    }

}
