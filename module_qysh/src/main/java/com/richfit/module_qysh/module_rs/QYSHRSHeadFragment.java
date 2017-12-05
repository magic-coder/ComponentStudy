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
    TextView tvCostCenter;
    TextView tvOrderNum;
    TextView tvNetWork;
    EditText etDeliveryTo;

    @Override
    public int getContentId() {
        return R.layout.qysh_fragment_rs_head;
    }

	@Override
    public void clearAllUI() {
		 super.clearAllUI();
        clearCommonUI(tvMoveType, tvCostCenter, tvOrderNum, tvNetWork,etDeliveryTo);
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
        tvCostCenter = mView.findViewById(R.id.qysh_tv_cost_center);
        tvOrderNum = mView.findViewById(R.id.qysh_tv_order_num);
        tvNetWork = mView.findViewById(R.id.qysh_tv_net_work);
        etDeliveryTo = mView.findViewById(R.id.qysh_et_qysh_delivery_to);
    }

    @Override
    public void bindCommonHeaderUI() {
        super.bindCommonHeaderUI();
        tvMoveType.setText(mRefData.sapMoveType);
        tvCostCenter.setText(mRefData.costCenter);
        tvOrderNum.setText(mRefData.orderNum);
        tvNetWork.setText(mRefData.network);
    }

    @Override
    public void _onPause() {
        super._onPause();
        if(mRefData != null) {
            mRefData.deliveryTo = getString(etDeliveryTo);
        }
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }
}
