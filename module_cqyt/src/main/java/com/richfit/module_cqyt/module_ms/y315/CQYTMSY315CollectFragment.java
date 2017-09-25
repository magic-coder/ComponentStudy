package com.richfit.module_cqyt.module_ms.y315;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.ArithUtil;
import com.richfit.common_lib.utils.L;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 注意这里使用的313的布局文件，因为长庆的移库都是在标准的基础上增加了一个件数的字段
 * Created by monday on 2017/6/30.
 */

public class CQYTMSY315CollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    EditText etQuantityCustom;
    TextView tvTotalQuantityCustom;

    //仓储类型
    Spinner spLocationType;
    List<SimpleEntity> mLocationTypes;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type, list);
        if (list != null && list.length == 2 && !cbSingle.isChecked()) {
            String location = list[Global.LOCATION_POS];
            String locationType = list[Global.LOCATION_TYPE_POS];
            clearCommonUI(etLocation);
            etLocation.setText(location);
            //自动选择仓储类型
            UiUtil.setSelectionForSimpleSp(mLocationTypes, locationType, spLocationType);
            getTransferSingle(getString(etBatchFlag), location);
            return;
        }
    }

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_msy315_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        TextView tvInvName = (TextView) mView.findViewById(R.id.tv_inv_name);
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        tvTotalQuantityCustom = (TextView) mView.findViewById(R.id.cqyt_tv_total_quantity_custom);
        tvWorkName.setText("工厂");
        tvActQuantityName.setText("应收数量");
        tvInvName.setText("接收库位");
        tvLocationName.setText("接收仓位");
        tvBatchFlagName.setText("接收批次");
        //显示仓储类型
        mView.findViewById(R.id.ll_location_type).setVisibility(View.VISIBLE);
        spLocationType = mView.findViewById(R.id.sp_location_type);
    }


    @Override
    public void initEvent() {
        super.initEvent();
        /*监听上架仓位点击事件(注意如果是必检物资该监听无效)*/
        etLocation.setOnRichAutoEditTouchListener((view, location) -> getTransferSingle(getString(etBatchFlag), location));

        //增加库存地点选择出发仓储类型的获取
        RxAdapterView.itemSelections(spInv)
                .filter(pos -> pos > 0)
                .subscribe(pos -> {
                    if (isNLocation) {
                        //如果不上架
                        getTransferSingle(getString(etBatchFlag), getString(etLocation));
                    } else {
                        mPresenter.getDictionaryData("locationType");
                    }
                });

        //增加仓储类型的选择获取提示库粗
        RxAdapterView.itemSelections(spLocationType)
                .filter(a -> spLocationType.getAdapter() != null && mLocationTypes != null
                        && mLocationTypes.size() > 0)
                .subscribe(position -> loadLocationList(false));
    }

    @Override
    public void initData() {

    }

    //增加危化品物料的拦截
    @Override
    public void bindCommonCollectUI() {
        mSelectedRefLineNum = mRefLines.get(spRefLine.getSelectedItemPosition());
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        if (!TextUtils.isEmpty(lineData.dangerFlag)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("温馨提示").setMessage(lineData.dangerFlag)
                    .setPositiveButton("继续采集", (dialog, which) -> {
                        super.bindCommonCollectUI();
                        dialog.dismiss();
                    })
                    .setNegativeButton("放弃采集", (dialog, which) -> {
                        dialog.dismiss();
                    }).show();
            return;
        }
        super.bindCommonCollectUI();
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
        }
    }

    //重写该方法，在获取提示库存之前清除历史库存
    @Override
    public void loadLocationList(boolean isDropDown) {
        if (mLocationList != null && mLocationAdapter != null) {
            mLocationList.clear();
            mLocationAdapter.notifyDataSetChanged();
        }
        super.loadLocationList(isDropDown);
    }

    //重写该方法的目的是获取累计件数缓存以及件数缓存。另外就是增加存储类型匹配条件
    @Override
    public void onBindCache(RefDetailEntity cache, String batchFlag, String location) {

        if (!isNLocation) {
            if (cache != null) {
                tvTotalQuantity.setText(cache.totalQuantity);
                tvTotalQuantityCustom.setText(cache.totalQuantityCustom);
                //锁定库存地点
                lockInv(cache.invId);
                //匹配缓存
                List<LocationInfoEntity> locationInfos = cache.locationList;
                if (locationInfos == null || locationInfos.size() == 0) {
                    //没有缓存
                    tvLocQuantity.setText("0");
                    return;
                }
                tvLocQuantity.setText("0");
                /**
                 * 这里匹配缓存是通过批次+仓位匹配的，但是批次即便是在打开了批次管理的情况下
                 * 也可能没有批次。
                 */
                for (LocationInfoEntity cachedItem : locationInfos) {
                    //缓存和输入的都为空或者都不为空而且相等,那么系统默认批次匹配
                    boolean isMatch = false;

                    isBatchValidate = !isOpenBatchManager ? true : ((TextUtils.isEmpty(cachedItem.batchFlag) && TextUtils.isEmpty(batchFlag)) ||
                            (!TextUtils.isEmpty(cachedItem.batchFlag) && !TextUtils.isEmpty(batchFlag) && batchFlag.equalsIgnoreCase(cachedItem.batchFlag)));

                    String locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;

                    if (!isOpenBatchManager) {
                        //没有打开批次管理，直接使用仓位匹配
                        isMatch = location.equalsIgnoreCase(cachedItem.location) && locationType.equalsIgnoreCase(cachedItem.locationType);
                    } else {
                        if (TextUtils.isEmpty(cachedItem.batchFlag) && TextUtils.isEmpty(batchFlag)) {
                            //打开批次管理，但是没有输入批次
                            isMatch = location.equalsIgnoreCase(cachedItem.location) && locationType.equalsIgnoreCase(cachedItem.locationType);
                        } else if (!TextUtils.isEmpty(cachedItem.batchFlag) && !TextUtils.isEmpty(batchFlag)) {
                            //打开了批次管理，输入了批次
                            isMatch = location.equalsIgnoreCase(cachedItem.location) && batchFlag.equalsIgnoreCase(cachedItem.batchFlag)
                                    && locationType.equalsIgnoreCase(cachedItem.locationType);
                        }
                    }
                    L.e("isBatchValidate = " + isBatchValidate + "; isMatch = " + isMatch);

                    //注意它没有匹配次成功可能是批次页可能是仓位。
                    if (isMatch) {
                        tvLocQuantity.setText(cachedItem.quantity);
                        break;
                    }
                }

                if (!isBatchValidate) {
                    showMessage("批次输入有误，请检查批次是否与缓存批次输入一致");
                }
            }
        } else {
            //对于不上架的物资，显示累计数量和锁定库存地点
            if (cache != null) {
                tvTotalQuantity.setText(cache.totalQuantity);
                lockInv(cache.invId);
            }
        }

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

        if(mLocationTypes == null || mLocationTypes.size() <= 0) {
            showMessage("未获取到仓储类型");
            return false;
        }

        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        //件数
        result.quantityCustom = getString(etQuantityCustom);
        //仓储类型
        result.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
        return result;
    }

    @Override
    public void saveCollectedDataSuccess(String message) {
        super.saveCollectedDataSuccess(message);
        //累计件数
        tvTotalQuantityCustom.setText(String.valueOf(ArithUtil.add(getString(etQuantityCustom),
                getString(tvTotalQuantityCustom))));
        if (!cbSingle.isChecked()) {
            etQuantityCustom.setText("");
        }
    }

    @Override
    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etQuantityCustom, tvTotalQuantityCustom);
    }

    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        if (mLocationTypes != null) {
            queryParam.extraMap = new HashMap<>();
            String locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
            queryParam.extraMap.put("locationType", locationType);
        }
        return queryParam;
    }

}
