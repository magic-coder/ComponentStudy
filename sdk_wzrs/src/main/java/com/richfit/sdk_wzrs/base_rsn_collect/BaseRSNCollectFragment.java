package com.richfit.sdk_wzrs.base_rsn_collect;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxAutoCompleteTextView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.widget.RichAutoEditText;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzrs.R;
import com.richfit.sdk_wzrs.R2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by monday on 2017/3/2.
 */

public abstract class BaseRSNCollectFragment<P extends IRSNCollectPresenter> extends BaseFragment<P>
        implements IRSNCollectView {


    @BindView(R2.id.et_material_num)
    RichEditText etMaterialNum;
    @BindView(R2.id.tv_material_desc)
    TextView tvMaterialDesc;
    @BindView(R2.id.tv_material_group)
    TextView tvMaterialGroup;
    @BindView(R2.id.tv_material_unit)
    TextView tvMaterialUnit;
    @BindView(R2.id.tv_special_inv_flag)
    TextView tvSpecialInvFlag;
    @BindView(R2.id.et_batch_flag)
    EditText etBatchFlag;
    @BindView(R2.id.sp_inv)
    Spinner spInv;
    @BindView(R2.id.et_location)
    RichAutoEditText etLocation;
    @BindView(R2.id.tv_location_quantity)
    TextView tvLocQuantity;
    @BindView(R2.id.et_quantity)
    EditText etQuantity;
    @BindView(R2.id.cb_single)
    CheckBox cbSingle;

    //库存地点
    private InvAdapter mInvAdapter;
    private List<InvEntity> mInvs;
    /*缓存的历史仓位数量*/
    private List<RefDetailEntity> mHistoryDetailList;
    /*上架仓位列表适配器*/
    ArrayAdapter<String> mLocationAdapter;
    List<String> mLocationList;

    /**
     * 处理扫描
     *
     * @param type
     * @param list
     */
    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
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
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.wzrs_fragment_rsn_collect;
    }

    @Override
    public void initVariable(Bundle savedInstanceState) {
        mInvs = new ArrayList<>();
        mLocationList = new ArrayList<>();
    }

    @Override
    public void initEvent() {
         /*扫描后者手动输入物资条码*/
        etMaterialNum.setOnRichEditTouchListener((view, materialNum) -> {
            //请求接口获取获取物料
            hideKeyboard(view);
            loadMaterialInfo(materialNum, getString(etBatchFlag));
        });

        //对于质检物资(不上架)通过库存地点来获取缓存，如果需要上架选择库存地点获取上架仓位列表
        RxAdapterView.itemSelections(spInv)
                .filter(pos -> pos > 0)
                .subscribe(pos -> loadLocationList(false));

        //上架仓位,匹配缓存的历史仓位数量
        etLocation.setOnRichAutoEditTouchListener((view, location) -> {
            hideKeyboard(etLocation);
            if (cbSingle.isChecked())
                return;
            matchLocationQuantity(getString(etBatchFlag), getString(etLocation));
        });

        //监听上架仓位时时变化
        RxTextView.textChanges(etLocation)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> tvLocQuantity.setText(""));


        //选中上架仓位列表的item，关闭输入法,并且直接匹配出仓位数量
        //注意这里由于不在请求接口，所以先执行了，然后执行了上面的监听
        RxAutoCompleteTextView.itemClickEvents(etLocation)
                .delay(100,TimeUnit.MILLISECONDS)
                .subscribe(a -> {
                    Log.d("yff", "选择了上架仓位");
                    hideKeyboard(etLocation);
                    matchLocationQuantity(getString(etBatchFlag), getString(etLocation));
                });

        //点击自动提示控件，显示默认列表
        RxView.clicks(etLocation)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .filter(a -> mLocationList != null && mLocationList.size() > 0)
                .subscribe(a -> showAutoCompleteConfig(etLocation));
    }

    @Override
    public void initDataLazily() {
        etMaterialNum.setEnabled(false);
        if (mRefData == null) {
            showMessage("请先在抬头界面选择工厂");
            return;
        }
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先在抬头界面选择工厂");
            return;
        }
        if ("46".equals(mBizType) && TextUtils.isEmpty(mRefData.costCenter)) {
            showMessage("请先在抬头界面输入成本中心");
            return;
        }
        if ("47".equals(mBizType) && TextUtils.isEmpty(mRefData.projectNum)) {
            showMessage("请现在抬头界面输入项目编号");
            return;
        }
        etMaterialNum.setEnabled(true);
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


    @Override
    public void onBindCommonUI(ReferenceEntity refData, String batchFlag) {
        RefDetailEntity data = refData.billDetailList.get(0);
        isOpenBatchManager = true;
        etBatchFlag.setEnabled(true);
        manageBatchFlagStatus(etBatchFlag, data.batchManagerStatus);
        //刷新UI
        etMaterialNum.setTag(data.materialId);
        tvMaterialDesc.setText(data.materialDesc);
        tvMaterialGroup.setText(data.materialGroup);
        tvMaterialUnit.setText(data.unit);
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
    public void loadLocationList(boolean isDropDown) {
        if (mHistoryDetailList != null && mHistoryDetailList.size() > 0) {
            RefDetailEntity lineData = mHistoryDetailList.get(0);
            InvEntity invEntity = mInvs.get(spInv.getSelectedItemPosition());
            mPresenter.getInventoryInfo(getInventoryQueryType(), mRefData.workId,
                    invEntity.invId, mRefData.workCode, invEntity.invCode, "", getString(etMaterialNum),
                    lineData.materialId, "", getString(etBatchFlag), "", "", getInvType(), "", isDropDown);
        }
    }


    @Override
    public void loadInventoryFail(String message) {
        showMessage(message);
        if (mLocationAdapter != null) {
            mLocationList.clear();
            etLocation.setAdapter(null);
        }
    }

    @Override
    public void showInventory(List<String> list) {
        mLocationList.clear();
        mLocationList.addAll(list);
        if (mLocationAdapter == null) {
            mLocationAdapter = new ArrayAdapter<>(mActivity,
                    android.R.layout.simple_dropdown_item_1line, mLocationList);
            etLocation.setAdapter(mLocationAdapter);
            setAutoCompleteConfig(etLocation);
        } else {
            mLocationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInventoryComplete(boolean isDropDown) {
        if (isDropDown) {
            showAutoCompleteConfig(etLocation);
        }
    }


    /**
     * 匹配历史仓位数量
     */
    private void matchLocationQuantity(final String batchFlag, final String location) {

        if (isOpenBatchManager && TextUtils.isEmpty(batchFlag)) {
            showMessage("批次为空");
            return;
        }

        if (TextUtils.isEmpty(location)) {
            showMessage("请先输入上架仓位");
            return;
        }

        if (mHistoryDetailList == null) {
            showMessage("请先获取物料信息");
            return;
        }

        String locQuantity = "0";
        tvLocQuantity.setText(locQuantity);

        for (RefDetailEntity detail : mHistoryDetailList) {
            List<LocationInfoEntity> locationList = detail.locationList;
            if (locationList != null && locationList.size() > 0) {
                for (LocationInfoEntity locationInfo : locationList) {
                    final boolean isMatched = isOpenBatchManager ? location.equalsIgnoreCase(locationInfo.location)
                            && batchFlag.equalsIgnoreCase(locationInfo.batchFlag) :
                            location.equalsIgnoreCase(locationInfo.location);
                    if (isMatched) {
                        locQuantity = locationInfo.quantity;
                        break;
                    }
                }
            }
        }
        tvLocQuantity.setText(locQuantity);
    }

    private void clearAllUI() {
        clearCommonUI(tvMaterialDesc, tvMaterialGroup, tvMaterialUnit, tvSpecialInvFlag,
                tvLocQuantity, tvLocQuantity, etQuantity, etLocation, etMaterialNum, etBatchFlag);
        //库存地点
        if (spInv.getAdapter() != null) {
            spInv.setSelection(0);
        }

        //上架仓位
        if (mLocationAdapter != null) {
            mLocationList.clear();
            mLocationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        //库存地点
        if (spInv.getSelectedItemPosition() <= 0) {
            showMessage("请先选择库存地点");
            return false;
        }

        //上架仓位
        if (TextUtils.isEmpty(getString(etLocation))) {
            showMessage("请先输入上架仓位");
            return false;
        }
        //物资条码
        if (TextUtils.isEmpty(getString(etMaterialNum))) {
            showMessage("请先输入物料条码");
            return false;
        }
        //批次
        if (isOpenBatchManager)
            if (TextUtils.isEmpty(getString(etBatchFlag))) {
                showMessage("请先输入批次");
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

    public void saveCollectedData() {
        if (!checkCollectedDataBeforeSave()) {
            return;
        }
        Flowable.create((FlowableOnSubscribe<ResultEntity>) emitter -> {
            ResultEntity result = new ResultEntity();
            result.businessType = mRefData.bizType;
            result.voucherDate = mRefData.voucherDate;
            result.moveType = mRefData.moveType;
            result.userId = Global.USER_ID;
            result.workId = mRefData.workId;
            result.invId = mInvs.get(spInv.getSelectedItemPosition()).invId;
            result.recWorkId = mRefData.recWorkId;
            result.recInvId = mRefData.recInvId;
            result.materialId = etMaterialNum.getTag().toString();
            result.batchFlag = getString(etBatchFlag);
            result.location = getString(etLocation);
            result.quantity = getString(etQuantity);
            result.specialInvFlag = getString(tvSpecialInvFlag);
            result.supplierId = mRefData.supplierId;
            result.costCenter = mRefData.costCenter;
            result.projectNum = mRefData.projectNum;
            result.invType = getInventoryQueryType();
            result.modifyFlag = "N";
            emitter.onNext(result);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER)
                .compose(TransformerHelper.io2main())
                .subscribe(result -> mPresenter.uploadCollectionDataSingle(result));

    }

    @Override
    public void _onPause() {
        super._onPause();
        clearAllUI();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationAdapter = null;
        mLocationList.clear();
    }

    @Override
    public void saveCollectedDataSuccess() {
        showMessage("保存成功");
        tvLocQuantity.setText(getString(etQuantity));
        if (!cbSingle.isChecked()) {
            etQuantity.setText("");
            isOpenBatchManager = true;
            etBatchFlag.setEnabled(true);
        }
    }

    @Override
    public void saveCollectedDataFail(String message) {
        showMessage("保存数据失败;" + message);
    }

    protected abstract String getInvType();

    protected abstract String getInventoryQueryType();

}
