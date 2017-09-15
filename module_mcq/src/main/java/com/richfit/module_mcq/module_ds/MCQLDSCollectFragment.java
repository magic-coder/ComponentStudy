package com.richfit.module_mcq.module_ds;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.common_lib.utils.ArithUtil;
import com.richfit.common_lib.utils.L;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_mcq.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

import java.util.List;

/**
 * 离线数据采集，这里由于不考虑库存，所以将下架仓位修改成输入。
 * 原来的控件隐藏，包括仓位，库存
 * Created by monday on 2017/9/11.
 */

public class MCQLDSCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {

    //副计量单位
    TextView tvMaterialUnitCustom;
    //副计量单位应发数量
    TextView tvActQuantityCustom;
    //副计量单位仓位数量
    TextView tvLocQuantityCustom;
    //副计量单位实发数量
    EditText etQuantityCustom;
    //副计量单位累计数量
    TextView tvTotalQuantityCustom;
    //建议下架仓位
    TextView tvSuggestedLocation;
    RichEditText etLocation;
    boolean isLocationChecked;

    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_lds_collect;
    }


    @Override
    public void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        tvMaterialUnitCustom = mView.findViewById(R.id.mcq_tv_material_unit_custom);
        tvActQuantityCustom = mView.findViewById(R.id.mcq_tv_act_quantity_custom);
        tvLocQuantityCustom = mView.findViewById(R.id.mcq_tv_location_quantity_custom);
        etQuantityCustom = mView.findViewById(R.id.mcq_et_quantity_custom);
        tvTotalQuantityCustom = mView.findViewById(R.id.mcq_tv_total_quantity_custom);
        tvSuggestedLocation = mView.findViewById(R.id.mcq_tv_suggested_location);

        etLocation = mView.findViewById(R.id.et_location);
        // etSpecialInvFlag = (EditText) mView.findViewById(R.id.et_special_inv_flag);
        // etSpecialInvNum = (EditText) mView.findViewById(R.id.et_special_inv_num);
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
        //禁用下架仓位
        spLocation.setEnabled(false);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        //开启输入仓位点击事件
        etLocation.setOnRichEditTouchListener((view, location) -> {
            hideKeyboard(view);
            checkLocation(getString(etLocation), location);
        });
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

    private void checkLocation(String batchFlag, String location) {
        tvLocQuantity.setText("");
        isLocationChecked = false;
        if (spRefLine.getSelectedItemPosition() <= 0) {
            showMessage("请先获取物料信息");
            return;
        }

        if (TextUtils.isEmpty(location)) {
            showMessage("请先输入发出仓位");
            return;
        }

        final String invId = mInvDatas.get(spInv.getSelectedItemPosition()).invId;
        //这里工厂id需要从行里面去取
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        mPresenter.checkLocation("04", lineData.workId, invId, batchFlag, location, null);
    }

    @Override
    public void checkLocationSuccess(String batchFlag, String location) {
        isLocationChecked = true;
        getTransferSingle(-1);
    }

    @Override
    protected void getTransferSingle(int position) {

        final String batchFlag = getString(etBatchFlag);
        final String location = getString(etLocation);
        // final String specialInvFlag = getString(etSpecialInvFlag);
        // final String specialInvNum = getString(etSpecialInvNum);

        String locationCombine = location;
       /* if (!TextUtils.isEmpty(specialInvFlag) && !TextUtils.isEmpty(specialInvNum)) {
            locationCombine = location + "_" + specialInvFlag + "_" + specialInvNum;
        }*/
        if (spRefLine.getSelectedItemPosition() <= 0) {
            showMessage("请先选择单据行");
            return;
        }
        //检验是否选择了库存地点
        if (spInv.getSelectedItemPosition() <= 0) {
            showMessage("请先选择库存地点");
            return;
        }

        if (isOpenBatchManager)
            if (TextUtils.isEmpty(batchFlag)) {
                showMessage("请先输入批次");
                return;
            }
        if (TextUtils.isEmpty(locationCombine)) {
            showMessage("请先输入发出仓位");
            return;
        }

        final RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        final String refCodeId = mRefData.refCodeId;
        final String refType = mRefData.refType;
        final String bizType = mRefData.bizType;
        final String refLineId = lineData.refLineId;
        mPresenter.getTransferInfoSingle(refCodeId, refType, bizType, refLineId, getString(etMaterialNum),
                batchFlag, locationCombine, lineData.refDoc, CommonUtil.convertToInt(lineData.refDocItem), Global.USER_ID);
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


    @Override
    protected boolean refreshQuantity(final String quantity) {
        //如果副计量单位不为空
        if (!TextUtils.isEmpty(getString(tvMaterialUnitCustom))) {
            //检查副计量单位实发数量
            float totalQuantityCustomV = 0.0f;
            //副计量单位的累计数量
            totalQuantityCustomV += CommonUtil.convertToFloat(getString(tvTotalQuantityCustom), 0.0f);
            //副计量单位的应该数量
            float actQuantityCustomV = CommonUtil.convertToFloat(getString(tvActQuantityCustom), 0.0f);
            //本次副计量单位的出库数量
            float quantityCustomV = CommonUtil.convertToFloat(getString(etQuantityCustom), 0.0f);
            if (Float.compare(quantityCustomV + totalQuantityCustomV, actQuantityCustomV) > 0.0f) {
                showMessage("副计量单位实发数量输入有误，请重新输入");
                etQuantity.setText("");
                return false;
            }
        }

        //检查主计量单位实发数量
        if (Float.valueOf(quantity) <= 0.0f) {
            showMessage("输入主计量单位实发数量不合理");
            return false;
        }

        float totalQuantityV = 0.0f;
        //累计数量
        totalQuantityV += CommonUtil.convertToFloat(getString(tvTotalQuantity), 0.0f);
        //应该数量
        float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        //本次出库数量
        float quantityV = CommonUtil.convertToFloat(quantity, 0.0f);
        if (Float.compare(quantityV + totalQuantityV, actQuantityV) > 0.0f) {
            showMessage("主计量单位实发数量有误，请重新输入");
            etQuantity.setText("");
            return false;
        }
        return true;
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (!etMaterialNum.isEnabled()) {
            showMessage("请先获取物料信息");
            return false;
        }

        //检查数据是否可以保存
        if (spRefLine.getSelectedItemPosition() <= 0) {
            showMessage("请先选择单据行");
            return false;
        }
        //库存地点
        if (spInv.getSelectedItemPosition() <= 0) {
            showMessage("请先选择库存地点");
            return false;
        }

        //物资条码
        if (isEmpty(getString(etMaterialNum))) {
            showMessage("请先输入物料条码");
            return false;
        }

        //批次
        if (!isSplitBatchFlag && isOpenBatchManager && !isBatchValidate) {
            showMessage("批次输入有误，请检查批次是否与缓存批次输入一致");
            return false;
        }
        //实发数量
        if (!cbSingle.isChecked() && TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请先输入数量");
            return false;
        }

        //增加下架仓位的检查
        if (TextUtils.isEmpty(getString(etLocation))) {
            showMessage("请先输入下架仓位");
            return false;
        }

        if (!refreshQuantity(cbSingle.isChecked() ? "1" : getString(etQuantity))) {
            return false;
        }
        return true;
    }



    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantityCustom = getString(etQuantityCustom);
        result.location = getString(etLocation);
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
                tvTotalQuantityCustom, etQuantityCustom, tvSuggestedLocation);
    }
}
