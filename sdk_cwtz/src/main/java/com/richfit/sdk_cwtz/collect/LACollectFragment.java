package com.richfit.sdk_cwtz.collect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectFragment;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_cwtz.R;
import com.richfit.sdk_cwtz.R2;
import com.richfit.sdk_cwtz.adapter.SpecialInvAdapter;
import com.richfit.sdk_cwtz.collect.imp.LACollectPresenterImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


/**
 * 注意在获取库存的时候，如果启用了WM那么使用04从SAP获取有效库存，
 * 如果没有启用WM，那么使用03获取有效库存
 * Created by monday on 2017/2/7.
 */

public class LACollectFragment extends BaseCollectFragment<LACollectPresenterImp>
        implements ILACollectView {

    @BindView(R2.id.et_material_num)
    protected RichEditText etMaterialNum;
    @BindView(R2.id.tv_material_desc)
    TextView tvMaterialDesc;
    @BindView(R2.id.tv_material_group)
    TextView tvMaterialGroup;
    @BindView(R2.id.tv_material_unit)
    TextView tvMaterialUnit;
    @BindView(R2.id.et_batch_flag)
    protected EditText etBatchFlag;
    @BindView(R2.id.et_send_location)
    protected RichEditText etSendLocation;
    @BindView(R2.id.tv_send_inv_quantity)
    protected TextView tvSendInvQuantity;
    @BindView(R2.id.et_rec_location)
    protected EditText etRecLocation;
    @BindView(R2.id.et_adjust_quantity)
    protected EditText etRecQuantity;
    //增加特殊库存
    @BindView(R2.id.sp_special_inv)
    protected Spinner spSpecialInv;
    //增加源仓位仓储类型
    @BindView(R2.id.ll_location_type)
    protected LinearLayout llLocationType;
    //增加目标仓位仓储类型
    @BindView(R2.id.ll_rec_location_type)
    protected LinearLayout llRecLocationType;
    @BindView(R2.id.sp_location_type)
    Spinner spLocationType;
    @BindView(R2.id.sp_rec_location_type)
    Spinner spRecLocationType;

    /*仓储类型*/
    protected List<SimpleEntity> mLocationTypes;
    protected List<SimpleEntity> mRecLocationTypes;
    /*是否启用仓储类型*/
    private boolean isOpenLocationType = false;
    protected SpecialInvAdapter mAdapter;
    protected List<InventoryEntity> mInventoryDatas;
    MaterialEntity mHistoryData;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type, list);
        if (list != null && list.length > 2) {
            final String materialNum = list[Global.MATERIAL_POS];
            final String batchFlag = list[Global.BATCHFALG_POS];
            loadMaterialInfo(materialNum, batchFlag);
        } else if (list != null && list.length == 1) {
            final String location = list[Global.LOCATION_POS];
            //目标仓位
            if (etRecLocation.hasFocus() && etRecLocation.isFocused()) {
                clearCommonUI(etRecLocation);
                etRecLocation.setText(location);
            } else {
                //源仓位
                clearCommonUI(etSendLocation);
                etSendLocation.setText(location);
                //获取库存
                loadInventoryInfo(location);
            }
        } else if (list != null && list.length == 2 && isOpenLocationType) {
            String location = list[Global.LOCATION_POS];
            String locationType = list[Global.LOCATION_TYPE_POS];
            //目标仓位
            if (etRecLocation.hasFocus() && etRecLocation.isFocused()) {
                clearCommonUI(etRecLocation);
                etRecLocation.setText(location);
            } else {
                //源仓位
                clearCommonUI(etSendLocation);
                etSendLocation.setText(location);
                //自动选择仓储类型
                UiUtil.setSelectionForSimpleSp(mLocationTypes, locationType, spLocationType);
                //加载库存
                loadInventoryInfo(location);
            }
        }
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected int getContentId() {
        return R.layout.cwtz_fragment_la_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new LACollectPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {
        etMaterialNum.setEnabled(false);
        if (mRefData == null) {
            showMessage("在先在抬头界面选择相关的信息");
            return;
        }

        if (TextUtils.isEmpty(mRefData.workCode)) {
            showMessage("请现在抬头界面选择工厂");
            return;
        }

        if (TextUtils.isEmpty(mRefData.invCode)) {
            showMessage("请先在抬头界面选择库存地点");
            return;
        }
        if ("Y".equalsIgnoreCase(Global.WMFLAG) && TextUtils.isEmpty(mRefData.storageNum)) {
            showMessage("未获取到仓位号,请重新在太头界面合适的工厂和库存地点");
            return;
        }
        etMaterialNum.setEnabled(true);
        isOpenBatchManager = true;
        etBatchFlag.setEnabled(true);
    }


    @Override
    public void initEvent() {

        //获取物料
        etMaterialNum.setOnRichEditTouchListener((view, materialNum) -> loadMaterialInfo(materialNum, getString(etBatchFlag)));

        //监听物料恢复批次状态
        RxTextView.textChanges(etMaterialNum)
                .filter(str -> !TextUtils.isEmpty(str))
                .subscribe(e -> {
                    isOpenBatchManager = true;
                    etBatchFlag.setEnabled(true);
                });

        //获取仓位的库存
        etSendLocation.setOnRichEditTouchListener((view, location) -> loadInventoryInfo(location));

        //选择下拉
        RxAdapterView.itemSelections(spSpecialInv)
                .filter(pos -> pos > 0)
                .subscribe(pos -> tvSendInvQuantity.setText(mInventoryDatas.get(pos).invQuantity));

        //增加仓储类型的选择获取提示库粗
        RxAdapterView.itemSelections(spLocationType)
                .filter(a -> mInventoryDatas != null && mInventoryDatas.size() > 0 && mAdapter != null)
                .subscribe(position -> {
                    //清除缓存
                    mInventoryDatas.clear();
                    mAdapter.notifyDataSetChanged();
                    tvSendInvQuantity.setText("");
                });
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {
        isOpenLocationType = llLocationType.getVisibility() != View.GONE;
        if (isOpenLocationType)
            mPresenter.getDictionaryData("locationType");
    }

    /**
     * 获取物料信息入口
     *
     * @param materialNum
     * @param batchFlag
     */
    private void loadMaterialInfo(String materialNum, String batchFlag) {
        if (TextUtils.isEmpty(materialNum)) {
            showMessage("物料编码为空,请重新输入");
            return;
        }
        clearAllUI();
        etMaterialNum.setText(materialNum);
        etBatchFlag.setText(batchFlag);
        mHistoryData = null;
        isOpenBatchManager = true;
        etBatchFlag.setEnabled(true);
        mPresenter.getMaterialInfo("01", materialNum, mRefData.workId);
    }

    @Override
    public void getMaterialInfoSuccess(MaterialEntity data) {
        etMaterialNum.setTag(data.id);
        tvMaterialDesc.setText(data.materialDesc);
        tvMaterialGroup.setText(data.materialGroup);
        tvMaterialUnit.setText(data.unit);
        manageBatchFlagStatus(etBatchFlag, data.batchManagerStatus);
        if (isOpenBatchManager && TextUtils.isEmpty(getString(etBatchFlag))) {
            etBatchFlag.setText(data.batchFlag);
        }
        mHistoryData = data;
    }

    @Override
    public void getMaterialInfoFail(String message) {
        showMessage(message);
    }

    /**
     * 获取库存信息
     *
     * @param location
     */
    protected void loadInventoryInfo(String location) {
        //清除
        if (mAdapter != null) {
            mInventoryDatas.clear();
            mAdapter.notifyDataSetChanged();
        }
        tvSendInvQuantity.setText("");

        Object tag = etMaterialNum.getTag();
        if (tag == null || TextUtils.isEmpty(tag.toString())) {
            showMessage("请先获取物料信息");
            return;
        }

        if (TextUtils.isEmpty(location)) {
            showMessage("先输入目标仓位");
            return;
        }
        InventoryQueryParam param = provideInventoryQueryParam();
        mPresenter.getInventoryInfo(param.queryType, mRefData.workId, mRefData.invId, mRefData.workCode,
                mRefData.invCode, mRefData.storageNum, getString(etMaterialNum), tag.toString(),
                "", "", getString(etBatchFlag), location, "", "", param.invType, "", param.extraMap);
    }

    @Override
    public void getInventorySuccess(List<InventoryEntity> invs) {
        if (mInventoryDatas == null) {
            mInventoryDatas = new ArrayList<>();
        }
        mInventoryDatas.clear();
        InventoryEntity tmp = new InventoryEntity();
        tmp.location = "请选择";
        mInventoryDatas.add(tmp);
        mInventoryDatas.addAll(invs);
        //初始化特殊库存标识下拉列表
        if (mAdapter == null) {
            mAdapter = new SpecialInvAdapter(mActivity, R.layout.item_simple_sp, mInventoryDatas);
            spSpecialInv.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getInventoryFail(String message) {
        showMessage(message);
    }

    @Override
    public void loadDictionaryDataFail(String message) {
        showMessage(message);
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

    protected boolean refreshQuantity(final String quantity) {
        if (Float.valueOf(quantity) <= 0.0f) {
            showMessage("输入数量不合理");
            return false;
        }
        //发出仓位的库存
        final float sendInvQuantityV = CommonUtil.convertToFloat(getString(tvSendInvQuantity), 0.0f);
        final float quantityV = CommonUtil.convertToFloat(quantity, 0.0f);
        if (Float.compare(quantityV, sendInvQuantityV) > 0.0f) {
            showMessage("输入数量有误，请重新输入");
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

        if (TextUtils.isEmpty(mRefData.bizType)) {
            showMessage("业务类型为空");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先在抬头界面选择工厂");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.invId)) {
            showMessage("请先在抬头界面选择库存地点");
            return false;
        }

        if (spSpecialInv.getSelectedItemPosition() <= 0) {
            showMessage("请先选择特殊库存标识");
            return false;
        }

        Object tag = etMaterialNum.getTag();
        if (tag == null || TextUtils.isEmpty(tag.toString())) {
            showMessage("请先获取物料信息");
            return false;
        }

        final String location = getString(etSendLocation);
        if (TextUtils.isEmpty(location) || location.length() > 10) {
            showMessage("源仓位不合理");
            return false;
        }

        final String recLocation = getString(etRecLocation);
        if (TextUtils.isEmpty(recLocation) || recLocation.length() > 10) {
            showMessage("目标仓位不合理");
            return false;
        }

        if (TextUtils.isEmpty(getString(tvSendInvQuantity))) {
            showMessage("请先获取有效库存");
            return false;
        }

        //必须先判断调整数据是否输入
        if (TextUtils.isEmpty(getString(etRecQuantity))) {
            showMessage("调整数量有误");
            return false;
        }

        if (!refreshQuantity(getString(etRecQuantity))) {
            showMessage("调整数量有误");
            return false;
        }
        return true;
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = new ResultEntity();
        result.businessType = mRefData.bizType;
        int position = spSpecialInv.getSelectedItemPosition();
        //如果没有打开，那么返回服务给出的默认批次
        result.batchFlag = !isOpenBatchManager ? CommonUtil.toUpperCase(mInventoryDatas.get(position).batchFlag)
                : CommonUtil.toUpperCase(getString(etBatchFlag));
        result.workId = mRefData.workId;
        result.invId = mRefData.invId;
        result.materialId = CommonUtil.Obj2String(etMaterialNum.getTag());
        result.location = CommonUtil.toUpperCase(getString(etSendLocation));
        result.recLocation = CommonUtil.toUpperCase(getString(etRecLocation));
        result.quantity = getString(etRecQuantity);
        result.userId = Global.USER_ID;
        result.invType = mInventoryDatas.get(position).invType;
        result.invFlag = mInventoryDatas.get(position).invFlag;
        result.specialInvFlag = mInventoryDatas.get(position).specialInvFlag;
        result.specialInvNum = mInventoryDatas.get(position).specialInvNum;
        if (isOpenLocationType) {
            result.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
            result.recLocationType = mRecLocationTypes.get(spRecLocationType.getSelectedItemPosition()).code;
        }
        return result;
    }

    @Override
    public void saveCollectedDataSuccess(String message) {
        showSuccessDialog(message);
        clearAllUI();
        clearCommonUI(etMaterialNum, etBatchFlag);
    }

    @Override
    public void saveCollectedDataFail(String message) {
        showErrorDialog(message);
    }

    private void clearAllUI() {
        clearCommonUI(tvMaterialDesc, tvMaterialGroup, tvMaterialUnit, etSendLocation,
                tvSendInvQuantity, etRecLocation, etRecQuantity);
        if (mAdapter != null) {
            mInventoryDatas.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void retry(String action) {
        switch (action) {
            case Global.RETRY_QUERY_MATERIAL_INFO:
                loadMaterialInfo(getString(etMaterialNum), getString(etBatchFlag));
                break;
            case Global.RETRY_LOAD_INVENTORY_ACTION:
                loadInventoryInfo(getString(etRecLocation));
                break;
        }
        super.retry(action);
    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {

        List<SimpleEntity> locationTypes = data.get("locationType");
        if (locationTypes != null) {
            if (mLocationTypes == null) {
                mLocationTypes = new ArrayList<>();
            }
            if (mRecLocationTypes == null) {
                mRecLocationTypes = new ArrayList<>();
            }
            mLocationTypes.clear();
            mRecLocationTypes.clear();
            mLocationTypes.addAll(locationTypes);
            mRecLocationTypes.addAll(locationTypes);
            //发出仓储类型
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp,
                    mLocationTypes, false);
            spLocationType.setAdapter(adapter);

            //接收仓储类型
            SimpleAdapter recAdapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp,
                    mRecLocationTypes, false);
            spRecLocationType.setAdapter(recAdapter);
        }
    }


    @Override
    public void _onPause() {
        super._onPause();
        clearAllUI();
        clearCommonUI(etMaterialNum, etBatchFlag);
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
}
