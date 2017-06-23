package com.richfit.module_xngd.module_ds.ds_ll;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.richfit.module_xngd.R;
import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

/**
 * Created by monday on 2017/1/20.
 */

public class XNGDDSLLHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {


    private TextView tvInvFlag;
    private TextView tvCostCenter;
    private TextView tvProjectNum;
    private TextView tvJobNum;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_dsy_head;
    }


    @Override
    public void initPresenter() {
        mPresenter = new DSHeadPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        super.initView();
        tvInvFlag = (TextView) mView.findViewById(R.id.xngd_tv_inv_flag);
        tvCostCenter = (TextView) mView.findViewById(R.id.xngd_tv_cost_center);
        tvProjectNum = (TextView) mView.findViewById(R.id.xngd_tv_project_num);
        tvJobNum = (TextView) mView.findViewById(R.id.xngd_tv_job_num);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initDataLazily() {

    }


    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }

    @Override
    public void bindCommonHeaderUI() {
        super.bindCommonHeaderUI();
        if(mRefData != null) {
            tvInvFlag.setText(mRefData.invFlag);
            tvProjectNum.setText(mRefData.projectNum);
            tvCostCenter.setText(mRefData.costCenter);
            tvJobNum.setText(mRefData.jobNum);
        }
    }


}
