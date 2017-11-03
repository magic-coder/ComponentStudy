package com.richfit.module_cqyt.module_ms.y313;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzyk.base_ms_edit.BaseMSEditFragment;
import com.richfit.sdk_wzyk.base_ms_edit.imp.MSEditPresenterImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by monday on 2017/6/30.
 */

public class CQYTMSY313EditFragment extends BaseMSEditFragment<MSEditPresenterImp> {

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
        super.initView();
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        llRecBatch.setVisibility(View.VISIBLE);
    }


    @Override
    public void initEvent() {
        super.initEvent();
        //选择仓储类型获取库存
        RxAdapterView.itemSelections(spLocationType)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                //注意工厂和库存地点必须使用行里面的
                .subscribe(position -> loadInventoryInfo());
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
    public void initDataLazily() {
    }


    @Override
    public boolean checkCollectedDataBeforeSave() {
        final String quantityCustom = getString(etQuantityCustom);
        if (TextUtils.isEmpty(quantityCustom)) {
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