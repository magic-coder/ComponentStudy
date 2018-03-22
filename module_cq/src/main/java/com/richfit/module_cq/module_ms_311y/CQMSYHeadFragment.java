package com.richfit.module_cq.module_ms_311y;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.richfit.module_cq.R;
import com.richfit.sdk_wzyk.base_ms_head.BaseMSHeadFragment;
import com.richfit.sdk_wzyk.base_ms_head.imp.MSHeadPresenterImp;

/**
 * Created by monday on 2017/12/5.
 */

public class CQMSYHeadFragment extends BaseMSHeadFragment<MSHeadPresenterImp> {

    //接收库位
    TextView tvRecInv;

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
        //发出工厂->工厂
        llSendWork.setVisibility(View.VISIBLE);
        TextView tvSendWorkName = mView.findViewById(R.id.tv_send_work_name);

        tvSendWorkName.setText("工厂");
        //去掉接收工厂
        llRecWork.setVisibility(View.GONE);
        //接收库位
        mView.findViewById(R.id.ll_rec_inv).setVisibility(View.VISIBLE);
        tvRecInv = mView.findViewById(R.id.tv_rec_inv);
    }

    @Override
    public void bindCommonHeaderUI() {
        super.bindCommonHeaderUI();
        if(mRefData != null) {
            tvRecInv.setText(mRefData.recInvCode);
        }
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "3";
    }
}
