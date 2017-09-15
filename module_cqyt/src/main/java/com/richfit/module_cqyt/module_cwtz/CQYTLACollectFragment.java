package com.richfit.module_cqyt.module_cwtz;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_cwtz.collect.LACollectFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/7/20.
 */

public class CQYTLACollectFragment extends LACollectFragment {

    EditText etQuantityCustom;
    Spinner spLocationType;
    Spinner spRecLocationType;
    List<SimpleEntity> mLocationTypes;
    List<SimpleEntity> mRecLocationTypes;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        if (list != null && list.length == 2) {
            String location = list[Global.LOCATION_POS];
            String locationType = list[Global.LOCATION_TYPE_POS];
            //目标仓位
            if (etRecLocation.hasFocus() && etRecLocation.isFocused()) {
                clearCommonUI(etRecLocation);
                etRecLocation.setText(location);
            } else {
                //源仓位
                clearCommonUI(etSendLocation);
                etSendLocation.setText(location);
                //自动选择仓储类型
                UiUtil.setSelectionForSimpleSp(mLocationTypes, locationType, spLocationType);
            }
            return;
        }
        super.handleBarCodeScanResult(type, list);
    }

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_la_collect;
    }

    @Override
    public void initView() {
        super.initView();
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        //源仓位仓储类型
        mView.findViewById(R.id.ll_location_type).setVisibility(View.VISIBLE);
        //目标仓位仓储类型
        mView.findViewById(R.id.ll_rec_location_type).setVisibility(View.VISIBLE);
        spLocationType = mView.findViewById(R.id.sp_location_type);
        spRecLocationType = mView.findViewById(R.id.sp_rec_location_type);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        //增加仓储类型的选择获取提示库粗
        RxAdapterView.itemSelections(spLocationType)
                .filter(a -> spLocationType.getAdapter() != null && mLocationTypes != null
                        && mLocationTypes.size() > 0)
                .subscribe(position -> loadInventoryInfo(getString(etSendLocation)));
    }

    @Override
    public void initData() {
        super.initData();
        mPresenter.getDictionaryData("locationType");
    }


    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {

        List<SimpleEntity> locationTypes = data.get("locationType");
        if (locationTypes != null) {
            if (mLocationTypes == null) {
                mLocationTypes = new ArrayList<>();
            }
            if (mRecLocationTypes == null) {
                mRecLocationTypes = new ArrayList<>();
            }
            mLocationTypes.clear();
            mRecLocationTypes.clear();
            mLocationTypes.addAll(locationTypes);
            mRecLocationTypes.addAll(locationTypes);
            //发出仓储类型
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp,
                    mLocationTypes, false);
            spLocationType.setAdapter(adapter);

            //接收仓储类型
            SimpleAdapter recAdapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp,
                    mRecLocationTypes, false);
            spRecLocationType.setAdapter(recAdapter);
        }
    }

    protected void loadInventoryInfo(String location) {
        //如果没有仓位那么直接返回
        if(TextUtils.isEmpty(location))
            return;
        super.loadInventoryInfo(location);
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {

        if (mLocationTypes == null || mLocationTypes.size() <= 0) {
            showMessage("未获取到源仓储类型");
            return false;
        }

        if (mRecLocationTypes == null && mRecLocationTypes.size() <= 0) {
            showMessage("未获取到目标仓储类型");
            return false;
        }

        return super.checkCollectedDataBeforeSave();
    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantityCustom = getString(etQuantityCustom);
        result.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
        result.recLocationType = mRecLocationTypes.get(spRecLocationType.getSelectedItemPosition()).code;
        return result;
    }

    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        if (mLocationTypes != null && spLocationType.getSelectedItemPosition() > 0) {
            queryParam.extraMap = new HashMap<>();
            String locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
            queryParam.extraMap.put("locationType", locationType);
        }
        return queryParam;
    }

}
