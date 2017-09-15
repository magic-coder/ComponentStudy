package com.richfit.module_qhyt.module_ms.n311;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.richfit.common_lib.widget.RichEditText;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzyk.base_msn_collect.BaseMSNCollectFragment;
import com.richfit.sdk_wzyk.base_msn_collect.imp.MSNCollectPresenterImp;

import java.util.List;

/**
 * 庆阳离线移库311数据采集界面。注意这里选择完库存地点后加载发出库存，
 * 这里将spSendLoc禁止，那么不会去加载发出库存，同时也不会去加载接收库存。
 * 因为离线不考虑库存。
 * Created by monday on 2017/4/12.
 */

public class QHYTLMSN311CollectFragment extends BaseMSNCollectFragment<MSNCollectPresenterImp> {


    RichEditText etSendLocation;
    EditText etSpecialInvFlag;
    EditText etSpecialInvNum;

    String specialConvert = "N";

    @Override
    public int getContentId() {
        return R.layout.qhyt_fragment_lmsn_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MSNCollectPresenterImp(mActivity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        etSendLocation = (RichEditText) mView.findViewById(R.id.et_send_location);
        etSpecialInvFlag = (EditText) mView.findViewById(R.id.et_special_inv_flag);
        etSpecialInvNum = (EditText) mView.findViewById(R.id.et_special_inv_num);
        return mView;
    }

    @Override
    protected void initView() {
        //这里将发出仓位禁止，作用是选择发出库位后过滤本次获取发出仓位库存的事件
        if (spSendLoc != null) {
            spSendLoc.setEnabled(false);
        }
        //工厂内移库不需要接收批次
        setVisibility(View.GONE, llRecBatch);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        etSendLocation.setOnRichEditTouchListener((view, location) -> {
            hideKeyboard(view);
            checkLocation(getString(etSendBatchFlag), location);
        });
    }

    @Override
    public void initData() {

    }

    /**
     * 如果打开了WM那么需要检查仓库号是否一致。
     * 对于工厂内的转储，没有接收工厂，那么接收工厂Id默认为发出工厂
     */
    @Override
    protected void checkWareHouseNum(int position) {
        if (position <= 0) {
            return;
        }
        final String workId = mRefData.workId;
        final String invCode = mSendInvs.get(position).invCode;
        final String recInvCode = mRefData.recInvCode;
        if (isOpenWM) {
            //没有打开WM，不需要检查ERP仓库号是否一致
            isWareHouseSame = true;
            return;
        }
        if (TextUtils.isEmpty(workId)) {
            showMessage("工厂为空");
            return;
        }
        if (TextUtils.isEmpty(invCode)) {
            showMessage("发出库位为空");
            return;
        }

        if (TextUtils.isEmpty(recInvCode)) {
            showMessage("接收库位为空");
            return;
        }

        mPresenter.checkWareHouseNum(isOpenWM, workId, invCode, workId, recInvCode, getOrgFlag());
    }

    @Override
    protected boolean checkHeaderData() {
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先选择工厂");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.invId)) {
            showMessage("请先选择发出库位");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.recInvId)) {
            showMessage("请先选择接收库位");
            return false;
        }
        return true;
    }


    /**
     * 在匹配缓存前先检查仓位是否存在
     *
     * @param batchFlag
     * @param location
     */
    private void checkLocation(String batchFlag, String location) {
        isLocationChecked = false;
        tvLocQuantity.setText("");

        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先输入工厂");
            return;
        }

        if (etMaterialNum.getTag() == null) {
            showMessage("请先获取物料信息");
            return;
        }

        if (TextUtils.isEmpty(location)) {
            showMessage("请先输入发出仓位");
            return;
        }

        final String invId = mSendInvs.get(spSendInv.getSelectedItemPosition()).invId;
        mPresenter.checkLocation("04", mRefData.workId, invId, batchFlag, location,null);
    }

    @Override
    public void checkLocationSuccess(String batchFlag, String location) {
        isLocationChecked = true;
        final String specialInvFlag = getString(etSpecialInvFlag);
        final String specialInvNum = getString(etSpecialInvNum);
        String locationCombine = location;
        if (!TextUtils.isEmpty(specialInvFlag) && !TextUtils.isEmpty(specialInvNum)) {
            locationCombine = location + "_" + specialInvFlag + "_" + specialInvNum;
        }
        loadLocationQuantity(batchFlag, location, locationCombine);
    }

    /**
     * 通过仓位和批次匹配出仓位缓存
     *
     * @param batchFlag
     * @param sendLocation
     * @param locationCombine
     */
    private void loadLocationQuantity(String batchFlag, String sendLocation, String locationCombine) {

        if (isOpenBatchManager && TextUtils.isEmpty(batchFlag)) {
            showMessage("请输入发出批次");
            return;
        }
        if (TextUtils.isEmpty(locationCombine)) {
            showMessage("请输入发出仓位");
            return;
        }

        if (mHistoryDetailList == null) {
            showMessage("请先获取物料信息");
            return;
        }

        String locQuantity = "0";
        String recLocation = "";
        String recBatchFlag = getString(etRecBatchFlag);
        for (RefDetailEntity detail : mHistoryDetailList) {
            List<LocationInfoEntity> locationList = detail.locationList;
            if (locationList != null && locationList.size() > 0) {
                for (LocationInfoEntity locationInfo : locationList) {

                    final boolean isMatched = isOpenBatchManager ?
                            locationCombine.equalsIgnoreCase(locationInfo.locationCombine)
                                    && batchFlag.equalsIgnoreCase(locationInfo.batchFlag) :
                            locationCombine.equalsIgnoreCase(locationInfo.locationCombine);

                    if (isMatched) {
                        locQuantity = locationInfo.quantity;
                        recLocation = locationInfo.recLocation;
                        recBatchFlag = locationInfo.recBatchFlag;
                        break;
                    }
                }
            }
        }
        tvLocQuantity.setText(locQuantity);
        //默认给接收仓位为发出仓位
        autoRecLoc.setText(sendLocation);
        //注意如果缓存中没有接收批次或者接收仓位，或者已经手动赋值,那么不用缓存更新它们
        if (!TextUtils.isEmpty(recLocation))
            autoRecLoc.setText(recLocation);
        if (!TextUtils.isEmpty(recBatchFlag) && !TextUtils.isEmpty(getString(etRecBatchFlag)))
            etRecBatchFlag.setText(recBatchFlag);
    }

    @Override
    public void showOperationMenuOnCollection(final String companyCode) {
        //每一次保存之前需要重置该字段
        specialConvert = "N";
        boolean isTurn = false;
        final int position = spSendLoc.getSelectedItemPosition();
        if (position >= 0 && !TextUtils.isEmpty(mInventoryDatas.get(position).specialInvFlag)
                && !TextUtils.isEmpty(mInventoryDatas.get(position).specialInvNum)) {
            isTurn = true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("温馨提示");
        String message = isTurn ? "检测到有寄售库存,您是否要进行寄售转自有" : "您真的确定要保存本次采集的数据?";
        builder.setMessage(message);
        //  第一个按钮
        builder.setPositiveButton("直接保存", (dialog, which) -> {
            dialog.dismiss();
            saveCollectedData();
        });
        if (isTurn) {
            builder.setNeutralButton("寄售转自有", (dialog, which) -> {
                dialog.dismiss();
                specialConvert = "Y";
                saveCollectedData();
            });
        }
        //  第三个按钮
        builder.setNegativeButton("取消保存", (dialog, which) -> dialog.dismiss());
        builder.create().show();
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
        if (TextUtils.isEmpty(mRefData.recInvId)) {
            showMessage("请先在抬头界面选择发出库位");
            return false;
        }

        if (spSendInv.getSelectedItemPosition() <= 0) {
            showMessage("请先选择发出库位");
            return false;
        }

        if (TextUtils.isEmpty(getString(etMaterialNum))) {
            showMessage("物料编码为空");
            return false;
        }

        if (TextUtils.isEmpty(getString(tvLocQuantity))) {
            showMessage("仓位数量为空");
            return false;
        }
        final String sendLocation = getString(etSendLocation);
        if (TextUtils.isEmpty(sendLocation)) {
            showMessage("请输入发出仓位");
            return false;
        }

        if (!isLocationChecked || sendLocation.length() > 10) {
            showMessage("您输入的发出仓位不合理");
            return false;
        }
        final String specialInvFlag = getString(etSpecialInvFlag);
        final String specialInvNum = getString(etSpecialInvNum);
        if (!TextUtils.isEmpty(specialInvFlag) && "K".equalsIgnoreCase(specialInvFlag)
                && TextUtils.isEmpty(specialInvNum)) {
            showMessage("请先输入特殊库存编号");
            return false;
        }
        //实发数量
        if (Float.valueOf(getString(etQuantity)) < 0.0f) {
            showMessage("输入数量不合理");
            return false;
        }

        //检查接收仓位
        final String recLocation = getString(autoRecLoc);
        if (TextUtils.isEmpty(recLocation) || recLocation.length() > 10) {
            showMessage("您输入的接收仓位不合理");
            return false;
        }
        return true;
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.location = getString(etSendLocation).toUpperCase();
        result.specialInvFlag = getString(etSpecialInvFlag);
        result.specialInvNum = getString(etSpecialInvNum);
        result.specialConvert = specialConvert;
        return result;
    }

    @Override
    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etSendLocation, etSpecialInvFlag, etSpecialInvNum);
    }

    @Override
    protected boolean getWMOpenFlag() {
        return false;
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
