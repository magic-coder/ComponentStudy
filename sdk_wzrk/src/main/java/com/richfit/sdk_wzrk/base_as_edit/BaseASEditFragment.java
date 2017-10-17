package com.richfit.sdk_wzrk.base_as_edit;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditFragment;
import com.richfit.common_lib.utils.L;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzrk.R;
import com.richfit.sdk_wzrk.R2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


/**
 * Created by monday on 2016/11/19.
 */

public abstract class BaseASEditFragment<P extends IASEditPresenter> extends BaseEditFragment<P>
        implements IASEditView {

    @BindView(R2.id.tv_ref_line_num)
    protected TextView tvRefLineNum;
    @BindView(R2.id.tv_material_num)
    TextView tvMaterialNum;
    @BindView(R2.id.tv_material_desc)
    TextView tvMaterialDesc;
    @BindView(R2.id.tv_material_unit)
    TextView tvMaterialUnit;
    @BindView(R2.id.tv_batch_flag)
    protected TextView tvBatchFlag;
    @BindView(R2.id.tv_inv)
    protected TextView tvInv;
    @BindView(R2.id.tv_act_quantity)
    protected TextView tvActQuantity;
    @BindView(R2.id.act_quantity_name)
    protected TextView tvActQuantityName;
    @BindView(R2.id.quantity_name)
    protected TextView tvQuantityName;
    @BindView(R2.id.et_quantity)
    protected EditText etQuantity;
    @BindView(R2.id.et_location)
    protected EditText etLocation;
    @BindView(R2.id.tv_location_quantity)
    protected TextView tvLocationQuantity;
    @BindView(R2.id.tv_total_quantity)
    protected TextView tvTotalQuantity;
    @BindView(R2.id.ll_location)
    protected LinearLayout llLocation;
    @BindView(R2.id.ll_location_quantity)
    protected LinearLayout llLocationQuantity;
    //增加仓储类型
    @BindView(R2.id.ll_location_type)
    protected LinearLayout llLocationType;
    @BindView(R2.id.sp_location_type)
    protected Spinner spLocationType;

    /*仓储类型*/
    protected List<SimpleEntity> mLocationTypes;
    /*是否启用仓储类型*/
    private boolean isOpenLocationType = false;
    //已经上架的所有仓位
    protected List<String> mLocations;
    ///要修改子节点的id
    String mRefLineId;
    protected String mLocationId;
    /*修改之前用户数的数量*/
    protected String mQuantity;
    /*要修改的子节点的父节点所在明细数据集的索引*/
    protected int mPosition;
    //是否上架（直接通过父节点的标志位判断即可）
    protected boolean isNLocation;
    protected float mTotalQuantity;

    @Override
    protected int getContentId() {
        return R.layout.wzrk_fragment_base_asy_edit;
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        isOpenLocationType = llLocationType.getVisibility() != View.GONE;
        Bundle bundle = getArguments();
        final String location = bundle.getString(Global.EXTRA_LOCATION_KEY);
        final String totalQuantity = bundle.getString(Global.EXTRA_TOTAL_QUANTITY_KEY);
        final String batchFlag = bundle.getString(Global.EXTRA_BATCH_FLAG_KEY);
        final String invId = bundle.getString(Global.EXTRA_INV_ID_KEY);
        final String invCode = bundle.getString(Global.EXTRA_INV_CODE_KEY);

        isNLocation = "BARCODE".equalsIgnoreCase(location);

        mPosition = bundle.getInt(Global.EXTRA_POSITION_KEY);
        mQuantity = bundle.getString(Global.EXTRA_QUANTITY_KEY);
        mLocations = bundle.getStringArrayList(Global.EXTRA_LOCATION_LIST_KEY);
        mRefLineId = bundle.getString(Global.EXTRA_REF_LINE_ID_KEY);
        mLocationId = bundle.getString(Global.EXTRA_LOCATION_ID_KEY);
        if (mRefData != null) {
            /*单据数据中的库存地点不一定有，而且用户可以录入新的库存地点，所以只有子节点的库存地点才是正确的*/
            final RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
            tvRefLineNum.setText(lineData.lineNum);
            tvMaterialNum.setText(lineData.materialNum);
            tvMaterialDesc.setText(lineData.materialDesc);
            tvMaterialUnit.setText(lineData.unit);
            tvActQuantity.setText(lineData.actQuantity);
            tvBatchFlag.setText(batchFlag);
            tvInv.setText(invCode);
            tvInv.setTag(invId);
            etLocation.setEnabled(!isNLocation);
            if (!isNLocation) {
                etLocation.setText(location);
                tvLocationQuantity.setText(mQuantity);
            }
            etQuantity.setText(mQuantity);
            tvTotalQuantity.setText(totalQuantity);
        }
        //如果打开了仓储类型需要获取仓储类型数据源
        if (isOpenBatchManager)
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
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, mLocationTypes, false);
            spLocationType.setAdapter(adapter);

            //默认选择缓存的数据
            Bundle arguments = getArguments();
            if (arguments != null) {
                String locationType = arguments.getString(Global.EXTRA_LOCATION_TYPE_KEY);
                UiUtil.setSelectionForSimpleSp(mLocationTypes, locationType, spLocationType);
            }
        }
    }

    /**
     * 用户修改的仓位不允许与其他子节点的仓位一致
     *
     * @return
     */
    protected boolean isValidatedLocation(String location) {
        if (TextUtils.isEmpty(location)) {
            showMessage("请输入修改的仓位");
            return false;
        }
        if (mLocations == null || mLocations.size() == 0)
            return true;
        for (String str : mLocations) {
            if (str.equalsIgnoreCase(location)) {
                showMessage("您修改的仓位不合理,请重新输入");
                return false;
            }
        }
        return true;
    }


    /**
     * 保存修改数据前的检查，注意这里之类必须根据业务检查仓位
     *
     * @return
     */
    @Override
    public boolean checkCollectedDataBeforeSave() {

        if (!isNLocation && !isValidatedLocation(getString(etLocation))) {
            return false;
        }

        if (TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请输入入库数量");
            return false;
        }

        float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        float totalQuantityV = CommonUtil.convertToFloat(getString(tvTotalQuantity), 0.0f);
        float collectedQuantity = CommonUtil.convertToFloat(mQuantity, 0.0f);
        float quantityV = CommonUtil.convertToFloat(getString(etQuantity), 0.0f);
        if (Float.compare(quantityV, 0.0f) <= 0.0f) {
            showMessage("实收数量输入不合理");
            etQuantity.setText("");
            return false;
        }
        float residualQuantity = totalQuantityV - collectedQuantity + quantityV;//减去已经录入的数量
        if (Float.compare(residualQuantity, actQuantityV) > 0.0f) {
            showMessage("实收数量输入有误");
            etQuantity.setText("");
            return false;
        }
        mQuantity = quantityV + "";
        mTotalQuantity = residualQuantity;
        return true;
    }

    @Override
    public ResultEntity provideResult() {
        RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        ResultEntity result = new ResultEntity();
        InventoryQueryParam param = provideInventoryQueryParam();
        result.invType = param.invType;
        result.businessType = mRefData.bizType;
        result.refCodeId = mRefData.refCodeId;
        result.voucherDate = mRefData.voucherDate;
        result.refType = mRefData.refType;
        result.refLineNum = lineData.lineNum;
        result.moveType = mRefData.moveType;
        result.userId = Global.USER_ID;
        result.refLineId = lineData.refLineId;
        result.workId = lineData.workId;
        result.invId = CommonUtil.Obj2String(tvInv.getTag());
        result.locationId = mLocationId;
        result.materialId = lineData.materialId;
        result.location = isNLocation ? Global.DEFAULT_LOCATION : getString(etLocation);
        result.batchFlag = !isOpenBatchManager ? Global.DEFAULT_BATCHFLAG : getString(tvBatchFlag);
        result.quantity = getString(etQuantity);
        //物料凭证
        result.refDoc = lineData.refDoc;
        //物料凭证单据行
        result.refDocItem = lineData.refDocItem;
        //检验批数量
        result.insLot = lineData.insLot;
        result.unit = TextUtils.isEmpty(lineData.recordUnit) ? lineData.materialUnit : lineData.recordUnit;
        result.unitRate = Float.compare(lineData.unitRate, 0.0f) == 0 ? 1.f : lineData.unitRate;
        result.supplierNum = mRefData.supplierNum;
        //仓储类型
        if (isOpenLocationType)
            result.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
        result.modifyFlag = "Y";
        return result;
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        super.saveEditedDataSuccess(message);
        if (!isNLocation)
            tvLocationQuantity.setText(mQuantity);
        tvTotalQuantity.setText(String.valueOf(mTotalQuantity));
    }

    @Override
    public void retry(String retryAction) {
        switch (retryAction) {
            case Global.RETRY_EDIT_DATA_ACTION:
                saveCollectedData();
                break;
        }
        super.retry(retryAction);
    }

    @Override
    public void loadDictionaryDataFail(String message) {
        showMessage(message);
    }
}
