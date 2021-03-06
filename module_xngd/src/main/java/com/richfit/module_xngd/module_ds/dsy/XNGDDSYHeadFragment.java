package com.richfit.module_xngd.module_ds.dsy;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.richfit.module_xngd.R;
import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

/**
 * 是否应急，默认为非应急(0)，应急(1)。这将影响数据采集中获取库存。
 * 注意西南管道抬头多出的几个关键字段1.应急物资，2.项目移交物资，3.物资类型
 * 这些都是与库存维度有关.
 * <p>
 * <p>
 * 应急物资:invFlag;
 * 项目移交物资:invType;
 * 库存类型:specialInvFlag;
 * 项目编号:
 * <p>
 * Created by monday on 2017/1/20.
 */

public class XNGDDSYHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {

    //是否应急
    private CheckBox cbInvFlag;
    //项目移交物资
    private TextView tvInvType;
    //成本中心
    private TextView tvCostCenter;
    //项目编号
    private TextView tvProjectNum;
    //工单号
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
        llCreator.setVisibility(View.GONE);
        cbInvFlag = (CheckBox) mView.findViewById(R.id.xngd_cb_inv_flag);
        tvCostCenter = (TextView) mView.findViewById(R.id.xngd_tv_cost_center);
        tvProjectNum = (TextView) mView.findViewById(R.id.xngd_tv_project_num);
        tvJobNum = (TextView) mView.findViewById(R.id.xngd_tv_job_num);
        tvInvType = (TextView) mView.findViewById(R.id.tv_inv_type);
    }


    @Override
    public void initDataLazily() {

    }

    protected void getRefData(String refNum) {
        super.getRefData(refNum);
        cbInvFlag.setEnabled(true);
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
            //项目编号
            tvProjectNum.setText(mRefData.projectNum);
            //成本中心
            tvCostCenter.setText(mRefData.costCenter);
            //项目移交物资
            if ("0".equalsIgnoreCase(mRefData.invType)) {
                tvInvType.setText(mRefData.invType + "_" + "工程移交");
            } else if ("1".equalsIgnoreCase(mRefData.invType)) {
                tvInvType.setText(mRefData.invType + "_" + "正常物资");
            }
            tvJobNum.setText(mRefData.jobNum);
            //如果单据中有应急标识，那么不允许修改
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
        clearCommonUI(tvInvType, tvCostCenter, tvProjectNum);
    }
}
