package com.richfit.module_hc.module_rs;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.richfit.module_hc.R;
import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * Created by monday on 2017/10/18.
 */

public class HCRSHeadFragmnent extends BaseASHeadFragment<ASHeadPresenterImp> {

    TextView tvCostCenter;
    TextView tvProjectNum;

    @Override
    public int getContentId() {
        return R.layout.hc_fragment_rs_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void initView() {
        super.initView();
        tvCostCenter = mView.findViewById(R.id.hc_tv_cost_center);
        tvProjectNum = mView.findViewById(R.id.hc_tv_project_num);
    }

    @Override
    public void bindCommonHeaderUI() {
        super.bindCommonHeaderUI();
        if (mRefData != null) {
            tvCostCenter.setText(mRefData.costCenter + "_" + mRefData.costCenterDesc);
            tvProjectNum.setText(mRefData.projectNum + "_" + mRefData.projectNumDesc);
        }
    }

    @Override
    public void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(tvCostCenter,tvProjectNum);
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }
}
