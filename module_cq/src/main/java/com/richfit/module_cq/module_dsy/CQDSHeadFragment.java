package com.richfit.module_cq.module_dsy;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.richfit.module_cq.R;
import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

/**
 * 其他出库有参考
 * Created by monday on 2017/12/5.
 */

public class CQDSHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {

    TextView tvSapMoveType;
    TextView tvSapMoveCause;
    TextView tvCostCenter;
    TextView tvProjectNum;
    TextView tvOrderNum;
    TextView tvReqCompany;
    TextView tvRecordCreator;


    @Override
    protected void initPresenter() {
        mPresenter = new DSHeadPresenterImp(mActivity);
    }


    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_rs_head;
    }

    @Override
    public void initView() {
        tvSapMoveType = mView.findViewById(R.id.cqzt_tv_sap_move_type);
        tvSapMoveCause = mView.findViewById(R.id.cqzt_tv_sap_move_cause);
        tvCostCenter = mView.findViewById(R.id.tv_cost_center);
        tvProjectNum = mView.findViewById(R.id.tv_project_num);
        tvOrderNum = mView.findViewById(R.id.cqzt_tv_order_num);
        tvReqCompany = mView.findViewById(R.id.cqzt_tv_req_company);
        tvRecordCreator = mView.findViewById(R.id.cqzt_tv_record_creator);
        //隐藏创建人
        llCreator.setVisibility(View.GONE);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    public void bindCommonHeaderUI() {
        super.bindCommonHeaderUI();
        //移动类型
        tvSapMoveType.setText(mRefData.sapMoveType);
        //移动原因
        tvSapMoveCause.setText(mRefData.sapMoveCause);
        //成本中心
        tvCostCenter.setText(mRefData.costCenter);
        //项目编号
        tvProjectNum.setText(mRefData.projectNum);
        //订单编号
        tvOrderNum.setText(mRefData.orderNum);
        //领料单位
        tvReqCompany.setText(mRefData.reqCompany);
        //创建人
        tvRecordCreator.setText(mRefData.recordCreator);
    }

    @Override
    public void _onPause() {
        super._onPause();
        if(mRefData != null) {
            mRefData.sapMoveType = getString(tvSapMoveType);
            mRefData.sapMoveCause = getString(tvSapMoveCause);
            mRefData.costCenter = getString(tvCostCenter);
            mRefData.projectNum = getString(tvProjectNum);
            mRefData.orderNum = getString(tvOrderNum);
            mRefData.reqCompany = getString(tvReqCompany);
            mRefData.recordCreator = getString(tvRecordCreator);
        }
    }


    @NonNull
    @Override
    protected String getMoveType() {
        return "2";
    }
}
