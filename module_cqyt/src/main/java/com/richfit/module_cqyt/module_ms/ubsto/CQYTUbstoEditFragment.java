package com.richfit.module_cqyt.module_ms.ubsto;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.module_ms.y313.CQYTMSY313EditFragment;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by monday on 2017/7/5.
 */

public class CQYTUbstoEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {

    EditText etQuantityCustom;
    //仓储类型
    Spinner spLocationType;
    List<SimpleEntity> mLocationTypes;

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
        mPresenter.getDictionaryData("locationType");
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
        super.loadInventoryInfo();
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
        //仓储类型
        result.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
        return result;
    }

    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        if (mLocationTypes != null && spLocationType.getSelectedItemPosition() > 0) {
            queryParam.extraMap = new HashMap<>();
            String locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
            Log.e("yff", "locationType = " + locationType);
            queryParam.extraMap.put("locationType", locationType);
        }
        return queryParam;
    }
}
