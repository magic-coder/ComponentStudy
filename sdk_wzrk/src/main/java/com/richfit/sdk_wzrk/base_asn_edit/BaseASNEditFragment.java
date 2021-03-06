package com.richfit.sdk_wzrk.base_asn_edit;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditFragment;
import com.richfit.common_lib.utils.L;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzrk.R;
import com.richfit.sdk_wzrk.R2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by monday on 2016/12/1.
 */

public abstract class BaseASNEditFragment<P extends IASNEditPresenter> extends BaseEditFragment<P>
        implements IASNEditView {

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
    @BindView(R2.id.et_location)
    protected EditText etLocation;
    @BindView(R2.id.tv_location_quantity)
    TextView tvLocQuantity;
    @BindView(R2.id.et_quantity)
    protected EditText etQuantity;
    //增加仓储类型
    @BindView(R2.id.ll_location_type)
    protected LinearLayout llLocationType;
    @BindView(R2.id.sp_location_type)
    Spinner spLocationType;


    String mQuantity;
    List<RefDetailEntity> mHistoryDetailList;
    String mLocation;
    String mLocationId;
    /*仓储类型*/
    protected List<SimpleEntity> mLocationTypes;
    /*是否上架*/
    protected boolean isLocation = true;

    @Override
    protected int getContentId() {
        return R.layout.wzrk_fragment_asn_edit;
    }

    @Override
    protected void initView() {
        llLocationType.setVisibility(isOpenLocationType ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initEvent() {
        //监听上架仓位输入，时时匹配缓存的仓位数量
        RxTextView.textChanges(etLocation)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(location -> isLocation && !TextUtils.isEmpty(location) && location.length() > 0)
                .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe(location -> loadLocationQuantity(location.toString(), getString(tvBatchFlag)), e -> L.d(e.getMessage()));
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        //物料编码
        final String materialNum = bundle.getString(Global.EXTRA_MATERIAL_NUM_KEY);
        final String materialId = bundle.getString(Global.EXTRA_MATERIAL_ID_KEY);
        //库位
        final String invId = bundle.getString(Global.EXTRA_INV_ID_KEY);
        final String invCode = bundle.getString(Global.EXTRA_INV_CODE_KEY);
        //批次
        final String batchFlag = bundle.getString(Global.EXTRA_BATCH_FLAG_KEY);
        //仓位
        mLocation = bundle.getString(Global.EXTRA_LOCATION_KEY);
        //仓位id
        mLocationId = bundle.getString(Global.EXTRA_LOCATION_ID_KEY);
        //入库数量
        mQuantity = bundle.getString(Global.EXTRA_QUANTITY_KEY);
        //绑定数据
        tvMaterialNum.setText(materialNum);
        tvMaterialNum.setTag(materialId);
        tvInv.setText(invCode);
        tvInv.setTag(invId);
        etQuantity.setText(mQuantity);
        tvLocQuantity.setText(mQuantity);
        tvBatchFlag.setText(batchFlag);
        etLocation.setEnabled(false);
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
        tvMaterialUnit.setText(data.unit);
        tvBatchFlag.setText(!TextUtils.isEmpty(data.batchFlag) ? data.batchFlag : batchFlag);
        mHistoryDetailList = refData.billDetailList;
    }

    @Override
    public void loadTransferSingleInfoFail(String message) {
        showMessage(message);
    }

    @Override
    public void loadTransferSingeInfoComplete() {
        //设置上架仓位，系统自动匹配最新的缓存
        etLocation.setText(mLocation);
        //如果打开了仓储类型需要获取仓储类型数据源
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
     * 加载缓存中仓位数量
     *
     * @param location
     */
    private void loadLocationQuantity(String location, String batchFlag) {

        if (isLocation && TextUtils.isEmpty(location)) {
            showMessage("仓位为空");
            return;
        }

        if (mHistoryDetailList == null) {
            showMessage("请先获取物料信息");
            return;
        }

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
                        isMatched = isOpenBatchManager ? location.equalsIgnoreCase(locationInfo.location)
                                && batchFlag.equalsIgnoreCase(locationInfo.batchFlag) && locationType.equals(locationInfo.locationType) :
                                location.equalsIgnoreCase(locationInfo.location) && locationType.equals(locationInfo.locationType);
                    } else {
                        isMatched = isOpenBatchManager ? location.equalsIgnoreCase(locationInfo.location)
                                && batchFlag.equalsIgnoreCase(locationInfo.batchFlag) :
                                location.equalsIgnoreCase(locationInfo.location);
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

    @Override
    public boolean checkCollectedDataBeforeSave() {

        //检查是否合理，可以保存修改后的数据
        if (isLocation && TextUtils.isEmpty(getString(etLocation))) {
            showMessage("请输入上架仓位");
            return false;
        }

        if (isLocation && TextUtils.isEmpty(mLocationId)) {
            showMessage("仓位id为空");
            return false;
        }

        if (TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请输入移库数量");
            return false;
        }

        if (Float.parseFloat(getString(etQuantity)) <= 0.0f) {
            showMessage("输入移库数量有误，请重新输入");
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
        result.invType = param.invType;
        result.queryType = param.queryType;
        result.businessType = mRefData.bizType;
        result.voucherDate = mRefData.voucherDate;
        result.moveType = mRefData.moveType;
        result.userId = Global.USER_ID;
        result.workId = mRefData.workId;
        result.invId = CommonUtil.Obj2String(tvInv.getTag());
        result.recWorkId = mRefData.recWorkId;
        result.recInvId = mRefData.recInvId;
        result.locationId = mLocationId;
        result.materialId = CommonUtil.Obj2String(tvMaterialNum.getTag());
        result.batchFlag = !isOpenBatchManager ? Global.DEFAULT_BATCHFLAG : getString(tvBatchFlag);
        result.location = isLocation ? getString(etLocation) : Global.DEFAULT_LOCATION;
        result.quantity = getString(etQuantity);
        result.projectNum = mRefData.projectNum;
        result.supplierId = mRefData.supplierId;
        result.specialInvFlag = mRefData.specialInvFlag;
        //仓储类型
        if (isOpenLocationType)
            result.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
        result.modifyFlag = "Y";
        return result;
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        super.saveEditedDataSuccess(message);
        tvLocQuantity.setText(getString(etQuantity));
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
}
