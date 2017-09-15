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
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzyk.base_ms_edit.BaseMSEditFragment;
import com.richfit.sdk_wzyk.base_ms_edit.imp.MSEditPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by monday on 2017/6/30.
 */

public class CQYTMSY313EditFragment extends BaseMSEditFragment<MSEditPresenterImp> {

    EditText etQuantityCustom;

    //仓储类型
    Spinner spLocationType;
    List<SimpleEntity> mLocationTypes;

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
        //显示仓储类型
        mView.findViewById(R.id.ll_location_type).setVisibility(View.VISIBLE);
        spLocationType = mView.findViewById(R.id.sp_location_type);
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
        mPresenter.getDictionaryData("locationType");
    }


    @Override
    public void initDataLazily() {
    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        List<SimpleEntity> locationTypes = data.get("locationType");
        if (locationTypes != null) {
            if (mLocationTypes == null) {
                mLocationTypes = new ArrayList<>();
            }
            mLocationTypes.clear();
            mLocationTypes.addAll(locationTypes);
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, mLocationTypes,false);
            spLocationType.setAdapter(adapter);

            //默认选择缓存的数据
            Bundle arguments = getArguments();
            if (arguments != null) {
                String locationType = arguments.getString(Global.EXTRA_LOCATION_TYPE_KEY);
                UiUtil.setSelectionForSimpleSp(mLocationTypes, locationType, spLocationType);
            }
        }
    }

    //拦截住在仓储类型还未初始化就去获取库粗
    @Override
    protected void loadInventoryInfo() {
        if (spLocationType.getAdapter() == null || mLocationTypes == null ||
                mLocationTypes.size() == 0) {
            return;
        }
        if (spLocationType.getSelectedItemPosition() <= 0 && spLocation.getAdapter() != null) {
            //清除之前的库存
            spLocation.setSelection(0);
            return;
        }
        super.loadInventoryInfo();
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

        if(mLocationTypes == null || mLocationTypes.size() <= 0) {
            showMessage("未获取到仓储类型");
            return false;
        }

        if(spLocationType.getSelectedItemPosition() <= 0) {
            showMessage("请先选择仓储类型");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

    //增加仓储类型匹配条件
    @Override
    public void onBindCache(RefDetailEntity cache, String batchFlag, String location) {
        if (cache != null) {
            tvTotalQuantity.setText(cache.totalQuantity);
            String locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
            //查询该行的locationInfo
            List<LocationInfoEntity> locationInfos = cache.locationList;
            if (locationInfos == null || locationInfos.size() == 0) {
                //没有缓存
                tvLocQuantity.setText("0");
                return;
            }
            //如果有缓存，但是可能匹配不上
            tvLocQuantity.setText("0");
            //匹配每一个缓存
            for (LocationInfoEntity info : locationInfos) {
                if (isOpenBatchManager ? location.equalsIgnoreCase(info.locationCombine) &&
                        batchFlag.equalsIgnoreCase(info.batchFlag) && locationType.equalsIgnoreCase(info.locationType) :
                        location.equalsIgnoreCase(info.locationCombine) && locationType.equalsIgnoreCase(info.locationType)) {
                    tvLocQuantity.setText(info.quantity);
                    break;
                }
            }
        }
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantityCustom = getString(etQuantityCustom);
        result.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
        return result;
    }
}