package com.richfit.module_cq.module_rs;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.richfit.module_cq.R;
import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * Created by monday on 2017/6/21.
 */

public class CQRSHeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    TextView tvSapMoveType;
    TextView tvSapMoveCause;
    TextView tvCostCenter;
    TextView tvProjectNum;
    TextView tvOrderNum;
    TextView tvReqCompany;
    TextView tvRecordCreator;


    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_rs_head;
    }

    @Override
    public void initView() {
        super.initView();
        tvSapMoveType = mView.findViewById(R.id.cqzt_tv_sap_move_type);
        tvSapMoveCause = mView.findViewById(R.id.cqzt_tv_sap_move_cause);
        tvCostCenter = mView.findViewById(R.id.tv_cost_center);
        tvProjectNum = mView.findViewById(R.id.tv_project_num);
        tvOrderNum = mView.findViewById(R.id.cqzt_tv_order_num);
        tvReqCompany = mView.findViewById(R.id.cqzt_tv_req_company);
        tvRecordCreator = mView.findViewById(R.id.cqzt_tv_record_creator);
    }


    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void bindCommonHeaderUI() {
        super.bindCommonHeaderUI();
        if (mRefData != null) {
            tvSapMoveType.setText(mRefData.sapMoveType);
            tvSapMoveCause.setText(mRefData.sapMoveCause);
            tvCostCenter.setText(mRefData.costCenter);
            tvProjectNum.setText(mRefData.projectNum);
            tvOrderNum.setText(mRefData.orderNum);
            tvReqCompany.setText(mRefData.reqCompany);
            tvRecordCreator.setText(mRefData.recordCreator);
        }
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
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
        return "1";
    }


}
