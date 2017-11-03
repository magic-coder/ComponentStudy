package com.richfit.module_qysh.module_rs;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.module_qysh.R;
import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHRSHeadFragment extends BaseASHeadFragment<ASHeadPresenterImp>{

    TextView tvMoveType;
    TextView tvBizScope;
    TextView tvCostCenter;
    TextView tvProfitCenter;
    TextView tvOrderNum;
    TextView tvNetWork;
    EditText etDeliveryTo;

    @Override
    public int getContentId() {
        return R.layout.qysh_fragment_rs_head;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    protected void initView() {
        tvMoveType = mView.findViewById(R.id.qysh_tv_move_type);
        tvBizScope = mView.findViewById(R.id.qysh_tv_biz_scope);
        tvCostCenter = mView.findViewById(R.id.qysh_tv_cost_center);
        tvProfitCenter = mView.findViewById(R.id.qysh_tv_profit_center);
        tvOrderNum = mView.findViewById(R.id.qysh_tv_order_num);
        tvNetWork = mView.findViewById(R.id.qysh_tv_net_work);
        tvNetWork = mView.findViewById(R.id.qysh_et_qysh_deliveryTor);
    }

    @Override
    public void bindCommonHeaderUI() {
        super.bindCommonHeaderUI();
        tvMoveType.setText(mRefData.moveType);
        tvBizScope.setText(mRefData.businessScope);
        tvCostCenter.setText(mRefData.costCenter);
        tvProfitCenter.setText(mRefData.profitCenter);
        tvOrderNum.setText(mRefData.orderNum);
        tvNetWork.setText(mRefData.network);
    }

    @Override
    public void _onPause() {
        mRefData.sapMoveType = mRefData.moveType;
        super._onPause();
        if(mRefData == null) {
            mRefData.deliveryTo = getString(etDeliveryTo);
        }
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }
}
