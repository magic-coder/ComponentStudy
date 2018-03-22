package com.richfit.module_cq.module_ms_313;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_wzyk.base_ms_edit.BaseMSEditFragment;
import com.richfit.sdk_wzyk.base_ms_edit.imp.MSEditPresenterImp;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by monday on 2017/6/30.
 */

public class CQMSY313EditFragment extends BaseMSEditFragment<MSEditPresenterImp> {



    @Override
    public void initPresenter() {
        mPresenter = new MSEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        llRecBatch.setVisibility(View.VISIBLE);
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