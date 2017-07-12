package com.richfit.module_cqyt.module_ys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

/**
 * Created by monday on 2017/7/5.
 */

public class CQYTAOEditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    TextView tvInsLotQuantity;

    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        TextView tvRefLineNumName = (TextView) mView.findViewById(R.id.tv_ref_line_num_name);
        tvRefLineNumName.setText("检验批");
        LinearLayout llInsLotQuantity = (LinearLayout) mView.findViewById(R.id.ll_inslot_quantity);
        tvInsLotQuantity = (TextView) mView.findViewById(R.id.tv_insLot_quantity);
        llInsLotQuantity.setVisibility(View.VISIBLE);
        actQuantityName.setText("允许过账数量");
        quantityName.setText("过账数量");

    }

    @Override
    public void initData() {
        super.initData();
        final RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        tvInsLotQuantity.setText(lineData.insLotQuantity);
    }

    @Override
    public void initDataLazily() {

    }
}
