package com.richfit.module_qysh.module_ds;

import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.module_qysh.R;
import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHDSYHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {

    TextView tvMoveType;
    TextView tvCostCenter;
    TextView tvOrderNum;
    TextView tvNetWork;
    EditText etDeliveryTo;

    @Override
    protected int getContentId() {
        return R.layout.qysh_fragment_dsy_head;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DSHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        tvMoveType = mView.findViewById(R.id.qysh_tv_move_type);
        tvCostCenter = mView.findViewById(R.id.qysh_tv_cost_center);
        tvOrderNum = mView.findViewById(R.id.qysh_tv_order_num);
        tvNetWork = mView.findViewById(R.id.qysh_tv_net_work);
        etDeliveryTo = mView.findViewById(R.id.qysh_et_qysh_delivery_to);
        //隐藏创建人
        llCreator.setVisibility(View.GONE);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    public void bindCommonHeaderUI() {
        super.bindCommonHeaderUI();
        tvMoveType.setText(mRefData.moveType);
        tvCostCenter.setText(mRefData.costCenter);
        tvOrderNum.setText(mRefData.orderNum);
        tvNetWork.setText(mRefData.network);
        tvMoveType.setText(mRefData.sapMoveType);
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
        return "2";
    }
}
