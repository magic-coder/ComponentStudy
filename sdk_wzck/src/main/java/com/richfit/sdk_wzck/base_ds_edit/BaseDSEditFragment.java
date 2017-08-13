package com.richfit.sdk_wzck.base_ds_edit;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.LocationAdapter;
import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditFragment;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzck.R;
import com.richfit.sdk_wzck.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by monday on 2016/11/21.
 */

public abstract class BaseDSEditFragment<P extends IDSEditPresenter> extends BaseEditFragment<P>
        implements IDSEditView {

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
    @BindView(R2.id.tv_act_delivery_quantity)
    protected TextView tvActQuantity;
    @BindView(R2.id.et_quantity)
    protected EditText etQuantity;
    @BindView(R2.id.sp_location)
    protected Spinner spLocation;
    @BindView(R2.id.tv_location_quantity)
    protected TextView tvLocQuantity;
    @BindView(R2.id.tv_total_quantity)
    protected TextView tvTotalQuantity;
    @BindView(R2.id.tv_inv_quantity)
    protected TextView tvInvQuantity;

    protected String mRefLineId;
    protected String mLocationId;
    protected int mPosition;
    //该子节点修改前的出库数量
    protected String mQuantity;
    protected List<InventoryEntity> mInventoryDatas;
    private LocationAdapter mLocationAdapter;
    private List<String> mLocationCombines;
    protected String mSpecialInvFlag;
    protected String mSpecialInvNum;
    protected String mSelectedLocationCombine;
    protected float mTotalQuantity;

    @Override
    protected int getContentId() {
        return R.layout.wzck_fragment_base_dsy_edit;
    }

    @Override
    public void initVariable(Bundle savedInstanceState) {
        mInventoryDatas = new ArrayList<>();
    }

    @Override
    public void initEvent() {
        //选择下架仓位，刷新库存数量并且请求缓存，注意缓存是用来刷新仓位数量和累计数量
        RxAdapterView
                .itemSelections(spLocation)
                .filter(position -> position >= 0 && isValidatedLocation())
                .subscribe(position -> {
                    //库存数量
                    tvInvQuantity.setText(mInventoryDatas.get(position).invQuantity);
                    //获取缓存
                    mPresenter.getTransferInfoSingle(mRefData.refCodeId, mRefData.refType,
                            mRefData.bizType, mRefLineId, getString(tvMaterialNum), getString(tvBatchFlag), mInventoryDatas.get(position).locationCombine,
                            "", -1, Global.USER_ID);
                });
    }

    /**
     * 用户修改的仓位不允许与其他子节点的仓位一致
     *
     * @return
     */
    private boolean isValidatedLocation() {
        if (TextUtils.isEmpty(mSelectedLocationCombine)) {
            return false;
        }
        for (String locationCombine : mLocationCombines) {
            if (mSelectedLocationCombine.equalsIgnoreCase(locationCombine)) {
                showMessage("该仓位已经存在");
                return false;
            }
        }
        return true;
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        mSelectedLocationCombine = bundle.getString(Global.EXTRA_LOCATION_KEY);
        mSpecialInvFlag = bundle.getString(Global.EXTRA_SPECIAL_INV_FLAG_KEY);
        mSpecialInvNum = bundle.getString(Global.EXTRA_SPECIAL_INV_NUM_KEY);
        final String totalQuantity = bundle.getString(Global.EXTRA_TOTAL_QUANTITY_KEY);
        final String batchFlag = bundle.getString(Global.EXTRA_BATCH_FLAG_KEY);
        final String invId = bundle.getString(Global.EXTRA_INV_ID_KEY);
        final String invCode = bundle.getString(Global.EXTRA_INV_CODE_KEY);
        mPosition = bundle.getInt(Global.EXTRA_POSITION_KEY);
        mQuantity = bundle.getString(Global.EXTRA_QUANTITY_KEY);
        mLocationCombines = bundle.getStringArrayList(Global.EXTRA_LOCATION_LIST_KEY);
        mRefLineId = bundle.getString(Global.EXTRA_REF_LINE_ID_KEY);
        mLocationId = bundle.getString(Global.EXTRA_LOCATION_ID_KEY);

        if (mRefData != null) {
            /*单据数据中的库存地点不一定有，而且用户可以录入新的库存地点，所以只有子节点的库存地点才是正确的*/
            final RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
            //拿到上架总数
            tvRefLineNum.setText(lineData.lineNum);
            tvMaterialNum.setText(lineData.materialNum);
            tvMaterialDesc.setText(lineData.materialDesc);
            tvMaterialUnit.setText(lineData.unit);
            tvActQuantity.setText(lineData.actQuantity);
            tvBatchFlag.setText(batchFlag);
            tvInv.setText(invCode);
            tvInv.setTag(invId);

            etQuantity.setText(mQuantity);
            tvTotalQuantity.setText(totalQuantity);
            //下载库存
            loadInventoryInfo(lineData.workId, lineData.workCode, invId, invCode,
                    lineData.materialId, "", batchFlag);
        }
    }

    /**
     * 获取库存
     *
     * @param workId
     * @param invId
     * @param materialId
     * @param location
     * @param batchFlag
     */
    protected void loadInventoryInfo(String workId, String workCode, String invId, String invCode,
                                     String materialId, String location, String batchFlag) {
        //检查批次，库存地点等字段
        if (TextUtils.isEmpty(workId)) {
            showMessage("工厂为空");
            return;
        }
        if (TextUtils.isEmpty(invId)) {
            showMessage("库存地点");
            return;
        }
        InventoryQueryParam param = provideInventoryQueryParam();
        mPresenter.getInventoryInfo(param.queryType, workId, invId, workCode, invCode, "",
                getString(tvMaterialNum), materialId, location, batchFlag, "", "", param.invType, "", param.extraMap);
    }

    @Override
    public void showInventory(List<InventoryEntity> list) {
        mInventoryDatas.clear();
        mInventoryDatas.addAll(list);
        if (mLocationAdapter == null) {
            mLocationAdapter = new LocationAdapter(mActivity, R.layout.item_simple_sp, mInventoryDatas);
            spLocation.setAdapter(mLocationAdapter);
        } else {
            mLocationAdapter.notifyDataSetChanged();
        }

        //默认选择已经下架的仓位
        if (TextUtils.isEmpty(mSelectedLocationCombine)) {
            spLocation.setSelection(0);
            return;
        }


        int pos = -1;
        for (InventoryEntity loc : mInventoryDatas) {
            pos++;
            //如果在修改前选择的是寄售库存的仓位
            if (mSelectedLocationCombine.equalsIgnoreCase(loc.locationCombine))
                break;
        }
        if (pos >= 0 && pos < list.size()) {
            spLocation.setSelection(pos);
        } else {
            spLocation.setSelection(0);
        }
    }

    @Override
    public void loadInventoryFail(String message) {
        showMessage(message);
    }

    @Override
    public void onBindCache(RefDetailEntity cache, String batchFlag, String location) {
        if (cache != null) {
            tvTotalQuantity.setText(cache.totalQuantity);

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
                        batchFlag.equalsIgnoreCase(info.batchFlag) : location.equalsIgnoreCase(info.locationCombine)) {
                    tvLocQuantity.setText(info.quantity);
                    break;
                }
            }
        }
    }

    @Override
    public void loadCacheFail(String message) {
        showMessage(message);
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {

        //检查是否合理，可以保存修改后的数据
        if (TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请输入实发数量");
            return false;
        }

        if (TextUtils.isEmpty(getString(tvInvQuantity))) {
            showMessage("请先获取库存");
            return false;
        }


        if (Float.parseFloat(getString(etQuantity)) <= 0.0f) {
            showMessage("输入出库数量不合理,请重新输入");
            return false;
        }

        //是否满足本次录入数量+累计数量-上次已经录入的出库数量<=应出数量
        float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        float totalQuantityV = CommonUtil.convertToFloat(getString(tvTotalQuantity), 0.0f);
        float collectedQuantity = CommonUtil.convertToFloat(mQuantity, 0.0f);
        //修改后的出库数量
        float quantityV = CommonUtil.convertToFloat(getString(etQuantity), 0.0f);
        float residualQuantity = totalQuantityV - collectedQuantity + quantityV;//减去已经录入的数量
        if (Float.compare(residualQuantity, actQuantityV) > 0.0f) {
            showMessage("输入实发数量有误");
            etQuantity.setText("");
            return false;
        }

        //该仓位的库存数量
        final float invQuantityV = CommonUtil.convertToFloat(getString(tvInvQuantity), 0.0f);
        if (Float.compare(quantityV, invQuantityV) > 0.0f) {
            showMessage("输入数量大于库存数量，请重新输入");
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
        InventoryQueryParam param = provideInventoryQueryParam();
        ResultEntity result = new ResultEntity();
        result.businessType = mRefData.bizType;
        result.refCodeId = mRefData.refCodeId;
        result.voucherDate = mRefData.voucherDate;
        result.refType = mRefData.refType;
        result.moveType = mRefData.moveType;
        result.userId = Global.USER_ID;
        result.refLineId = lineData.refLineId;
        result.workId = lineData.workId;
        result.locationId = mLocationId;
        result.refLineNum = lineData.lineNum;
        result.invId = tvInv.getTag().toString();
        result.materialId = lineData.materialId;
        result.unit = TextUtils.isEmpty(lineData.recordUnit) ? lineData.materialUnit : lineData.recordUnit;
        result.unitRate = Float.compare(lineData.unitRate, 0.0f) == 0 ? 1.f : lineData.unitRate;
        //库存相关的字段回传
        int locationPos = spLocation.getSelectedItemPosition();
        result.location = mInventoryDatas.get(locationPos).location;
        result.specialInvFlag = mInventoryDatas.get(locationPos).specialInvFlag;
        result.specialInvNum = mInventoryDatas.get(locationPos).specialInvNum;
        result.specialConvert = !TextUtils.isEmpty(result.specialInvFlag) && !TextUtils.isEmpty(result.specialInvNum) ?
                "Y" : "N";
        result.batchFlag = !isOpenBatchManager ? Global.DEFAULT_BATCHFLAG : getString(tvBatchFlag);
        result.quantity = getString(etQuantity);
        result.invType = param.invType;
        result.modifyFlag = "Y";
        return result;
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        super.saveEditedDataSuccess(message);
        tvTotalQuantity.setText(String.valueOf(mTotalQuantity));
        tvLocQuantity.setText(getString(etQuantity));
        int locationPos = spLocation.getSelectedItemPosition();
        if (locationPos >= 0 && locationPos < mInventoryDatas.size()) {
            mSelectedLocationCombine = mInventoryDatas.get(locationPos).locationCombine;
        }
    }

    @Override
    public void retry(String retryAction) {
        switch (retryAction) {
            case Global.RETRY_LOAD_INVENTORY_ACTION:
                final RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
                loadInventoryInfo(lineData.workId, lineData.workCode, getString(tvInv), CommonUtil.Obj2String(tvInv.getTag()), lineData.materialId, "", getString(tvBatchFlag));
                break;
        }
        super.retry(retryAction);
    }

}
