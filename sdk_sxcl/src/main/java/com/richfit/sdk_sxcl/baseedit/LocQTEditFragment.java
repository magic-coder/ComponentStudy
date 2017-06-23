package com.richfit.sdk_sxcl.baseedit;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.LocationAdapter;
import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditFragment;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_sxcl.R;
import com.richfit.sdk_sxcl.R2;
import com.richfit.sdk_sxcl.baseedit.imp.LocQTEditPresenterImp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by monday on 2017/5/26.
 */

public class LocQTEditFragment extends BaseEditFragment<LocQTEditPresenterImp>
        implements ILocQTEditView {

    @BindView(R2.id.tv_ref_line_num)
    TextView tvRefLineNum;
    @BindView(R2.id.tv_material_num)
    TextView tvMaterialNum;
    @BindView(R2.id.tv_material_desc)
    TextView tvMaterialDesc;
    @BindView(R2.id.tv_batch_flag)
    TextView tvBatchFlag;
    @BindView(R2.id.tv_work)
    TextView tvWork;
    @BindView(R2.id.tv_inv)
    TextView tvInv;
    @BindView(R2.id.tv_act_quantity)
    TextView tvActQuantity;
    @BindView(R2.id.sp_x_loc)
    Spinner spXLoc;
    @BindView(R2.id.tv_inv_quantity)
    TextView tvInvQuantity;
    @BindView(R2.id.et_s_location)
    EditText etSLocation;
    @BindView(R2.id.tv_location_quantity)
    TextView tvLocQuantity;
    @BindView(R2.id.et_quantity)
    EditText etQuantity;
    @BindView(R2.id.tv_total_quantity)
    TextView tvTotalQuantity;


    protected String mRefLineId;
    protected String mLocationId;
    protected int mPosition;
    //该子节点修改前的出库数量
    protected String mQuantity;
    protected List<InventoryEntity> mInventoryDatas;
    private LocationAdapter mXLocationAdapter;
    private List<String> mLocations;
    protected String mSelectedLocation;
    private String mSpecialInvFlag;
    private String mSpecialInvNum;
    protected float mTotalQuantity;


    @Override
    protected int getContentId() {
        return R.layout.sxcl_fragment_locqt_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new LocQTEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        mInventoryDatas = new ArrayList<>();
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initEvent() {
        //选择下架仓位，刷新库存数量并且请求缓存，注意缓存是用来刷新仓位数量和累计数量
        RxAdapterView
                .itemSelections(spXLoc)
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

    @Override
    public void initDataLazily() {

    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        mSelectedLocation = bundle.getString(Global.EXTRA_LOCATION_KEY);
        mSpecialInvFlag = bundle.getString(Global.EXTRA_SPECIAL_INV_FLAG_KEY);
        mSpecialInvNum = bundle.getString(Global.EXTRA_SPECIAL_INV_NUM_KEY);
        final String totalQuantity = bundle.getString(Global.EXTRA_TOTAL_QUANTITY_KEY);
        final String batchFlag = bundle.getString(Global.EXTRA_BATCH_FLAG_KEY);
        final String invId = bundle.getString(Global.EXTRA_INV_ID_KEY);
        final String invCode = bundle.getString(Global.EXTRA_INV_CODE_KEY);
        mPosition = bundle.getInt(Global.EXTRA_POSITION_KEY);
        mQuantity = bundle.getString(Global.EXTRA_QUANTITY_KEY);
        mLocations = bundle.getStringArrayList(Global.EXTRA_LOCATION_LIST_KEY);
        mRefLineId = bundle.getString(Global.EXTRA_REF_LINE_ID_KEY);
        mLocationId = bundle.getString(Global.EXTRA_LOCATION_ID_KEY);

        if (mRefData != null) {
            final RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
            //拿到上架总数
            tvRefLineNum.setText(lineData.lineNum);
            tvMaterialNum.setText(lineData.materialNum);
            tvMaterialDesc.setText(lineData.materialDesc);
            tvActQuantity.setText(lineData.actQuantity);
            tvBatchFlag.setText(batchFlag);
            tvInv.setText(invCode);
            tvInv.setTag(invId);

            etQuantity.setText(mQuantity);
            tvTotalQuantity.setText(totalQuantity);
            if("S".equalsIgnoreCase(lineData.shkzg)) {
                spXLoc.setEnabled(false);
                //上架仓位
                etSLocation.setText(mSelectedLocation);
            }
            //仓位数量
            tvLocQuantity.setText(mQuantity);
            //下载库存
            if ("H".equalsIgnoreCase(lineData.shkzg)) {
                etSLocation.setEnabled(false);
                loadInventoryInfo(lineData.workId, lineData.workCode, invId, invCode,
                        lineData.materialId, "", batchFlag);
            }
        }
    }

    private boolean isValidatedLocation() {
        if (TextUtils.isEmpty(mSelectedLocation)) {
            return false;
        }
        for (String item : mLocations) {
            if (item.equalsIgnoreCase(mSelectedLocation)) {
                return false;
            }
        }
        return true;
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
     * 下架处理获取库存
     *
     * @param workId
     * @param workCode
     * @param invId
     * @param invCode
     * @param materialId
     * @param location
     * @param batchFlag
     */
    private void loadInventoryInfo(String workId, String workCode, String invId, String invCode,
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
        mPresenter.getInventoryInfo("04", workId, invId, workCode, invCode, "",
                getString(tvMaterialNum), materialId, location, batchFlag, "", "", "", "",null);
    }

    @Override
    public void showInventory(List<InventoryEntity> list) {
        mInventoryDatas.clear();
        mInventoryDatas.addAll(list);
        if (mXLocationAdapter == null) {
            mXLocationAdapter = new LocationAdapter(mActivity, R.layout.item_simple_sp, mInventoryDatas);
            spXLoc.setAdapter(mXLocationAdapter);
        } else {
            mXLocationAdapter.notifyDataSetChanged();
        }

        //默认选择已经下架的仓位
        if (TextUtils.isEmpty(mSelectedLocation)) {
            spXLoc.setSelection(0);
            return;
        }

        String locationCombine;
        if (!TextUtils.isEmpty(mSpecialInvFlag) && !TextUtils.isEmpty(mSpecialInvNum)) {
            locationCombine = mSelectedLocation + mSpecialInvFlag + mSpecialInvNum;
        } else {
            locationCombine = mSelectedLocation;
        }
        int pos = -1;
        for (InventoryEntity loc : mInventoryDatas) {
            pos++;
            if (mSelectedLocation.equals(locationCombine)) {
                if (mSelectedLocation.equalsIgnoreCase(loc.location)) {
                    break;
                }
            } else {
                //如果在修改前选择的是寄售库存的仓位
                if (locationCombine.equalsIgnoreCase(loc.locationCombine))
                    break;
            }
        }
        if (pos >= 0 && pos < list.size()) {
            spXLoc.setSelection(pos);
        } else {
            spXLoc.setSelection(0);
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


    /**
     * 保存修改数据前的检查，注意这里之类必须根据业务检查仓位
     *
     * @return
     */
    @Override
    public boolean checkCollectedDataBeforeSave() {
        RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        if ("S".equalsIgnoreCase(lineData.shkzg) && !isValidatedLocation(getString(etSLocation))) {
            return false;
        }

        if (TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请输入入库数量");
            return false;
        }

        if ("H".equalsIgnoreCase(lineData.shkzg) && TextUtils.isEmpty(getString(tvInvQuantity))) {
            showMessage("请先获取库存");
            return false;
        }

        float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        float totalQuantityV = CommonUtil.convertToFloat(getString(tvTotalQuantity), 0.0f);
        float collectedQuantity = CommonUtil.convertToFloat(mQuantity, 0.0f);
        float quantityV = CommonUtil.convertToFloat(getString(etQuantity), 0.0f);
        if (Float.compare(quantityV, 0.0f) <= 0.0f) {
            showMessage("输入数量不合理");
            etQuantity.setText("");
            return false;
        }
        float residualQuantity = totalQuantityV - collectedQuantity + quantityV;//减去已经录入的数量
        if ("S".equalsIgnoreCase(lineData.shkzg)) {
            if (Float.compare(residualQuantity, actQuantityV) > 0.0f) {
                showMessage("输入实收数量有误");
                etQuantity.setText("");
                return false;
            }
        } else {
            //该仓位的库存数量
            final float invQuantityV = CommonUtil.convertToFloat(getString(tvInvQuantity), 0.0f);
            if (Float.compare(quantityV, invQuantityV) > 0.0f) {
                showMessage("输入数量大于库存数量，请重新输入");
                etQuantity.setText("");
                return false;
            }
        }
        mQuantity = quantityV + "";
        mTotalQuantity = residualQuantity;
        return true;
    }

    @Override
    public void saveCollectedData() {
        if (!checkCollectedDataBeforeSave()) {
            return;
        }
        Flowable.create((FlowableOnSubscribe<ResultEntity>) emitter -> {
            RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
            ResultEntity result = new ResultEntity();
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
            if ("S".equalsIgnoreCase(lineData.shkzg)) {
                //上架仓位
                result.location = getString(etSLocation);
            } else {
                //下架仓位
                result.location = mInventoryDatas.get(spXLoc.getSelectedItemPosition()).location;
            }
            result.batchFlag = getString(tvBatchFlag);
            result.quantity = getString(etQuantity);
            result.refDoc = lineData.refDoc;
            result.refDocItem = lineData.refDocItem;
            result.unit = TextUtils.isEmpty(lineData.recordUnit) ? lineData.materialUnit : lineData.recordUnit;
            result.unitRate = Float.compare(lineData.unitRate, 0.0f) == 0 ? 1.f : lineData.unitRate;
            result.specialConvert = "N";
            result.modifyFlag = "Y";
            result.shkzg = lineData.shkzg;
            emitter.onNext(result);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER).compose(TransformerHelper.io2main())
                .subscribe(result -> mPresenter.uploadCollectionDataSingle(result));
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        super.saveEditedDataSuccess(message);
        tvLocQuantity.setText(mQuantity);
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

}
