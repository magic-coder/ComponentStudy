package com.richfit.module_cqyt.module_ms.ubsto;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.BottomDialogMenuAdapter;
import com.richfit.common_lib.lib_adapter.LocationAdapter;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.ArithUtil;
import com.richfit.common_lib.utils.L;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;
import com.richfit.sdk_wzys.camera.TakephotoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by monday on 2017/7/5.
 */

public class CQYTUbstoCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {

    EditText etQuantityCustom;
    TextView tvTotalQuantityCustom;

    //仓储类型
    Spinner spLocationType;
    List<SimpleEntity> mLocationTypes;

    //当扫描下架仓位+仓储类型时必须先通过仓储类型去加载库存，将下架仓位保存
    String mAutoLocation;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type, list);
        mAutoLocation = null;
        if (list != null && list.length == 2 && !cbSingle.isChecked()) {
            mAutoLocation = list[Global.LOCATION_POS];
            String locationType = list[Global.LOCATION_TYPE_POS];
            if (mLocationTypes != null && mLocationTypes.size() > 0 && spLocationType.getAdapter() != null) {
                String oldLocationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
                if (locationType.equals(oldLocationType)) {
                    if (mInventoryDatas == null || mInventoryDatas.size() == 0) {
                        showMessage("请先获取库存");
                        return;
                    }
                    //如果当前仓储类型一致,那么直接获取单条缓存
                    UiUtil.setSelectionForLocation(mInventoryDatas, mAutoLocation, spLocation);
                    return;
                }
            }
            //如果仓储类型不一致
            UiUtil.setSelectionForSimpleSp(mLocationTypes, locationType, spLocationType);
            return;
        }

    }

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_ubsto_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        tvTotalQuantityCustom = (TextView) mView.findViewById(R.id.cqyt_tv_total_quantity_custom);
        TextView tvBatchFlagName = (TextView) mView.findViewById(R.id.tv_batch_flag_name);
        tvBatchFlagName.setText("发出批次");

        //显示仓储类型
        mView.findViewById(R.id.ll_location_type).setVisibility(View.VISIBLE);
        spLocationType = mView.findViewById(R.id.sp_location_type);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        //选择库存地点触发仓储类型的初始化
        RxAdapterView.itemSelections(spInv)
                .filter(a -> spInv.getSelectedItemPosition() > 0)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                //注意工厂和库存地点必须使用行里面的
                .subscribe(position -> mPresenter.getDictionaryData("locationType"));

        //选择仓储类型加载库存(这里不增加过来>0条件的目标是当用户从>0切回<=0时需要清除一些字段)
        RxAdapterView.itemSelections(spLocationType)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                //注意工厂和库存地点必须使用行里面的
                .subscribe(position -> loadInventory(position));
    }

    @Override
    public void initData() {
        isSplitBatchFlag = true;
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
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, mLocationTypes, false);
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
                        dialog.dismiss();
                    })
                    .setNegativeButton("放弃采集", (dialog, which) -> {
                        dialog.dismiss();
                    }).show();
            return;
        }
        super.bindCommonCollectUI();
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
        if (spInv.getAdapter() == null || spInv.getSelectedItemPosition() <= 0) {
            return;
        }
        super.loadInventory(spInv.getSelectedItemPosition());
    }

    //这里的逻辑上，扫描仓储类型先匹配仓储类型->触发下载最新库存->匹配下架仓位->触发单条缓存匹配
    @Override
    public void loadInventoryComplete() {
        if (TextUtils.isEmpty(mAutoLocation)) {
            return;
        }
        //自动匹配下架仓位，并获取缓存
        UiUtil.setSelectionForLocation(mInventoryDatas, mAutoLocation, spLocation);
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

                //这里匹配的逻辑是，如果打开了匹配管理，那么如果输入了批次通过批次和仓位匹配，而且如果批次没有输入，那么通过仓位匹配。
                //如果没有打开批次管理，那么直接通过仓位匹配
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

                //注意它没有匹配成功，可能是批次没有匹配也可能是仓位没有匹配。
                if (isMatch) {
                    tvLocQuantity.setText(cachedItem.quantity);
                    break;
                }
            }
            //2017年07月19日增加批次拆分标识。如果不进行批次拆分那么批次必须保持一致。
            if (!isSplitBatchFlag && !isBatchValidate) {
                showMessage("批次输入有误，请检查批次是否与缓存批次输入一致");
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
                spInv.setEnabled(false);
                spInv.setSelection(pos);
            }
        }
    }

    @Override
    public void showOperationMenuOnCollection(final String companyCode) {
        View rootView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_bottom_menu, null);
        GridView menu = (GridView) rootView.findViewById(R.id.gd_menus);
        final List<BottomMenuEntity> menus = provideDefaultBottomMenu();
        BottomDialogMenuAdapter adapter = new BottomDialogMenuAdapter(mActivity, R.layout.item_dialog_bottom_menu, menus);
        menu.setAdapter(adapter);

        final Dialog dialog = new Dialog(mActivity, R.style.MaterialDialogSheet);
        dialog.setContentView(rootView);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        menu.setOnItemClickListener((adapterView, view, position, id) -> {
            switch (position) {
                case 0:
                    //保存本次数据
                    saveCollectedData();
                    break;
                case 1:
                    //拍照
                    toTakePhoto(menus.get(position).menuName, menus.get(position).takePhotoType);
                    break;
            }
            dialog.dismiss();
        });
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

        if (mRefData != null && TextUtils.isEmpty(mRefData.shopCondition)) {
            showMessage("请先在抬头界面选择装运条件");
            return false;
        }

        if (mLocationTypes == null || mLocationTypes.size() <= 0) {
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
        result.shopCondition = mRefData.shopCondition;
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
    public void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(tvTotalQuantityCustom);
    }

    private void toTakePhoto(String menuName, int takePhotoType) {
        if (!checkBeforeTakePhoto()) {
            return;
        }
        Intent intent = new Intent(mActivity, TakephotoActivity.class);
        Bundle bundle = new Bundle();
        //入库的子菜单的名称
        bundle.putString(Global.EXTRA_TITLE_KEY, "外向交货单出库-" + menuName);
        //拍照类型
        bundle.putInt(Global.EXTRA_TAKE_PHOTO_TYPE, takePhotoType);
        //单据号
        bundle.putString(Global.EXTRA_REF_NUM_KEY, mRefData.recordNum);

        bundle.putString(Global.EXTRA_BIZ_TYPE_KEY, mBizType);
        bundle.putString(Global.EXTRA_REF_TYPE_KEY, mRefType);
        //单据行号
        final int selectedLineNum = getIndexByLineNum(mSelectedRefLineNum);
        final RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        //行号
        bundle.putString(Global.EXTRA_REF_LINE_NUM_KEY, lineData.lineNum);
        //行id
        bundle.putString(Global.EXTRA_REF_LINE_ID_KEY, lineData.refLineId);
        bundle.putInt(Global.EXTRA_POSITION_KEY, selectedLineNum);
        bundle.putBoolean(Global.EXTRA_IS_LOCAL_KEY, mPresenter.isLocal());
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    /**
     * 拍照之前做必要的检查
     *
     * @return
     */
    private boolean checkBeforeTakePhoto() {
        if (!etMaterialNum.isEnabled()) {
            showMessage("请先获取单据数据");
            return false;
        }
        if (TextUtils.isEmpty(mSelectedRefLineNum)) {
            showMessage("请先选择单据行");
            return false;
        }
        return true;
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = new ArrayList<>();

        BottomMenuEntity menu = new BottomMenuEntity();
        menu.menuName = "保存数据";
        menu.menuImageRes = R.mipmap.icon_save_data;
        menus.add(menu);

        menu = new BottomMenuEntity();
        menu.menuName = "外观拍照";
        menu.menuImageRes = R.mipmap.icon_take_photo4;
        menu.takePhotoType = 4;
        menus.add(menu);
        return menus;
    }


    @Override
    protected int getOrgFlag() {
        return 0;
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
