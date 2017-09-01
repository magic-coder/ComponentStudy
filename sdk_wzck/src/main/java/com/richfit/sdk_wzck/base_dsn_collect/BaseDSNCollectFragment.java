package com.richfit.sdk_wzck.base_dsn_collect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.lib_adapter.LocationAdapter;
import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectFragment;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzck.R;
import com.richfit.sdk_wzck.R2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by monday on 2017/2/23.
 */

public abstract class BaseDSNCollectFragment<P extends IDSNCollectPresenter> extends BaseCollectFragment<P>
        implements IDSNCollectView {

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
    @BindView(R2.id.sp_inv)
    protected Spinner spInv;
    @BindView(R2.id.sp_location)
    protected Spinner spLocation;
    @BindView(R2.id.tv_inv_quantity)
    TextView tvInvQuantity;
    @BindView(R2.id.tv_location_quantity)
    TextView tvLocQuantity;
    @BindView(R2.id.et_quantity)
    protected EditText etQuantity;
    @BindView(R2.id.cb_single)
    CheckBox cbSingle;

    /*库存地点*/
    InvAdapter mInvAdapter;
    protected List<InvEntity> mInvs;
    /*下架仓位*/
    protected List<InventoryEntity> mInventoryDatas;
    LocationAdapter mLocAdapter;
    /*缓存的历史仓位数量*/
    List<RefDetailEntity> mHistoryDetailList;

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
                loadLocationQuantity(spLocation.getSelectedItemPosition());
            } else {
                loadMaterialInfo(materialNum, batchFlag);
            }
        } else if (list != null && list.length == 1 && !cbSingle.isChecked()) {
            //仓位处理
            if (mInventoryDatas != null && spLocation.getAdapter() != null) {
                final String location = list[0];
                UiUtil.setSelectionForLocation(mInventoryDatas, location, spLocation);
            }
        }
    }


    @Override
    protected int getContentId() {
        return R.layout.wzck_fragment_base_dsn_collect;
    }


    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        mInvs = new ArrayList<>();
        mInventoryDatas = new ArrayList<>();
    }

    @Override
    public void initEvent() {
        /*扫描后者手动输入物资条码*/
        etMaterialNum.setOnRichEditTouchListener((view, materialNum) -> {
            //请求接口获取获取物料
            hideKeyboard(view);
            loadMaterialInfo(materialNum, getString(etBatchFlag));
        });

        //监听物料恢复批次状态
        RxTextView.textChanges(etMaterialNum)
                .filter(str -> !TextUtils.isEmpty(str))
                .subscribe(e -> {
                    isOpenBatchManager = true;
                    etBatchFlag.setEnabled(true);
                });

        /*监测批次修改，如果修改了批次那么需要重新刷新库存信息和用户已经输入的信息*/
        /**
         * debounce(400, TimeUnit.MILLISECONDS) 当没有数据传入达到400ms之后,才去发送数据
         * throttleFirst(400, TimeUnit.MILLISECONDS) 在每一个400ms内,如果有数据传入就发送.且每个400ms内只发送一次或零次数据.
         * 但是这是使用debonce，resetCommonUIPartly将延迟执行到发出库位显示默认位置之后，导致抬头界面的默认发出库位选择失效
         */
        RxTextView.textChanges(etBatchFlag)
                .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .filter(str -> !TextUtils.isEmpty(str))
                .subscribe(batch -> resetCommonUIPartly());

       /*库存地点。选择库存地点获取库存*/
        RxAdapterView.itemSelections(spInv)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(position -> loadInventoryInfo(position));

        /*选择发货仓位，查询历史仓位数量以及历史接收仓位*/
        RxAdapterView
                .itemSelections(spLocation)
                .filter(position -> (mInventoryDatas != null && mInventoryDatas.size() > 0 &&
                        position.intValue() < mInventoryDatas.size()))
                .subscribe(position -> loadLocationQuantity(position));

       /*单品(注意单品仅仅控制实收数量，累计数量是由行信息里面控制)*/
        cbSingle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etQuantity.setText(isChecked ? "1" : "");
            etQuantity.setEnabled(!isChecked);
        });
    }

    @Override
    public void initDataLazily() {
        //检查抬头界面的数据
        etMaterialNum.setEnabled(false);
        if (mRefData == null) {
            showMessage("请先在抬头界面选择工厂");
            return;
        }
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先在抬头界面选择工厂");
            return;
        }

        etMaterialNum.setEnabled(true);
        isOpenBatchManager = true;
        etBatchFlag.setEnabled(true);
    }


    private void loadMaterialInfo(String materialNum, String batchFlag) {
        if (TextUtils.isEmpty(materialNum)) {
            showMessage("物料编码为空,请重新输入");
            return;
        }
        clearAllUI();
        etMaterialNum.setText(materialNum);
        etBatchFlag.setText(batchFlag);
        mHistoryDetailList = null;
        mPresenter.getTransferInfoSingle(mRefData.bizType, materialNum,
                Global.USER_ID, mRefData.workId, mRefData.invId, mRefData.recWorkId,
                mRefData.recInvId, batchFlag, "", -1);
    }

    @Override
    public void bindCommonCollectUI(ReferenceEntity refData, String batchFlag) {
        isOpenBatchManager = true;
        etBatchFlag.setEnabled(true);
        RefDetailEntity data = refData.billDetailList.get(0);
        manageBatchFlagStatus(etBatchFlag, data.batchManagerStatus);
        //刷新UI
        etMaterialNum.setTag(data.materialId);
        tvMaterialDesc.setText(data.materialDesc);
        tvMaterialGroup.setText(data.materialGroup);
        tvMaterialUnit.setText(data.unit);
        //如果打开了批次管理，那么以当前输入的为准，如果没有那么获取单据中的批次
        if (isOpenBatchManager && TextUtils.isEmpty(getString(etBatchFlag))) {
            etBatchFlag.setText(data.batchFlag);
        }
        mHistoryDetailList = refData.billDetailList;
    }

    @Override
    public void loadTransferSingleInfoFail(String message) {
        showMessage(message);
    }

    @Override
    public void loadTransferSingleInfoComplete() {
        mPresenter.getInvsByWorks(mRefData.workId, 0);
    }

    /**
     * 加载发出库位成功
     *
     * @param invs
     */
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
        if (!TextUtils.isEmpty(mRefData.invCode)) {
            int position = -1;
            for (InvEntity entity : mInvs) {
                position++;
                if (mRefData.invCode.equalsIgnoreCase(entity.invCode)) {
                    break;
                }
            }
            spInv.setSelection(position);
        }
    }

    @Override
    public void loadInvsFail(String message) {
        showMessage(message);
    }

    /**
     * 获取库存信息。注意无参考使用的抬头的工厂信息
     */
    private void loadInventoryInfo(int position) {
        if (position <= 0) {
            return;
        }
        tvInvQuantity.setText("");
        tvLocQuantity.setText("");
        if (mLocAdapter != null) {
            mInventoryDatas.clear();
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
                getString(etBatchFlag), "", "", param.invType, "", param.extraMap);
    }

    /**
     * 加载库存成功
     */
    @Override
    public void showInventory(List<InventoryEntity> list) {
        mInventoryDatas.clear();
        InventoryEntity tmp = new InventoryEntity();
        tmp.locationCombine = "请选择";
        mInventoryDatas.add(tmp);
        mInventoryDatas.addAll(list);
        if (mLocAdapter == null) {
            mLocAdapter = new LocationAdapter(mActivity, R.layout.item_simple_sp, mInventoryDatas);
            spLocation.setAdapter(mLocAdapter);
        } else {
            mLocAdapter.notifyDataSetChanged();
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
        if (position <= 0) {
            resetSendLocation();
            return;
        }
        final String locationCombine = mInventoryDatas.get(position).locationCombine;
        final String invQuantity = mInventoryDatas.get(position).invQuantity;
        final String batchFlag = getString(etBatchFlag);

        if (isOpenBatchManager && TextUtils.isEmpty(batchFlag)) {
            showMessage("请输入发出批次");
            resetSendLocation();
            return;
        }
        if (TextUtils.isEmpty(locationCombine)) {
            showMessage("请输入发出仓位");
            resetSendLocation();
            return;
        }

        if (mHistoryDetailList == null) {
            showMessage("请先获取物料信息");
            resetSendLocation();
            return;
        }
        tvInvQuantity.setText(invQuantity);
        String locQuantity = "0";
        for (RefDetailEntity detail : mHistoryDetailList) {
            List<LocationInfoEntity> locationList = detail.locationList;
            if (locationList != null && locationList.size() > 0) {
                for (LocationInfoEntity locationInfo : locationList) {
                    final boolean isMatched = isOpenBatchManager ? locationCombine.equalsIgnoreCase(locationInfo.locationCombine)
                            && batchFlag.equalsIgnoreCase(locationInfo.batchFlag) :
                            locationCombine.equalsIgnoreCase(locationInfo.locationCombine);
                    if (isMatched) {
                        locQuantity = locationInfo.quantity;
                        break;
                    }
                }
            }
        }
        tvLocQuantity.setText(locQuantity);
    }

    private void resetSendLocation() {
        spLocation.setSelection(0, true);
        tvInvQuantity.setText("");
        tvLocQuantity.setText("");
    }

    private boolean refreshQuantity(final String quantity) {
        if (Float.valueOf(quantity) < 0.0f) {
            showMessage("输入数量不合理");
            return false;
        }
        //该仓位的历史出库数量
        //本次出库数量
        final float quantityV = CommonUtil.convertToFloat(quantity, 0.0f);
        final float historyQuantityV = CommonUtil.convertToFloat(getString(tvLocQuantity), 0.0f);
        //该仓位的库存数量
        final float inventoryQuantity = CommonUtil.convertToFloat(getString(tvInvQuantity), 0.0f);
        if (Float.compare(quantityV + historyQuantityV, inventoryQuantity) > 0.0f) {
            showMessage("输入实收数量有误，请出现输入");
            if (!cbSingle.isChecked())
                etQuantity.setText("");
            return false;
        }
        return true;
    }

    /**
     * 子类自己检查接收仓位和接收批次
     *
     * @return
     */
    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (!etMaterialNum.isEnabled()) {
            showMessage("请先获取物料信息");
            return false;
        }

        if (TextUtils.isEmpty(getString(etMaterialNum))) {
            showMessage("物料编码为空");
            return false;
        }

        if (spLocation.getSelectedItemPosition() <= 0) {
            showMessage("请先选择下架仓位");
            return false;
        }

        if (TextUtils.isEmpty(getString(tvInvQuantity))) {
            showMessage("库存数量为空");
            return false;
        }
        if (TextUtils.isEmpty(getString(tvLocQuantity))) {
            showMessage("仓位数量为空");
            return false;
        }
        //2017年06月28日增加批次判断
//        if(mHistoryDetailList != null) {
//            RefDetailEntity data = mHistoryDetailList.get(0);
//            manageBatchFlagStatus(etBatchFlag, data.batchManagerStatus);
//            if(isOpenBatchManager && TextUtils.isEmpty(getString(etBatchFlag))) {
//                showMessage("请输入批次");
//                return false;
//            }
//        }

        //实发数量
        if (!refreshQuantity(getString(etQuantity))) {
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
        result.materialId = etMaterialNum.getTag().toString();
        result.batchFlag = !isOpenBatchManager ? Global.DEFAULT_BATCHFLAG : getString(etBatchFlag);
        result.location = mInventoryDatas.get(spLocation.getSelectedItemPosition()).location;
        result.quantity = getString(etQuantity);
        result.costCenter = mRefData.costCenter;
        result.projectNum = mRefData.projectNum;
        //库存相关的字段回传
        int locationPos = spLocation.getSelectedItemPosition();
        result.location = mInventoryDatas.get(locationPos).location;
        result.specialInvFlag = mInventoryDatas.get(locationPos).specialInvFlag;
        result.specialInvNum = mInventoryDatas.get(locationPos).specialInvNum;
        result.specialConvert = (!TextUtils.isEmpty(result.specialInvFlag) && "k".equalsIgnoreCase(result.specialInvFlag)
                && !TextUtils.isEmpty(result.specialInvNum)) ?
                "Y" : "N";
        result.invType = param.invType;
        result.invFlag = mRefData.invFlag;
        result.modifyFlag = "N";
        return result;
    }

    @Override
    public void saveCollectedDataSuccess(String message) {
        showMessage(message);
        final float quantityV = CommonUtil.convertToFloat(getString(etQuantity), 0.0f);
        final float locQuantityV = CommonUtil.convertToFloat(getString(tvLocQuantity), 0.0f);
        tvLocQuantity.setText(String.valueOf(quantityV + locQuantityV));
        if (!cbSingle.isChecked()) {
            etQuantity.setText("");
        }
    }

    @Override
    public void saveCollectedDataFail(String message) {
        showMessage(message);
    }

    private void clearAllUI() {
        clearCommonUI(tvMaterialDesc, tvMaterialGroup, tvInvQuantity, tvLocQuantity,
                etQuantity, etMaterialNum, etBatchFlag, tvMaterialUnit);

        //库存地点
        if (spInv.getAdapter() != null) {
            spInv.setSelection(0);
        }

        //下架仓位
        if (spLocation.getAdapter() != null) {
            mInventoryDatas.clear();
            mLocAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 用户修改批次后重置部分UI
     */
    protected void resetCommonUIPartly() {
        //如果没有打开批次，那么用户不能输入批次，这里再次拦截
        if (!isOpenBatchManager)
            return;
        //库存地点
        if (spInv.getAdapter() != null) {
            spInv.setSelection(0);
        }
        //下架仓位
        if (mInventoryDatas != null && mInventoryDatas.size() > 0 &&
                spLocation.getAdapter() != null) {
            mInventoryDatas.clear();
            mLocAdapter.notifyDataSetChanged();
        }
        //库存数量
        tvInvQuantity.setText("");
        //历史仓位数量
        tvLocQuantity.setText("");
        //实收数量
        etQuantity.setText("");

    }

    @Override
    public void _onPause() {
        super._onPause();
        clearAllUI();
    }

    @Override
    public void networkConnectError(String retryAction) {

    }
}
