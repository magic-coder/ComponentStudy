package com.richfit.sdk_wzrk.base_asn_collect;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectFragment;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.common_lib.widget.RichAutoEditText;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzrk.R;
import com.richfit.sdk_wzrk.R2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 注意庆阳的无参考出库没有上架仓位，默认为barcode
 * Created by monday on 2016/11/27.
 */

public abstract class BaseASNCollectFragment<P extends IASNCollectPresenter> extends BaseCollectFragment<P>
        implements IASNCollectView {

    @BindView(R2.id.et_material_num)
    protected RichEditText etMaterialNum;
    @BindView(R2.id.tv_material_desc)
    TextView tvMaterialDesc;
    @BindView(R2.id.tv_material_group)
    TextView tvMaterialGroup;
    @BindView(R2.id.tv_material_unit)
    TextView tvMaterialUnit;
    @BindView(R2.id.tv_special_inv_flag)
    protected TextView tvSpecialInvFlag;
    @BindView(R2.id.et_batch_flag)
    protected EditText etBatchFlag;
    @BindView(R2.id.sp_inv)
    protected Spinner spInv;
    @BindView(R2.id.et_location)
    protected RichAutoEditText etLocation;
    @BindView(R2.id.tv_location_quantity)
    TextView tvLocQuantity;
    @BindView(R2.id.et_quantity)
    protected EditText etQuantity;
    @BindView(R2.id.cb_single)
    CheckBox cbSingle;
    //增加仓储类型
    @BindView(com.richfit.common_lib.R2.id.ll_location_type)
    protected LinearLayout llLocationType;
    @BindView(com.richfit.common_lib.R2.id.sp_location_type)
    Spinner spLocationType;

    /*库存地点数据和适配器*/
    private InvAdapter mInvAdapter;
    protected List<InvEntity> mInvs;
    /*单条缓存数据列表*/
    private List<RefDetailEntity> mHistoryDetailList;
    /*上架仓位列表适配器*/
    ArrayAdapter<String> mLocAdapter;
    List<String> mLocationList;
    /*是否发不上架*/
    protected boolean isNLocation = false;
    /*校验仓位是否存在，如果false表示校验该仓位不存在或者没有校验该仓位，不允许保存数据*/
    protected boolean isLocationChecked = false;
    /*仓储类型*/
    protected List<SimpleEntity> mLocationTypes;

    /**
     * 处理扫描
     *
     * @param type
     * @param list
     */
    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type, list);
        if (list != null && list.length > 12) {
            if (!etMaterialNum.isEnabled()) {
                showMessage("请先在抬头界面获取相关数据");
                return;
            }
            final String materialNum = list[Global.MATERIAL_POS];
            final String batchFlag = list[Global.BATCHFALG_POS];
            if (cbSingle.isChecked() && materialNum.equalsIgnoreCase(getString(etMaterialNum))) {
                //如果已经选中单品，那么说明已经扫描过一次。必须保证每一次的物料都一样
                saveCollectedData();
            } else {
                loadMaterialInfo(materialNum, batchFlag);
            }
        } else if (list != null && list.length == 1 && !cbSingle.isChecked()) {
            final String location = list[0];
            clearCommonUI(etLocation);
            etLocation.setText(location);
            getTransferSingle(getString(etBatchFlag), location);
        } else if (list != null && list.length == 2 && !cbSingle.isChecked() && isOpenLocationType) {
            //处理仓储类型
            String location = list[Global.LOCATION_POS];
            String locationType = list[Global.LOCATION_TYPE_POS];
            clearCommonUI(etLocation);
            etLocation.setText(location);
            //自动选择仓储类型
            UiUtil.setSelectionForSimpleSp(mLocationTypes, locationType, spLocationType);
            getTransferSingle(getString(etBatchFlag), location);
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.wzrk_fragment_asn_collect;
    }

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        super.initVariable(savedInstanceState);
        mInvs = new ArrayList<>();
        mLocationList = new ArrayList<>();
    }

    @Override
    protected void initView() {
        llLocationType.setVisibility(isOpenLocationType ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initEvent() {
        //扫描后者手动输入物资条码
        etMaterialNum.setOnRichEditTouchListener((view, materialNum) -> {
            //请求接口获取获取物料
            hideKeyboard(view);
            loadMaterialInfo(materialNum, getString(etBatchFlag));
        });

        //监听物料的改变，清除当前物料的批次状态(2017年修改，这里能够保证保存当前物料后，用户可以手动修改物料并且输入批次)
        RxTextView.textChanges(etMaterialNum)
                .filter(str -> !TextUtils.isEmpty(str))
                .subscribe(e -> {
                    isOpenBatchManager = true;
                    etBatchFlag.setEnabled(true);
                });


        //上架仓位,匹配缓存的历史仓位数量
        etLocation.setOnRichAutoEditTouchListener((view, location) -> {
            hideKeyboard(etLocation);
            if (cbSingle.isChecked())
                return;
            getTransferSingle(getString(etBatchFlag), location);
        });

        //监听库存地点，加载上架仓位
        RxAdapterView.itemSelections(spInv)
                .filter(pos -> pos > 0)
                .subscribe(pos -> {
                    if (isOpenLocationType) {
                        //如果打开
                        mPresenter.getDictionaryData("locationType");
                    } else {
                        loadLocationList(pos);
                    }
                });

        //增加仓储类型的选择获取提示库粗
        RxAdapterView.itemSelections(spLocationType)
                .filter(a -> isOpenLocationType && spLocationType.getAdapter() != null && mLocationTypes != null
                        && mLocationTypes.size() > 0)
                .subscribe(position -> loadLocationList(spInv.getSelectedItemPosition()));


        //监听上架仓位输入，及时清空仓位数量
        RxTextView.textChanges(etLocation)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> tvLocQuantity.setText(""));

        //选中上架仓位列表的item，关闭输入法,并且直接匹配出仓位数量
        RxAutoCompleteTextView.itemClickEvents(etLocation)
                .subscribe(a -> {
                    hideKeyboard(etLocation);
                    getTransferSingle(getString(etBatchFlag), getString(etLocation));
                });

        //点击自动提示控件，显示默认列表
        RxView.clicks(etLocation)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .filter(a -> mLocationList != null && mLocationList.size() > 0)
                .subscribe(a -> showAutoCompleteConfig(etLocation));
    }

    @Override
    protected void initDataLazily() {
        etMaterialNum.setEnabled(false);
        if (mRefData == null) {
            showMessage("请先在抬头界面输入必要的数据");
            return;
        }

        if (TextUtils.isEmpty(mRefData.moveType)) {
            showMessage("请先在抬头选择移动类型");
            return;
        }

        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先选择工厂");
            return;
        }

        etMaterialNum.setEnabled(true);
        etLocation.setEnabled(!isNLocation);
        //恢复批次
        isOpenBatchManager = true;
        etBatchFlag.setEnabled(true);
        //加载发出工厂下的发出库位
        mPresenter.getInvsByWorks(mRefData.workId, 0);
    }

    private void loadMaterialInfo(String materialNum, String batchFlag) {
        if (!etMaterialNum.isEnabled())
            return;
        if (TextUtils.isEmpty(materialNum)) {
            showMessage("物料编码为空,请重新输入");
            return;
        }
        clearAllUI();
        etMaterialNum.setText(materialNum);
        etBatchFlag.setText(batchFlag);
        mPresenter.getTransferSingleInfo(mRefData.bizType, materialNum,
                Global.USER_ID, mRefData.workId, mRefData.invId, mRefData.recWorkId,
                mRefData.recInvId, batchFlag, "", -1);
    }


    @Override
    public void bindCommonCollectUI(ReferenceEntity refData, String batchFlag) {
        RefDetailEntity data = refData.billDetailList.get(0);
        isOpenBatchManager = true;
        etBatchFlag.setEnabled(true);
        manageBatchFlagStatus(etBatchFlag, data.batchManagerStatus);
        //刷新UI
        etMaterialNum.setTag(data.materialId);
        tvMaterialDesc.setText(data.materialDesc);
        tvMaterialGroup.setText(data.materialGroup);
        tvMaterialUnit.setText(data.unit);
        tvSpecialInvFlag.setText(mRefData.specialInvFlag);
        etBatchFlag.setText(!TextUtils.isEmpty(data.batchFlag) ? data.batchFlag : batchFlag);
        mHistoryDetailList = refData.billDetailList;
    }

    @Override
    public void loadTransferSingleInfoFail(String message) {
        showMessage(message);
    }

    @Override
    public void showInvs(List<InvEntity> invs) {
        mInvs.clear();
        mInvs.addAll(invs);
        if (mInvAdapter == null) {
            mInvAdapter = new InvAdapter(mActivity, R.layout.item_simple_sp, mInvs);
            spInv.setAdapter(mInvAdapter);
        } else {
            mInvAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInvsFail(String message) {
        showMessage(message);
    }

    //position:为库存地点的位置
    @Override
    public void loadLocationList(int position) {
        if (position <= 0) {
            return;
        }
        tvLocQuantity.setText("");
        if (mLocAdapter != null) {
            mLocationList.clear();
            mLocAdapter.notifyDataSetChanged();
        }
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先输入工厂");
            spInv.setSelection(0);
            return;
        }

        if (etMaterialNum.getTag() == null) {
            showMessage("请先获取物料信息");
            spInv.setSelection(0);
            return;
        }

        final InvEntity invEntity = mInvs.get(position);
        InventoryQueryParam param = provideInventoryQueryParam();
        mPresenter.getInventoryInfo(param.queryType, mRefData.workId, invEntity.invId,
                mRefData.workCode, invEntity.invCode, "", getString(etMaterialNum),
                CommonUtil.Obj2String(etMaterialNum.getTag()), "",
                getString(etBatchFlag), "", "", param.invType, param.extraMap);
    }


    @Override
    public void loadInventoryFail(String message) {
        showMessage(message);
    }

    @Override
    public void showInventory(List<String> list) {
        mLocationList.clear();
        mLocationList.addAll(list);
        if (mLocAdapter == null) {
            mLocAdapter = new ArrayAdapter<>(mActivity,
                    android.R.layout.simple_dropdown_item_1line, mLocationList);
            etLocation.setAdapter(mLocAdapter);
            setAutoCompleteConfig(etLocation);
        } else {
            mLocAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInventoryComplete() {
    }

    /**
     * 匹配历史仓位数量
     */
    private void getTransferSingle(final String batchFlag, final String location) {

        if (mHistoryDetailList == null) {
            showMessage("请先获取物料信息");
            return;
        }

        //检验是否选择了库存地点
        if (spInv.getSelectedItemPosition() <= 0) {
            showMessage("请先选择库存地点");
            return;
        }

        //批次处理。打开了批次管理而且必须输入，那么检查是否输入了批次
        if (isOpenBatchManager && etBatchFlag.isEnabled()) {
            if (TextUtils.isEmpty(batchFlag)) {
                showMessage("请先输入批次");
                return;
            }
        }
        if (TextUtils.isEmpty(location) && !isNLocation) {
            showMessage("请先输入上架仓位");
            return;
        }

        if (!isNLocation) {
            String invId = mInvs.get(spInv.getSelectedItemPosition()).invId;
            //使用库存参数
            InventoryQueryParam queryParam = provideInventoryQueryParam();
            mPresenter.checkLocation(queryParam.queryType, mRefData.workId, invId, batchFlag, location, queryParam.extraMap);
        } else {
            //如果不上架，那么直接默认仓位检查通过
            checkLocationSuccess(batchFlag, location);
        }
    }

    @Override
    public void checkLocationSuccess(String batchFlag, String location) {
        isLocationChecked = true;
        //开始匹配缓存
        String locQuantity = "0";
        tvLocQuantity.setText(locQuantity);
        boolean isMatched = false;
        String locationType = "";
        if (isOpenLocationType) {
            locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
        }
        for (RefDetailEntity detail : mHistoryDetailList) {
            List<LocationInfoEntity> locationList = detail.locationList;
            if (locationList != null && locationList.size() > 0) {
                for (LocationInfoEntity locationInfo : locationList) {
                    if(isOpenLocationType) {
                        isMatched = isOpenBatchManager ? location.equalsIgnoreCase(locationInfo.location)
                                && batchFlag.equalsIgnoreCase(locationInfo.batchFlag) && locationType.equals(locationInfo.locationType) :
                                location.equalsIgnoreCase(locationInfo.location) && locationType.equals(locationInfo.locationType);
                    }else {
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
    public void checkLocationFail(String message) {
        showMessage(message);
        isLocationChecked = false;
    }


    protected void clearAllUI() {
        clearCommonUI(tvMaterialDesc, tvMaterialGroup, tvLocQuantity, tvLocQuantity, etQuantity, etLocation,
                etMaterialNum, etBatchFlag, tvSpecialInvFlag, tvMaterialUnit);
        //库存地点，注意这里不清除数据
        if (spInv.getAdapter() != null) {
            spInv.setSelection(0);
        }

        //上架仓位
        if (mLocAdapter != null) {
            mLocationList.clear();
            mLocAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {

        if (!etMaterialNum.isEnabled()) {
            showMessage("请先获取物料信息");
            return false;
        }

        if (mRefData == null) {
            showMessage("请先在抬头界面输入必要的数据");
            return false;
        }


        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先选择工厂");
            return false;
        }

        //库存地点
        if (spInv.getSelectedItemPosition() <= 0) {
            showMessage("请先选择库存地点");
            return false;
        }

        //上架仓位
        if (!isNLocation && TextUtils.isEmpty(getString(etLocation))) {
            showMessage("请先输入上架仓位");
            return false;
        }

        if (!isNLocation && !isLocationChecked) {
            showMessage("您输入的仓位不存在");
            return false;
        }

        //物资条码
        if (TextUtils.isEmpty(getString(etMaterialNum))) {
            showMessage("请先输入物料条码");
            return false;
        }
        //实发数量
        if (TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请先输入实收数量");
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
        result.invId = mInvs.get(spInv.getSelectedItemPosition()).invId;
        result.recWorkId = mRefData.recWorkId;
        result.recInvId = mRefData.recInvId;
        result.materialId = CommonUtil.Obj2String(etMaterialNum.getTag());
        result.batchFlag = !isOpenBatchManager ? Global.DEFAULT_BATCHFLAG : getString(etBatchFlag);
        result.location = !isNLocation ? getString(etLocation) : Global.DEFAULT_LOCATION;
        result.quantity = getString(etQuantity);
        result.specialInvFlag = getString(tvSpecialInvFlag);
        result.projectNum = mRefData.projectNum;
        result.supplierId = mRefData.supplierId;
        result.supplierNum = mRefData.supplierNum;
        result.invType = param.invType;
        //仓储类型
        if (isOpenLocationType)
            result.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
        result.modifyFlag = "N";
        return result;
    }

    @Override
    public void _onPause() {
        super._onPause();
        clearAllUI();
        clearCommonUI(etMaterialNum, etBatchFlag);
    }

    @Override
    public void saveCollectedDataSuccess(String message) {
        showMessage(message);
        if (!isNLocation)
            tvLocQuantity.setText(getString(etQuantity));
        if (!cbSingle.isChecked()) {
            etQuantity.setText("");
        }
    }

    @Override
    public void saveCollectedDataFail(String message) {
        showMessage("保存数据失败;" + message);
    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        //加载仓储类型完毕，初始化下拉
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
        }
    }

    /**
     * 默认增加仓储类型的维度
     *
     * @return
     */
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


}
