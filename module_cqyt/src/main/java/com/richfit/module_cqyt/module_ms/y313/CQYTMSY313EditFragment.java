package com.richfit.module_cqyt.module_ms.y313;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzyk.base_ms_edit.BaseMSEditFragment;
import com.richfit.sdk_wzyk.base_ms_edit.imp.MSEditPresenterImp;

/**
 * Created by monday on 2017/6/30.
 */

public class CQYTMSY313EditFragment extends BaseMSEditFragment<MSEditPresenterImp> {

    public static final String EXTRA_QUANTITY_CUSTOM_KEY = "extra_quantity_custom";

    EditText etQuantityCustom;

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_msy313_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MSEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        llRecBatch.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String quantityCustom = bundle.getString(EXTRA_QUANTITY_CUSTOM_KEY);
            etQuantityCustom.setText(quantityCustom);
        }
    }

    @Override
    public void initDataLazily() {
    }


    @Override
    public boolean checkCollectedDataBeforeSave() {
        final String quantityCustom = getString(etQuantityCustom);
        if(TextUtils.isEmpty(quantityCustom)) {
            showMessage("请先输入件数");
            return false;
        }
        if (Float.valueOf(quantityCustom) < 0.0f) {
            showMessage("件数不合理");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantityCustom = getString(etQuantityCustom);
        return result;
    }


}
