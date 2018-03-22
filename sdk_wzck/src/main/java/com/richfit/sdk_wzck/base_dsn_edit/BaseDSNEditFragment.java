package com.richfit.sdk_wzck.base_dsn_edit;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.richfit.domain.bean.ReferenceEntity;
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
 * Created by monday on 2017/2/27.
 */

public abstract class BaseDSNEditFragment<P extends IDSNEditPresenter> extends BaseEditFragment<P>
        implements IDSNEditView {

    @BindView(R2.id.tv_material_num)
    protected TextView tvMaterialNum;
    @BindView(R2.id.tv_material_desc)
    TextView tvMaterialDesc;
    @BindView(R2.id.tv_material_group)
    TextView tvMaterialGroup;
    @BindView(R2.id.tv_material_unit)
    TextView tvMaterialUnit;
    @BindView(R2.id.tv_batch_flag)
    protected TextView tvBatchFlag;
    @BindView(R2.id.tv_inv)
    protected TextView tvInv;
    @BindView(R2.id.sp_location)
    protected Spinner spLocation;
    @BindView(R2.id.tv_inv_quantity)
    TextView tvInvQuantity;
    @BindView(R2.id.tv_location_quantity)
    TextView tvLocQuantity;
    @BindView(R2.id.et_quantity)
    protected EditText etQuantity;
    //增加仓储类型
    @BindView(R2.id.ll_location_type)
    LinearLayout llLocationType;
    @BindView(R2.id.sp_location_type)
    protected Spinner spLocationType;

    /*仓储类型*/
    protected List<SimpleEntity> mLocationTypes;
    protected String mLocationId;
    String mQuantity;
    /*修改前的发出仓位*/
    String mSelectedLocationCombine;
    protected String mSpecialInvFlag;
    protected String mSpecialInvNum;
    /*修改前的其他子节点的发出仓位列表*/
    ArrayList<String> mLocationCombines;

    /*库存列表*/
    protected List<InventoryEntity> mInventoryDatas;
    private LocationAdapter mLocAdapter;

    /*缓存的历史仓位数量*/
    private List<RefDetailEntity> mHistoryDetailList;


    @Override
    protected int getContentId() {
        return R.layout.wzck_fragment_base_dsn_edit;
    }

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        super.initVariable(savedInstanceState);
        mInventoryDatas = new ArrayList<>();
    }

    @Override
    protected void initView() {
        llLocationType.setVisibility(isOpenLocationType ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initEvent() {
        //选择下架仓位，刷新库存并且请求缓存
        RxAdapterView
                .itemSelections(spLocation)
                .filter(position -> position >= 0 && isValidatedSendLocation())
                .subscribe(position -> {
                    //库存数量
                    tvInvQuantity.setText(mInventoryDatas.get(position).invQuantity);
                    //获取缓存
                    loadLocationQuantity(position);
                });
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
        //物料编码
        final String materialNum = bundle.getString(Global.EXTRA_MATERIAL_NUM_KEY);
        final String materialId = bundle.getString(Global.EXTRA_MATERIAL_ID_KEY);
        //库存地点
        final String invId = bundle.getString(Global.EXTRA_INV_ID_KEY);
        final String invCode = bundle.getString(Global.EXTRA_INV_CODE_KEY);
        //下架仓位
        mSelectedLocationCombine = bundle.getString(Global.EXTRA_LOCATION_KEY);
        mSpecialInvFlag = bundle.getString(Global.EXTRA_SPECIAL_INV_FLAG_KEY);
        mSpecialInvNum = bundle.getString(Global.EXTRA_SPECIAL_INV_NUM_KEY);
        //发出批次
        final String batchFlag = bundle.getString(Global.EXTRA_BATCH_FLAG_KEY);
        //接收仓位
        final String recLocation = bundle.getString(Global.EXTRA_REC_LOCATION_KEY);
        //接收批次
        final String recBatchFlag = bundle.getString(Global.EXTRA_REC_BATCH_FLAG_KEY);
        //移库数量
        mQuantity = bundle.getString(Global.EXTRA_QUANTITY_KEY);
        //其他子节点的发出仓位列表
        mLocationCombines = bundle.getStringArrayList(Global.EXTRA_LOCATION_LIST_KEY);
        mLocationId = bundle.getString(Global.EXTRA_LOCATION_ID_KEY);
        //绑定数据
        tvMaterialNum.setText(materialNum);
        tvMaterialNum.setTag(materialId);
        tvInv.setText(invCode);
        tvInv.setTag(invId);
        etQuantity.setText(mQuantity);
        tvBatchFlag.setText(batchFlag);

        //获取缓存信息
        mPresenter.getTransferInfoSingle(mRefData.bizType, materialNum,
                Global.USER_ID, mRefData.workId, mRefData.invId, mRefData.recWorkId,
                mRefData.recInvId, batchFlag, "", -1);
    }

    @Override
    public void onBindCommonUI(ReferenceEntity refData, String batchFlag) {
        RefDetailEntity data = refData.billDetailList.get(0);
        //刷新UI
        tvMaterialNum.setTag(data.materialId);
        tvMaterialDesc.setText(data.materialDesc);
        tvMaterialGroup.setText(data.materialGroup);
        tvMaterialUnit.setText(data.materialUnit);
        tvBatchFlag.setText(!TextUtils.isEmpty(data.batchFlag) ? data.batchFlag :
                batchFlag);
        mHistoryDetailList = refData.billDetailList;
    }

    @Override
    public void loadTransferSingleInfoComplete() {
        loadInventoryInfo();
    }

    @Override
    public void loadTransferSingleInfoFail(String message) {
        showMessage(message);
    }

    protected void loadInventoryInfo() {
        //拦截住在仓储类型还未初始化就去获取库粗
        if (isOpenLocationType && (spLocationType.getAdapter() == null || mLocationTypes == null ||
                mLocationTypes.size() == 0)) {
            mPresenter.getDictionaryData("locationType");
            return;
        }

        //获取库存信息
        InventoryQueryParam param = provideInventoryQueryParam();
        mPresenter.getInventoryInfo(param.queryType, mRefData.workId,
                CommonUtil.Obj2String(tvInv.getTag()), mRefData.workCode, getString(tvInv),
                "", getString(tvMaterialNum), tvMaterialNum.getTag().toString(),
                "", getString(tvBatchFlag), "", "", param.invType, param.extraMap);
    }

    @Override
    public void showInventory(List<InventoryEntity> list) {
        mInventoryDatas.clear();
        mInventoryDatas.addAll(list);
        if (mLocAdapter == null) {
            mLocAdapter = new LocationAdapter(mActivity, R.layout.item_simple_sp, mInventoryDatas);
            spLocation.setAdapter(mLocAdapter);
        } else {
            mLocAdapter.notifyDataSetChanged();
        }

        //自动选定用户修改前的发出仓位
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

    /**
     * 用户选择发出仓位，匹配该仓位上的仓位数量
     */
    private void loadLocationQuantity(int position) {
        final String locationCombine = mInventoryDatas.get(position).locationCombine;
        final String batchFlag = mInventoryDatas.get(position).batchFlag;
        final String invQuantity = mInventoryDatas.get(position).invQuantity;


        if (TextUtils.isEmpty(locationCombine)) {
            showMessage("发出仓位为空");
            return;
        }

        if (mHistoryDetailList == null) {
            showMessage("请先获取物料信息");
            return;
        }
        tvInvQuantity.setText(invQuantity);
        String locQuantity = "0";
        String locationType = "";
        boolean isMatched = false;
        if (isOpenLocationType) {
            locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
        }
        for (RefDetailEntity detail : mHistoryDetailList) {
            List<LocationInfoEntity> locationList = detail.locationList;
            if (locationList != null && locationList.size() > 0) {
                for (LocationInfoEntity locationInfo : locationList) {
                    if (isOpenLocationType) {
                        isMatched = isOpenBatchManager ? locationCombine.equalsIgnoreCase(locationInfo.locationCombine)
                                && batchFlag.equalsIgnoreCase(locationInfo.batchFlag) && locationType.equals(locationInfo.locationType) :
                                locationCombine.equalsIgnoreCase(locationInfo.locationCombine) && locationType.equals(locationInfo.locationType);
                    } else {
                        isMatched = isOpenBatchManager ? locationCombine.equalsIgnoreCase(locationInfo.locationCombine)
                                && batchFlag.equalsIgnoreCase(locationInfo.batchFlag) :
                                locationCombine.equalsIgnoreCase(locationInfo.locationCombine);
                    }
                    if (isMatched) {
                        locQuantity = locationInfo.quantity;
                        break;
                    }
                }
            }
        }
        tvLocQuantity.setText(locQuantity);
    }

    /**
     * 用户修改的仓位不允许与其他子节点的仓位一致
     *
     * @return
     */
    private boolean isValidatedSendLocation() {
        if (TextUtils.isEmpty(mSelectedLocationCombine)) {
            return false;
        }
        if (mLocationCombines == null || mLocationCombines.size() == 0)
            return true;
        for (String location : mLocationCombines) {
            if (mSelectedLocationCombine.equalsIgnoreCase(location)) {
                showMessage("您修改的仓位不合理,请重新输入");
                spLocation.setSelection(0);
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean checkCollectedDataBeforeSave() {

        //检查是否合理，可以保存修改后的数据
        if (TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请输入移库数量");
            return false;
        }

        if (Float.parseFloat(getString(etQuantity)) <= 0.0f) {
            showMessage("输入移库数量有误，请重新输入");
            return false;
        }

        //修改后的出库数量
        float quantityV = CommonUtil.convertToFloat(getString(etQuantity), 0.0f);
        //是否满足本次录入数量<=库存数量
        final float invQuantityV = CommonUtil.convertToFloat(getString(tvInvQuantity), 0.0f);
        if (Float.compare(quantityV, invQuantityV) > 0.0f) {
            showMessage("实发数量有误,请重新输入");
            etQuantity.setText("");
            return false;
        }
        return true;
    }

    @Override
    public void showOperationMenuOnCollection(final String companyCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("提示");
        builder.setMessage("您真的需要保存数据吗?点击确定将保存数据.");
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            saveCollectedData();
        });
        builder.show();
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = new ResultEntity();
        InventoryQueryParam param = provideInventoryQueryParam();
        result.businessType = mRefData.bizType;
        result.voucherDate = mRefData.voucherDate;
        result.moveType = mRefData.moveType;
        result.userId = Global.USER_ID;
        result.workId = mRefData.workId;
        result.locationId = mLocationId;
        result.invId = CommonUtil.Obj2String(tvInv.getTag());
        result.recWorkId = mRefData.recWorkId;
        result.recInvId = mRefData.recInvId;
        result.materialId = tvMaterialNum.getTag().toString();
        result.batchFlag = !isOpenBatchManager ? Global.DEFAULT_BATCHFLAG : getString(tvBatchFlag);
        result.costCenter = mRefData.costCenter;
        result.projectNum = mRefData.projectNum;
        final int position = spLocation.getSelectedItemPosition();
        result.location = mInventoryDatas.get(position).location;
        result.specialInvFlag = mInventoryDatas.get(position).specialInvFlag;
        result.specialInvNum = mInventoryDatas.get(position).specialInvNum;
        result.specialConvert = (!TextUtils.isEmpty(result.specialInvFlag) && "k".equalsIgnoreCase(result.specialInvFlag)
                && !TextUtils.isEmpty(result.specialInvNum)) ?
                "Y" : "N";
        result.quantity = getString(etQuantity);
        result.invType = param.invType;
        result.invFlag = mRefData.invFlag;
        if (isOpenLocationType)
            //仓储类型
            result.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
        result.modifyFlag = "Y";
        return result;
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        super.saveEditedDataSuccess(message);
        tvLocQuantity.setText(getString(etQuantity));
        int locationPos = spLocation.getSelectedItemPosition();
        if (locationPos >= 0 && locationPos < mInventoryDatas.size()) {
            mSelectedLocationCombine = mInventoryDatas.get(locationPos).locationCombine;
        }
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

    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        if (mLocationTypes != null && isOpenLocationType) {
            queryParam.extraMap = new HashMap<>();
            String locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
            queryParam.extraMap.put("locationType", locationType);
        }
        return queryParam;
    }

    @Override
    public void networkConnectError(String retryAction) {
        showNetConnectErrorDialog(retryAction);
    }

    @Override
    public void retry(String retryAction) {
        switch (retryAction) {
            case Global.RETRY_LOAD_SINGLE_CACHE_ACTION:
                mPresenter.getTransferInfoSingle(mRefData.bizType, getString(tvMaterialNum),
                        Global.USER_ID, mRefData.workId, mRefData.invId, mRefData.recWorkId,
                        mRefData.recInvId, getString(tvBatchFlag), "", -1);
                break;
            case Global.RETRY_SAVE_COLLECTION_DATA_ACTION:
                saveCollectedData();
                break;
        }
        super.retry(retryAction);
    }

    @Override
    public void loadSuggestInfoSuccess(String suggestLocation, String suggestBatchFlag) {

    }

    @Override
    public void loadSuggestInfoFail(String message) {
        showMessage(message);
    }
}
