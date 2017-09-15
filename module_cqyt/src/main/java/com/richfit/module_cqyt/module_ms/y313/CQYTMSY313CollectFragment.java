package com.richfit.module_cqyt.module_ms.y313;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzyk.base_ms_collect.BaseMSCollectFragment;
import com.richfit.sdk_wzyk.base_ms_collect.imp.MSCollectPresenterImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by monday on 2017/6/30.
 */

public class CQYTMSY313CollectFragment extends BaseMSCollectFragment<MSCollectPresenterImp> {

    EditText etQuantityCustom;
    TextView tvTotalQuantityCustom;
    //仓储类型
    Spinner spLocationType;
    List<SimpleEntity> mLocationTypes;
    String mAutoLocation;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        mAutoLocation = null;
        if (list != null && list.length == 2 && !cbSingle.isChecked()) {
            mAutoLocation = list[Global.LOCATION_POS];
            String locationType = list[Global.LOCATION_TYPE_POS];
            //自动选择仓储类型
            UiUtil.setSelectionForSimpleSp(mLocationTypes, locationType, spLocationType);
            return;
        }
        super.handleBarCodeScanResult(type, list);
    }

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_msy313_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        tvTotalQuantityCustom = (TextView) mView.findViewById(R.id.cqyt_tv_total_quantity_custom);
        //打开接收批次，默认与发出批次一致，不允许修改
        llRecBatch.setVisibility(View.VISIBLE);
        etRecBatchFlag.setEnabled(false);
        //发出工厂改为工厂
        sendWorkName.setText("工厂");

        //显示仓储类型
        mView.findViewById(R.id.ll_location_type).setVisibility(View.VISIBLE);
        spLocationType = mView.findViewById(R.id.sp_location_type);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        //选择库存地点触发仓储类型的初始化
        RxAdapterView.itemSelections(spSendInv)
                .filter(a -> spSendInv.getSelectedItemPosition() > 0)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                //注意工厂和库存地点必须使用行里面的
                .subscribe(position -> mPresenter.getDictionaryData("locationType"));

        RxAdapterView.itemSelections(spLocationType)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                //注意工厂和库存地点必须使用行里面的
                .subscribe(position -> loadInventory(position));
    }

    @Override
    public void initData(){}

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

    @Override
    public void bindCommonCollectUI() {
        mSelectedRefLineNum = mRefLines.get(spRefLine.getSelectedItemPosition());
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        if (!TextUtils.isEmpty(lineData.dangerFlag)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("温馨提示").setMessage(lineData.dangerFlag)
                    .setPositiveButton("继续采集", (dialog, which) -> {
                        super.bindCommonCollectUI();
                        etRecBatchFlag.setText(getString(etSendBatchFlag));
                        dialog.dismiss();
                    })
                    .setNegativeButton("放弃采集", (dialog, which) -> {
                        dialog.dismiss();
                    }).show();
            return;
        }
        super.bindCommonCollectUI();
        //接收批次与接收批次一致
        etRecBatchFlag.setText(getString(etSendBatchFlag));
    }

    //重写加载库存，目的是如果选择的仓储类型是0那么清除之前的库存信息
    //注意这里的position已经是仓储类型的位置了
    @Override
    protected void loadInventory(int position) {
        tvInvQuantity.setText("");
        tvLocQuantity.setText("");
        tvTotalQuantity.setText("");
        //如果没有选择仓储类型那么清空之前的库存信息
        if (mLocationAdapter != null) {
            mInventoryDatas.clear();
            mLocationAdapter.notifyDataSetChanged();
        }
        if (spSendInv.getAdapter() == null || spSendInv.getSelectedItemPosition() <= 0) {
            return;
        }
        super.loadInventory(spSendInv.getSelectedItemPosition());
    }

    @Override
    public void loadInventoryComplete() {
        //如果仓储条码中没有仓位那么不加载单条缓存
        if(TextUtils.isEmpty(mAutoLocation)) {
            return;
        }
        UiUtil.setSelectionForLocation(mInventoryDatas, mAutoLocation, spSendLoc);
    }

    //重写该方法的目的是获取累计件数缓存以及件数缓存。另外就是增加存储类型匹配条件
    @Override
    public void onBindCache(RefDetailEntity cache, String batchFlag, String locationCombine) {
        if (cache != null) {
            tvTotalQuantity.setText(cache.totalQuantity);
            tvTotalQuantityCustom.setText(cache.totalQuantityCustom);
            //查询该行的locationInfo
            List<LocationInfoEntity> locationInfos = cache.locationList;
            if (locationInfos == null || locationInfos.size() == 0) {
                //没有缓存
                tvLocQuantity.setText("0");
                return;
            }

            //当前输入批次是否与缓存的批次一致
            if (isOpenBatchManager && !TextUtils.isEmpty(mCachedBatchFlag)) {
                if (!mCachedBatchFlag.equalsIgnoreCase(batchFlag)) {
                    showMessage("您输入的批次有误，请重新输入");
                    return;
                }
            }

            //如果有缓存，但是可能匹配不上
            tvLocQuantity.setText("0");
            //匹配每一个缓存
            for (LocationInfoEntity cachedItem : locationInfos) {
                if ("barcode".equalsIgnoreCase(cachedItem.location)) {
                    //不显示该仓位的值
                    return;
                }
                //缓存和输入的都为空或者都不为空而且相等
                boolean isMatch = false;

                isBatchValidate = !isOpenBatchManager ? true : ((TextUtils.isEmpty(cachedItem.batchFlag) && TextUtils.isEmpty(batchFlag)) ||
                        (!TextUtils.isEmpty(cachedItem.batchFlag) && !TextUtils.isEmpty(batchFlag) &&
                                batchFlag.equalsIgnoreCase(cachedItem.batchFlag)));

                String locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
                if (!isOpenBatchManager) {
                    isMatch = locationCombine.equalsIgnoreCase(cachedItem.locationCombine)
                            && locationType.equalsIgnoreCase(cachedItem.locationType);
                } else {
                    if (TextUtils.isEmpty(cachedItem.batchFlag) && TextUtils.isEmpty(batchFlag)) {
                        isMatch = locationCombine.equalsIgnoreCase(cachedItem.locationCombine)
                                && locationType.equalsIgnoreCase(cachedItem.locationType);
                    } else if (!TextUtils.isEmpty(cachedItem.batchFlag) && !TextUtils.isEmpty(batchFlag)) {
                        isMatch = locationCombine.equalsIgnoreCase(cachedItem.locationCombine) && batchFlag.equalsIgnoreCase(cachedItem.batchFlag)
                                && locationType.equalsIgnoreCase(cachedItem.locationType);
                    }
                }
                L.e("isBatchValidate = " + isBatchValidate + "; isMatch = " + isMatch);

                //注意它没有匹配次成功可能是批次页可能是仓位。
                if (isMatch) {
                    mCachedBatchFlag = cachedItem.batchFlag;
                    tvLocQuantity.setText(cachedItem.quantity);
                    break;
                }
            }
            //锁定库存地点
            final String cachedInvId = cache.invId;
            if (!TextUtils.isEmpty(cachedInvId)) {
                int pos = -1;
                for (InvEntity data : mInvDatas) {
                    pos++;
                    if (cachedInvId.equals(data.invId))
                        break;
                }
                spSendInv.setEnabled(false);
                spSendInv.setSelection(pos);
            }
        }
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
        result.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
        return result;
    }

    @Override
    public void saveCollectedDataSuccess(String message) {
        super.saveCollectedDataSuccess(message);
        float quantityCustomV = CommonUtil.convertToFloat(getString(etQuantityCustom), 0.0F);
        float totalQuantityCustomV = CommonUtil.convertToFloat(getString(tvTotalQuantityCustom), 0.0F);
        tvTotalQuantityCustom.setText(String.valueOf(quantityCustomV + totalQuantityCustomV));
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
    protected int getOrgFlag() {
        return 0;
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
