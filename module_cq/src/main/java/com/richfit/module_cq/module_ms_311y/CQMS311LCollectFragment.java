package com.richfit.module_cq.module_ms_311y;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.richfit.common_lib.utils.UiUtil;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzyk.base_ms_collect.BaseMSCollectFragment;
import com.richfit.sdk_wzyk.base_ms_collect.imp.MSCollectPresenterImp;

/**
 * Created by monday on 2017/4/13.
 */

public class CQMS311LCollectFragment extends BaseMSCollectFragment<MSCollectPresenterImp> {

    RichEditText etSendLocation;
    EditText etSpecialInvFlag;
    EditText etSpecialInvNum;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        if (!etMaterialNum.isEnabled()) {
            showMessage("请先在抬头界面获取相关数据");
            return;
        }
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
                getTransferSingle(spSendLoc.getSelectedItemPosition());
            } else if (!cbSingle.isChecked()) {
                loadMaterialInfo(materialNum, batchFlag);
            }
        } else if (list != null && list.length == 1 & !cbSingle.isChecked()) {
            final String location = list[0];
            if (etRecLoc.isFocused()) {
                clearCommonUI(etRecLoc);
                etRecLoc.setText(location);
                return;
            } else  {
                //扫描发出仓位
                etSendLocation.setText(location);
                getTransferSingle(-1);
            }
        }
    }

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_ms311l_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MSCollectPresenterImp(mActivity);
    }


    @Override
    protected void initView() {
        etSendLocation = (RichEditText) mView.findViewById(R.id.et_send_location);
        etSpecialInvFlag = (EditText) mView.findViewById(R.id.et_special_inv_flag);
        etSpecialInvNum = (EditText) mView.findViewById(R.id.et_special_inv_num);
        spSendLoc.setEnabled(false);
        llRecLocation.setVisibility(View.VISIBLE);
        llRecBatch.setVisibility(View.VISIBLE);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        //注意这里点击的是输入仓位控件
        etSendLocation.setOnRichEditTouchListener((view, location) -> {
            hideKeyboard(view);
            getTransferSingle(-1);
        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected void loadInventory(int position) {

    }

    @Override
    protected void getTransferSingle(int position) {

        final String batchFlag = getString(etSendBatchFlag);
        final String location = getString(etSendLocation);
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
        if (spSendInv.getSelectedItemPosition() <= 0) {
            showMessage("请先选择库存地点");
            return;
        }

        if (isOpenBatchManager)
            if (TextUtils.isEmpty(batchFlag)) {
                showMessage("请先输入批次");
                return;
            }
        if (TextUtils.isEmpty(locationCombine)) {
            showMessage("请先输入发出仓位");
            return;
        }

        final RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        final String refCodeId = mRefData.refCodeId;
        final String refType = mRefData.refType;
        final String bizType = mRefData.bizType;
        final String refLineId = lineData.refLineId;
        mCachedBatchFlag = "";
        mPresenter.getTransferInfoSingle(refCodeId, refType, bizType, refLineId,
                batchFlag, locationCombine, lineData.refDoc, CommonUtil.convertToInt(lineData.refDocItem), Global.USER_ID);
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
        if (spSendInv.getSelectedItemPosition() <= 0) {
            showMessage("请先选择库存地点");
            return false;
        }

        //批次
        if (isOpenBatchManager && !isBatchValidate) {
            showMessage("批次输入有误，请检查批次是否与缓存批次输入一致");
            return false;
        }

        if (TextUtils.isEmpty(getString(etSendLocation))) {
            showMessage("请输入的发出仓位");
            return false;
        }


        final String specialInvFlag = getString(etSpecialInvFlag);
        final String specialInvNum = getString(etSpecialInvNum);
        if(!TextUtils.isEmpty(specialInvFlag) && "K".equalsIgnoreCase(specialInvFlag)
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
        result.location = getString(etSendLocation);
        result.specialInvFlag = getString(etSpecialInvFlag);
        result.specialInvNum = getString(etSpecialInvNum);
        return result;
    }

    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etSendLocation,etSpecialInvFlag,etSpecialInvNum);
    }

    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.queryType = "01";
        return queryParam;
    }


    @Override
    protected int getOrgFlag() {
        return getInteger(R.integer.orgNorm);
    }
}
