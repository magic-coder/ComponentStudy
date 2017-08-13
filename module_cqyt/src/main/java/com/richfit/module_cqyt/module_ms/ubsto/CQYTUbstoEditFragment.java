package com.richfit.module_cqyt.module_ms.ubsto;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.module_ms.y313.CQYTMSY313EditFragment;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

/**
 * Created by monday on 2017/7/5.
 */

public class CQYTUbstoEditFragment extends BaseDSEditFragment<DSEditPresenterImp>{

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
            String quantityCustom = bundle.getString(CQYTMSY313EditFragment.EXTRA_QUANTITY_CUSTOM_KEY);
            etQuantityCustom.setText(quantityCustom);
        }
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantityCustom = getString(etQuantityCustom);
        return result;
    }
}
