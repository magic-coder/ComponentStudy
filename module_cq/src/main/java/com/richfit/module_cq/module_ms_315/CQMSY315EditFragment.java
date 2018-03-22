package com.richfit.module_cq.module_ms_315;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;

import com.richfit.module_cq.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

/**
 * Created by monday on 2017/6/30.
 */

public class CQMSY315EditFragment extends BaseASEditFragment<ASEditPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        TextView tvInvName = (TextView) mView.findViewById(R.id.tv_inv_name);
        TextView tvBatchFlagName = (TextView) mView.findViewById(R.id.tv_batch_flag_name);
        TextView tvLocationName = (TextView) mView.findViewById(R.id.tv_location_name);
        tvInvName.setText("接收库位");
        tvBatchFlagName.setText("接收批次");
        tvLocationName.setText("接收仓位");
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.queryType = "01";
        return queryParam;
    }

}
