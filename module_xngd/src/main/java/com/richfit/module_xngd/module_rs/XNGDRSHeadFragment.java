package com.richfit.module_xngd.module_rs;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * Created by monday on 2017/6/21.
 */

public class XNGDRSHeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    //应急物资
    private CheckBox cbInvFlag;
    //成本中心
    private TextView tvCostCenter;
    //项目编号
    private TextView tvProjectNum;
    //工单号
    private TextView tvJobNum;
    //项目移交物资
    private TextView tvInvType;


    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_rs_head;
    }

    @Override
    public void initView() {
        super.initView();
        cbInvFlag = (CheckBox) mView.findViewById(R.id.xngd_cb_inv_flag);
        tvCostCenter = (TextView) mView.findViewById(R.id.xngd_tv_cost_center);
        tvProjectNum = (TextView) mView.findViewById(R.id.xngd_tv_project_num);
        tvJobNum = (TextView) mView.findViewById(R.id.xngd_tv_job_num);
        tvInvType = (TextView) mView.findViewById(R.id.xngd_tv_inv_flag);
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
            tvProjectNum.setText(mRefData.projectNum);
            tvCostCenter.setText(mRefData.costCenter);
            tvJobNum.setText(mRefData.jobNum);
            //项目移交物资
            tvInvType.setText(mRefData.invType);
            cbInvFlag.setEnabled(TextUtils.isEmpty(mRefData.invFlag));
            cbInvFlag.setChecked("1".equals(mRefData.invFlag));
        }
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            mRefData.invFlag = cbInvFlag.isChecked() ? "1" : "0";
        }
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {
        super.clearAllUIAfterSubmitSuccess();
        clearCommonUI(tvInvType, tvCostCenter,tvProjectNum);
    }
}
