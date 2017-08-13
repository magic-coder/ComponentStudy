package com.richfit.sdk_wzrk.base_asn_collect;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
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
import com.richfit.sdk_wzrk.R;
import com.richfit.sdk_wzrk.R2;

import java.util.ArrayList;
import java.util.List;
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

    private InvAdapter mInvAdapter;
    protected List<InvEntity> mInvs;
    private List<RefDetailEntity> mHistoryDetailList;
    /*上架仓位列表适配器*/
    ArrayAdapter<String> mLocationAdapter;
    List<String> mLocationList;
    /*是否发不上架*/
    protected boolean isNLocation = false;

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
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.wzrk_fragment_asn_collect;
    }

    @Override
    public void initVariable(Bundle savedInstanceState) {
        mInvs = new ArrayList<>();
        mLocationList = new ArrayList<>();
    }

    @Override
    public void initEvent() {
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
                .subscribe(pos -> loadLocationList(getString(etLocation), false));

        //监听上架仓位输入，及时清空仓位数量
        RxTextView.textChanges(etLocation)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(a -> tvLocQuantity.setText(""));

        //监听输入的关键字
        RxTextView.textChanges(etLocation)
                .debounce(100, TimeUnit.MILLISECONDS)
                .filter(str -> !TextUtils.isEmpty(str) && mLocationList != null &&
                        mLocationList.size() > 0 && !filterKeyWord(str.toString()))
                .subscribe(a -> loadLocationList(getString(etLocation), true));

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
    public void initDataLazily() {
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
    public void loadLocationList(String keyWord, boolean isDropDown) {
        InvEntity invEntity = mInvs.get(spInv.getSelectedItemPosition());
        mPresenter.getLocationList(mRefData.workId, mRefData.workCode, invEntity.invId,
                invEntity.invCode, keyWord, 100, getInteger(R.integer.orgNorm), isDropDown);
    }

    /**
     * 如果用户输入的关键字在mLocationList存在，那么不在进行数据查询.
     *
     * @param keyWord
     * @return
     */
    private boolean filterKeyWord(String keyWord) {
        Pattern pattern = Pattern.compile("^" + keyWord);
        for (String item : mLocationList) {
            Matcher matcher = pattern.matcher(item);
            while (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void getLocationListFail(String message) {
        showMessage(message);
    }

    @Override
    public void getLocationListSuccess(List<String> list, boolean isDropDown) {
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
        if (isDropDown) {
            showAutoCompleteConfig(etLocation);
        }
    }


    /**
     * 匹配历史仓位数量
     */
    private void getTransferSingle(final String batchFlag, final String location) {


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

    protected void clearAllUI() {
        clearCommonUI(tvMaterialDesc, tvMaterialGroup, tvLocQuantity, tvLocQuantity, etQuantity, etLocation,
                etMaterialNum, etBatchFlag, tvSpecialInvFlag, tvMaterialUnit);
        //库存地点，注意这里不清除数据
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
        //物资条码
        if (TextUtils.isEmpty(getString(etMaterialNum))) {
            showMessage("请先输入物料条码");
            return false;
        }
        //批次.2017年06月28日增加在每一次保存数据的前，重新再次判断是否打开了批次管理
//        if (mHistoryDetailList != null) {
//            manageBatchFlagStatus(etBatchFlag, mHistoryDetailList.get(0).batchManagerStatus);
//            if (isOpenBatchManager && TextUtils.isEmpty(getString(etBatchFlag))) {
//                showMessage("请先输入批次");
//                return false;
//            }
//        }
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
        result.invType = param.invType;
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

}
