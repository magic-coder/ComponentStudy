package com.richfit.module_qysh.module_ms.ms301;

import android.support.annotation.NonNull;
import android.widget.EditText;

import com.richfit.module_qysh.R;
import com.richfit.sdk_wzyk.base_ms_head.BaseMSHeadFragment;
import com.richfit.sdk_wzyk.base_ms_head.imp.MSHeadPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHMSY301HeadFragment extends BaseMSHeadFragment<MSHeadPresenterImp> {

    EditText etDeliveryTo;

    @Override
    protected int getContentId() {
        return R.layout.qysh_fragment_msy301_head;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MSHeadPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    public void initView() {
        super.initView();
        etDeliveryTo = mView.findViewById(R.id.qysh_et_qysh_delivery_to);
    }

    @Override
    public void bindCommonHeaderUI() {
        super.bindCommonHeaderUI();
        if (mRefData != null) {
            //接收库位
            tvInv.setText(mRefData.recInvCode);
        }
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            mRefData.deliveryTo = getString(etDeliveryTo);
        }
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "3";
    }
}
