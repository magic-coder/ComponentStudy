package com.richfit.module_cqyt.module_ms.ubsto;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.data.constant.Global;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

/**
 * Created by monday on 2017/7/5.
 */

public class CQYTUbstoEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {

    EditText etQuantityCustom;

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_ubsto_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        TextView tvBatchFlagName = (TextView) mView.findViewById(R.id.tv_batch_flag_name);
        tvBatchFlagName.setText("发出批次");
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String quantityCustom = bundle.getString(Global.EXTRA_QUANTITY_CUSTOM_KEY);
            etQuantityCustom.setText(quantityCustom);
        }
    }


    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (mRefData != null && TextUtils.isEmpty(mRefData.shopCondition)) {
            showMessage("请先在抬头界面选择装运条件");
            return false;
        }

        if (mLocationTypes == null || mLocationTypes.size() <= 0) {
            showMessage("未获取到仓储类型");
            return false;
        }

        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantityCustom = getString(etQuantityCustom);
        result.shopCondition = mRefData.shopCondition;
        return result;
    }

}
