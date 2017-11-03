package com.richfit.module_hc.module_ds;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.richfit.module_hc.R;
import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

/**
 * Created by monday on 2017/10/18.
 */

public class HCDSHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {

    TextView tvCostCenter;
    TextView tvProjectNum;

    @Override
    public int getContentId() {
        return R.layout.hc_fragment_ds_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSHeadPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        llCreator.setVisibility(View.GONE);
        tvCostCenter = mView.findViewById(R.id.hc_tv_cost_center);
        tvProjectNum = mView.findViewById(R.id.hc_tv_project_num);
    }

    @Override
    public void initDataLazily() {

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
        return "2";
    }
}
