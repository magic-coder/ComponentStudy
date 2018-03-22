package com.richfit.module_cq.module_dsy;

import android.text.TextUtils;
import android.widget.EditText;

import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

/**
 * Created by monday on 2018/2/7.
 */

public class CQLCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {

    RichEditText etLocation;
    EditText etSpecialInvFlag;
    EditText etSpecialInvNum;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type, list);
        if (list != null && list.length == 1 && !cbSingle.isChecked()) {
            final String location = list[0];
            etLocation.setText(location);
            getTransferSingle(-1);
        }
    }

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_dsyl_collect;
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        etLocation = (RichEditText) mView.findViewById(R.id.et_location);
        etSpecialInvFlag = (EditText) mView.findViewById(R.id.et_special_inv_flag);
        etSpecialInvNum = (EditText) mView.findViewById(R.id.et_special_inv_num);
        spLocation.setEnabled(false);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        //注意这里点击的是输入仓位控件
        etLocation.setOnRichEditTouchListener((view, location) -> {
            hideKeyboard(view);
            getTransferSingle(-1);
        });
    }

    @Override
    protected void getTransferSingle(int position) {

        final String batchFlag = getString(etBatchFlag);
        final String location = getString(etLocation);
        final String specialInvFlag = getString(etSpecialInvFlag);
        final String specialInvNum = getString(etSpecialInvNum);

        String locationCombine = location;
        if (!TextUtils.isEmpty(specialInvFlag) && !TextUtils.isEmpty(specialInvNum)) {
            locationCombine = location + "_" + specialInvFlag + "_" + specialInvNum;
        }
        if (spRefLine.getSelectedItemPosition() <= 0) {
            showMessage("请先选择单据行");
            return;
        }
        //检验是否选择了库存地点
        if (spInv.getSelectedItemPosition() <= 0) {
            showMessage("请先选择库存地点");
            return;
        }

        if (isOpenBatchManager)
            if (TextUtils.isEmpty(batchFlag)) {
                showMessage("请先输入批次");
                return;
            }
        if (TextUtils.isEmpty(locationCombine)) {
            showMessage("请先输入下架仓位");
            return;
        }

        final RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        final String refCodeId = mRefData.refCodeId;
        final String refType = mRefData.refType;
        final String bizType = mRefData.bizType;
        final String refLineId = lineData.refLineId;
        mCachedBatchFlag = "";
        mPresenter.getTransferInfoSingle(refCodeId, refType, bizType, refLineId, getString(etMaterialNum),
                batchFlag, locationCombine, lineData.refDoc, CommonUtil.convertToInt(lineData.refDocItem), Global.USER_ID);
    }

    @Override
    protected void loadInventory(int position) {

    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        //检查数据是否可以保存
        if (spRefLine.getSelectedItemPosition() <= 0) {
            showMessage("请先选择单据行");
            return false;
        }
        //物资条码
        if (TextUtils.isEmpty(getString(etMaterialNum))) {
            showMessage("请先输入物料条码");
            return false;
        }
        //发出库位
        if (spInv.getSelectedItemPosition() <= 0) {
            showMessage("请先选择库存地点");
            return false;
        }

        //批次
        if (isOpenBatchManager && !isBatchValidate) {
            showMessage("批次输入有误，请检查批次是否与缓存批次输入一致");
            return false;
        }

        if (TextUtils.isEmpty(getString(etLocation))) {
            showMessage("请输入的发出仓位");
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
        if (TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请先输入数量");
            return false;
        }

        if (Float.valueOf(getString(etQuantity)) < 0.0f) {
            showMessage("移库数量不合理");
            return false;
        }

        return true;
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.location = getString(etLocation);
        result.specialInvFlag = getString(etSpecialInvFlag);
        result.specialInvNum = getString(etSpecialInvNum);
        return result;
    }

    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etLocation, etSpecialInvFlag, etSpecialInvNum);
    }

}
