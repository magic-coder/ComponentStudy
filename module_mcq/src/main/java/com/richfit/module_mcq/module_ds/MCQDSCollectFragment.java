package com.richfit.module_mcq.module_ds;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.utils.ArithUtil;
import com.richfit.common_lib.utils.L;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_mcq.R;
import com.richfit.module_mcq.module_ds.imp.MCQDSCollectPresenterImp;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.IDSCollectView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 在数据采集界面中录入下架信息，系统根据先进先出原则建议下架仓位，
 * 自动分配实发数量，建议仓位为入库时间最早的仓位，
 * 实际下架仓位默认跟建议下架仓位一致，允许更改，系统自动分配实发数量，
 * 如果应发数量小于等于建议仓位的库存数量，实发数量等于应发数量，
 * 如果应发数量大于建议仓位的库存数量，实发数量等于建议仓位库存数量，实发数量允许修改，
 * 保存之后再重新分配建议仓位和实发数量，建议仓位和实发数量需要扣减相同物料在相应仓位上已经录入的数量，
 * 建议仓位只考虑主计量单位的实发数量。当副计量单位应发数量不为空时，副计量单位的实发数量必输。
 *
 * 主要逻辑:
 * 选择库存地点—>仓储类型
 * ->获取库存以及获取建议仓位。如果用户输入仓位，那么以用户输入的为准
 * Created by monday on 2017/8/31.
 */

public class MCQDSCollectFragment extends BaseDSCollectFragment<MCQDSCollectPresenterImp>
        implements IDSCollectView {

    //副计量单位
    TextView tvMaterialUnitCustom;

    //副计量单位应发数量
    TextView tvActQuantityCustom;

    //副计量单位库存数量
    TextView tvInvQuantityCustom;

    //副计量单位仓位数量
    TextView tvLocQuantityCustom;

    //副计量单位实发数量
    EditText etQuantityCustom;

    //副计量单位累计数量
    TextView tvTotalQuantityCustom;

    //建议下架仓位
    TextView tvSuggestedLocation;

    //仓储类型
    Spinner spLocationType;
    List<SimpleEntity> mLocationTypes;

    //当扫描下架仓位+仓储类型时必须先通过仓储类型去加载库存，将下架仓位保存
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
        return R.layout.mcq_fragment_ds_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MCQDSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        tvMaterialUnitCustom = mView.findViewById(R.id.mcq_tv_material_unit_custom);
        tvActQuantityCustom = mView.findViewById(R.id.mcq_tv_act_quantity_custom);
        tvLocQuantityCustom = mView.findViewById(R.id.mcq_tv_location_quantity_custom);
        etQuantityCustom = mView.findViewById(R.id.mcq_et_quantity_custom);
        tvInvQuantityCustom = mView.findViewById(R.id.mcq_tv_inv_quantity_custom);
        tvTotalQuantityCustom = mView.findViewById(R.id.mcq_tv_total_quantity_custom);
        tvSuggestedLocation = mView.findViewById(R.id.mcq_tv_suggested_location);
        spLocationType = mView.findViewById(R.id.sp_location_type);
        //实际下架仓位
        TextView tvLocationName = mView.findViewById(R.id.mcq_location_name);
        tvLocationName.setText("实际下架仓位");


        //主计量单位
        TextView tvMaterialUniName = mView.findViewById(R.id.mcq_tv_material_unit_name);
        tvMaterialUniName.setText("主计量单位");

        //主计量单位应收数量
        tvActQuantityName.setText("主计量单位应发数量");

        //主计量单位实收数量
        tvQuantityName.setText("主计量单位实发数量");

        //主计量单位仓位数量
        TextView tvLocationQuantityName = mView.findViewById(R.id.mcq_tv_location_quantity_name);
        tvLocationQuantityName.setText("主计量单位仓位数量");

        //主计量单位库存数量
        TextView tvInvQuantityName = mView.findViewById(R.id.mcq_tv_inv_quantity_name);
        tvInvQuantityName.setText("主计量单位库存数量");

        //主计量单位累计数量
        TextView tvTotalQuantityName = mView.findViewById(R.id.mcq_total_quantity_name);
        tvTotalQuantityName.setText("主计量单位累计数量");

        //隐藏批次
        mView.findViewById(R.id.mcq_ll_batch_flag).setVisibility(View.GONE);

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

    }

    /**
     * 绑定UI。
     */
    @Override
    public void bindCommonCollectUI() {
        mSelectedRefLineNum = mRefLines.get(spRefLine.getSelectedItemPosition());
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        //副计量单位
        tvMaterialUnitCustom.setText(lineData.unitCustom);
        //副计量单位的应收数量
        tvActQuantityCustom.setText(lineData.actQuantityCustom);
        super.bindCommonCollectUI();
    }

    //重写加载库存，目的是如果选择的仓储类型是0那么清除之前的库存信息
    //注意这里的position已经是仓储类型的位置了
    @Override
    protected void loadInventory(int position) {
        if (position <= 0) {
            tvInvQuantity.setText("");
            tvLocQuantity.setText("");
            tvTotalQuantity.setText("");
            //如果没有选择仓储类型那么清空之前的库存信息
            if (mLocationAdapter != null) {
                mInventoryDatas.clear();
                mLocationAdapter.notifyDataSetChanged();
            }
            return;
        }
        if (spInv.getAdapter() == null || spInv.getSelectedItemPosition() <= 0) {
            return;
        }
        super.loadInventory(spInv.getSelectedItemPosition());
    }

    /**
     * 库存获取成功。拦截住去获取建议仓位
     */
    @Override
    public void loadInventoryComplete() {
        if (TextUtils.isEmpty(mRefData.refCodeId)) {
            showMessage("请先在抬头界面获取单据信息");
            return;
        }

        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        InvEntity invEntity = mInvDatas.get(spInv.getSelectedItemPosition());
        InventoryQueryParam param = provideInventoryQueryParam();
        //获取建议仓位
        mPresenter.getSuggestedLocation(mRefData.refCodeId, mBizType, mRefType,
                Global.USER_ID, mRefData.workId, mRefData.invId, mRefData.recWorkId,
                mRefData.recInvId, param.queryType, lineData.workId,
                invEntity.invId, lineData.workCode, invEntity.invCode, "", getString(etMaterialNum),
                lineData.materialId, "", getString(etBatchFlag), "", "", param.invType, "", param.extraMap);
    }

    @Override
    public void getSuggestedLocationSuccess(InventoryEntity suggestedInventory) {
        if (suggestedInventory != null) {
            tvSuggestedLocation.setText(suggestedInventory.locationCombine);
        }
        if(TextUtils.isEmpty(mAutoLocation)) {
            //如果没有输入下架仓位，那么使用建议仓位
            super.getSuggestedLocationSuccess(suggestedInventory);
        }
    }

    @Override
    public void getSuggestedLocationComplete() {
        if(!TextUtils.isEmpty(mAutoLocation)) {
            //如果用户输入了下架仓位，匹配输入的下架仓位
            UiUtil.setSelectionForLocation(mInventoryDatas, mAutoLocation, spLocation);
        }
    }

    //重写该方法的目的是将副计量单位的仓位数量赋值
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
                tvLocQuantityCustom.setText("0");
                return;
            }
            //如果有缓存，但是可能匹配不上
            tvLocQuantity.setText("0");
            tvLocQuantityCustom.setText("0");
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
                if (!isOpenBatchManager) {
                    isMatch = locationCombine.equalsIgnoreCase(cachedItem.locationCombine);
                } else {
                    if (TextUtils.isEmpty(cachedItem.batchFlag) && TextUtils.isEmpty(batchFlag)) {
                        isMatch = locationCombine.equalsIgnoreCase(cachedItem.locationCombine);
                    } else if (!TextUtils.isEmpty(cachedItem.batchFlag) && !TextUtils.isEmpty(batchFlag)) {
                        isMatch = locationCombine.equalsIgnoreCase(cachedItem.locationCombine) && batchFlag.equalsIgnoreCase(cachedItem.batchFlag);
                    }
                }

                L.e("isBatchValidate = " + isBatchValidate + "; isMatch = " + isMatch);

                //注意它没有匹配成功，可能是批次没有匹配也可能是仓位没有匹配。
                if (isMatch) {
                    tvLocQuantity.setText(cachedItem.quantity);
                    //副计量单位的仓位数量
                    tvLocQuantityCustom.setText(cachedItem.quantityCustom);
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
    public void loadCacheFail(String message) {
        //副计量单位的仓位数量
        tvLocQuantityCustom.setText("0");
        tvTotalQuantityCustom.setText("0");
        super.loadCacheFail(message);
    }

    //增加副计量单位的校验
    @Override
    protected boolean refreshQuantity(final String quantity) {
        //如果副计量单位不为空
        if (!TextUtils.isEmpty(getString(tvMaterialUnitCustom))) {
            float totalQuantityCustomV = 0.0f;
            //副计量单位的累计数量
            totalQuantityCustomV += CommonUtil.convertToFloat(getString(tvTotalQuantityCustom), 0.0f);
            //副计量单位的应该数量
            final float actQuantityCustomV = CommonUtil.convertToFloat(getString(tvActQuantityCustom), 0.0f);
            //本次副计量单位的出库数量
            final float quantityCustomV = CommonUtil.convertToFloat(getString(etQuantityCustom), 0.0f);
            if (Float.compare(quantityCustomV + totalQuantityCustomV, actQuantityCustomV) > 0.0f) {
                showMessage("副计量单位实发数量输入有误，请重新输入");
                etQuantity.setText("");
                return false;
            }
            //该仓位的历史出库数量
            final float historyQuantityCustomV = CommonUtil.convertToFloat(getString(tvLocQuantityCustom), 0.0f);
            //该仓位的库存数量
            float inventoryQuantityCustomV = CommonUtil.convertToFloat(getString(tvInvQuantityCustom), 0.0f);

            if (Float.compare(quantityCustomV + historyQuantityCustomV, inventoryQuantityCustomV) > 0.0f) {
                showMessage("副计量单位实发数量输入有误，请重新输入");
                etQuantity.setText("");
                return false;
            }
        }
        return super.refreshQuantity(quantity);
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantityCustom = getString(etQuantityCustom);
        result.batchFlag = !isOpenBatchManager ? null : getString(etBatchFlag);
        return result;
    }

    /**
     * 数据保存成功，更新副计量为的累计数量
     *
     * @param message
     */
    @Override
    public void saveCollectedDataSuccess(String message) {
        if (!TextUtils.isEmpty(getString(tvMaterialUnitCustom))) {
            tvTotalQuantity.setText(String.valueOf(ArithUtil.add(getString(etQuantityCustom), getString(tvTotalQuantityCustom))));
            tvLocQuantityCustom.setText(String.valueOf(ArithUtil.add(getString(etQuantityCustom), getString(tvLocQuantityCustom))));
        }
        if (!cbSingle.isChecked()) {
            etQuantityCustom.setText("");
        }
        super.saveCollectedDataSuccess(message);
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(tvActQuantityCustom, tvLocQuantityCustom, tvMaterialUnitCustom,
                tvTotalQuantityCustom, etQuantityCustom, tvSuggestedLocation, tvInvQuantityCustom);
    }
}
