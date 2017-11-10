package com.richfit.sdk_wzck.base_ds_edit;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.LocationAdapter;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditFragment;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzck.R;
import com.richfit.sdk_wzck.R2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by monday on 2016/11/21.
 */

public abstract class BaseDSEditFragment<P extends IDSEditPresenter> extends BaseEditFragment<P>
        implements IDSEditView {

    @BindView(R2.id.tv_ref_line_num)
    protected TextView tvRefLineNum;
    @BindView(R2.id.tv_material_num)
    protected TextView tvMaterialNum;
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
    //增加仓储类型
    @BindView(R2.id.ll_location_type)
    LinearLayout llLocationType;
    @BindView(R2.id.sp_location_type)
    protected Spinner spLocationType;

    /*仓储类型*/
    protected List<SimpleEntity> mLocationTypes;
    protected String mRefLineId;
    protected String mLocationId;
    protected int mPosition;
    //该子节点修改前的出库数量
    protected String mQuantity;
    protected List<InventoryEntity> mInventoryDatas;
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
    protected void initView() {
        llLocationType.setVisibility(isOpenLocationType ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initEvent() {
        //选择下架仓位，刷新库存数量并且请求缓存，注意缓存是用来刷新仓位数量和累计数量
        RxAdapterView.itemSelections(spLocation)
                .filter(position -> mInventoryDatas != null && isValidatedLocation())
                .subscribe(position -> getTransferSingle(position));

        //选择仓储类型获取库存
        RxAdapterView.itemSelections(spLocationType)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(a -> isOpenLocationType)
                //注意工厂和库存地点必须使用行里面的
                .subscribe(position -> loadInventoryInfo());
    }


    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mSelectedLocationCombine = bundle.getString(Global.EXTRA_LOCATION_KEY);
        mSpecialInvFlag = bundle.getString(Global.EXTRA_SPECIAL_INV_FLAG_KEY);
        mSpecialInvNum = bundle.getString(Global.EXTRA_SPECIAL_INV_NUM_KEY);
        String totalQuantity = bundle.getString(Global.EXTRA_TOTAL_QUANTITY_KEY);
        String batchFlag = bundle.getString(Global.EXTRA_BATCH_FLAG_KEY);
        String invId = bundle.getString(Global.EXTRA_INV_ID_KEY);
        String invCode = bundle.getString(Global.EXTRA_INV_CODE_KEY);
        mPosition = bundle.getInt(Global.EXTRA_POSITION_KEY);
        mQuantity = bundle.getString(Global.EXTRA_QUANTITY_KEY);
        mLocationCombines = bundle.getStringArrayList(Global.EXTRA_LOCATION_LIST_KEY);
        mRefLineId = bundle.getString(Global.EXTRA_REF_LINE_ID_KEY);
        mLocationId = bundle.getString(Global.EXTRA_LOCATION_ID_KEY);
        if (mRefData != null) {
            /*单据数据中的库存地点不一定有，而且用户可以录入新的库存地点，所以只有子节点的库存地点才是正确的*/
            RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
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
            loadInventoryInfo();
        }
        if (isOpenLocationType)
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
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp,
                    mLocationTypes, false);
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
     * 获取库存。注意这里获取的是所有仓位的库存，在用户选择仓位之后进行过滤选择
     */
    protected void loadInventoryInfo() {
        //拦截住在仓储类型还未初始化就去获取库粗
        if (isOpenLocationType && (spLocationType.getAdapter() == null || mLocationTypes == null ||
                mLocationTypes.size() == 0)) {
            return;
        }
        //清除之前匹配的缓存
        tvInvQuantity.setText("");
        tvLocQuantity.setText("");
        tvTotalQuantity.setText("");

        //准备数据
        RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        String workId = lineData.workId;
        String workCode = lineData.workCode;
        String invId = CommonUtil.Obj2String(tvInv.getTag());
        String invCode = getString(tvInv);
        String materialId = lineData.materialId;
        String batchFlag = getString(tvBatchFlag);

        //检查批次，库存地点等字段
        if (TextUtils.isEmpty(workId)) {
            showMessage("工厂为空");
            return;
        }
        if (TextUtils.isEmpty(invId)) {
            showMessage("库存地点");
            return;
        }
        //开始加载库存
        InventoryQueryParam param = provideInventoryQueryParam();
        mPresenter.getInventoryInfo(param.queryType, workId, invId, workCode, invCode, "",
                getString(tvMaterialNum), materialId, "", batchFlag,
                lineData.specialInvFlag, lineData.specialInvNum, param.invType, param.extraMap);
    }

    @Override
    public void showInventory(List<InventoryEntity> list) {
        if (mInventoryDatas == null) {
            mInventoryDatas = new ArrayList<>();
        }
        mInventoryDatas.clear();
        InventoryEntity tmp = new InventoryEntity();
        tmp.locationCombine = "请选择";
        mInventoryDatas.add(tmp);
        mInventoryDatas.addAll(list);

        //这里采用局部变量的原因是，煤层气增加的仓储类型。每次选择仓储类型都必须去获取最新的库存信息
        LocationAdapter adapter = new LocationAdapter(mActivity, R.layout.item_simple_sp, mInventoryDatas);
        spLocation.setAdapter(adapter);

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
        if (pos >= 0 && pos < mInventoryDatas.size()) {
            spLocation.setSelection(pos);
        } else {
            spLocation.setSelection(0);
        }
    }

    @Override
    public void loadInventoryFail(String message) {
        showMessage(message);
    }

    /**
     * 选择下拉仓位匹配单条缓存
     *
     * @param position:下拉仓位的位置
     */
    protected void getTransferSingle(int position) {
        final String invQuantity = mInventoryDatas.get(position).invQuantity;
        //检测是否选择了发出库位
        if (position <= 0) {
            spLocation.setSelection(0);
            tvInvQuantity.setText("");
            tvLocQuantity.setText("");
            tvTotalQuantity.setText("");
            return;
        }
        //如果满足条件，那么先显示库存数量
        tvInvQuantity.setText(invQuantity);
        mPresenter.getTransferInfoSingle(mRefData.refCodeId, mRefData.refType,
                mRefData.bizType, mRefLineId, getString(tvMaterialNum),
                getString(tvBatchFlag),
                mInventoryDatas.get(position).locationCombine,
                "", -1, Global.USER_ID);
    }

    //获取缓存成功，显示缓存
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
            boolean isMatch = false;
            String locationType = "";
            if (isOpenLocationType) {
                locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
            }
            for (LocationInfoEntity info : locationInfos) {
                if (isOpenLocationType) {
                    isMatch = isOpenBatchManager ? location.equalsIgnoreCase(info.locationCombine) &&
                            batchFlag.equalsIgnoreCase(info.batchFlag) && locationType.equalsIgnoreCase(info.locationType) :
                            location.equalsIgnoreCase(info.locationCombine) && locationType.equalsIgnoreCase(info.locationType);
                } else {
                    isMatch = isOpenBatchManager ? location.equalsIgnoreCase(info.locationCombine) &&
                            batchFlag.equalsIgnoreCase(info.batchFlag) : location.equalsIgnoreCase(info.locationCombine);
                }
                if (isMatch) {
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
        //这里需要兼容离线，而离线是没有库存
        if (mInventoryDatas != null && mInventoryDatas.size() > 0) {
            result.location = mInventoryDatas.get(locationPos).location;
            result.specialInvFlag = mInventoryDatas.get(locationPos).specialInvFlag;
            result.specialInvNum = mInventoryDatas.get(locationPos).specialInvNum;
            result.specialConvert = (!TextUtils.isEmpty(result.specialInvFlag) && "k".equalsIgnoreCase(result.specialInvFlag)
                    && !TextUtils.isEmpty(result.specialInvNum)) ?
                    "Y" : "N";
        }
        result.batchFlag = !isOpenBatchManager ? Global.DEFAULT_BATCHFLAG : getString(tvBatchFlag);
        result.quantity = getString(etQuantity);
        result.invType = param.invType;
        if (isOpenLocationType)
            //仓储类型
            result.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
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
                loadInventoryInfo();
                break;
        }
        super.retry(retryAction);
    }

    /**
     * 用户修改的仓位不允许与其他子节点的仓位一致
     *
     * @return
     */
    protected boolean isValidatedLocation() {
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
}
