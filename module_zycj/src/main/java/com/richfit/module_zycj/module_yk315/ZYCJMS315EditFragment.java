package com.richfit.module_zycj.module_yk315;

import android.view.View;
import android.widget.TextView;

import com.richfit.module_zycj.R;
import com.richfit.sdk_wzyk.base_msn_edit.BaseMSNEditFragment;
import com.richfit.sdk_wzyk.base_msn_edit.imp.MSNEditPresenterImp;

/**
 * Created by monday on 2017/11/3.
 */

public class ZYCJMS315EditFragment extends BaseMSNEditFragment<MSNEditPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSNEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        //隐藏接收仓位和接收批次
        llRecLocation.setVisibility(View.INVISIBLE);
        llRecBatchFlag.setVisibility(View.INVISIBLE);
        //修改发出批次和发出仓位
        TextView sendBatchFlagName = mView.findViewById(R.id.tv_send_batch_flag_name);
        if (sendBatchFlagName != null) {
            sendBatchFlagName.setText("接收批次");
        }
        TextView sendLocationName = mView.findViewById(R.id.tv_send_location_name);
        if (sendLocationName != null) {
            sendLocationName.setText("接收仓位");
        }
    }

    @Override
    protected void initDataLazily() {

    }

}
