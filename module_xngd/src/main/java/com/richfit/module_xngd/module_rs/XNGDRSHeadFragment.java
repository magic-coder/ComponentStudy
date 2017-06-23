package com.richfit.module_xngd.module_rs;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * Created by monday on 2017/6/21.
 */

public class XNGDRSHeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    private TextView tvInvFlag;
    private TextView tvCostCenter;
    private TextView tvProjectNum;


    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_rs_head;
    }

    @Override
    public void initView() {
        super.initView();
        tvInvFlag = (TextView) mView.findViewById(R.id.xngd_tv_inv_flag);
        tvCostCenter = (TextView) mView.findViewById(R.id.xngd_tv_cost_center);
        tvProjectNum = (TextView) mView.findViewById(R.id.xngd_tv_project_num);
    }


    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
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
        if (mRefData != null) {
            tvInvFlag.setText(mRefData.invFlag);
            tvProjectNum.setText(mRefData.projectNum);
            tvCostCenter.setText(mRefData.costCenter);
        }
    }
}
