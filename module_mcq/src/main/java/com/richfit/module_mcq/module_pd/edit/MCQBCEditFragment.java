package com.richfit.module_mcq.module_pd.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditFragment;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_mcq.R;
import com.richfit.sdk_wzpd.blind.edit.BCEditFragment;

/**
 * Created by monday on 2017/8/30.
 */

public class MCQBCEditFragment extends BaseEditFragment<MCQEditPresenterImp> implements IMCQBCEditView{

    //盘点仓位
    EditText etLocation;
    //主计量单位盘点数据
    EditText etMainQuantity;
    //副计量单位盘点数量
    EditText etQuantityCustom;

    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_bc_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MCQEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        etLocation = mView.findViewById(R.id.et_location);
        etMainQuantity = mView.findViewById(R.id.quantity);
        etQuantityCustom = mView.findViewById(R.id.mcq_et_quantity_custom);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        Bundle arguments = getArguments();
        if(arguments != null) {
            String location = arguments.getString(Global.EXTRA_LOCATION_KEY);
            String quantity = arguments.getString(Global.EXTRA_QUANTITY_KEY);
            String quantityCustom = arguments.getString(Global.EXTRA_QUANTITY_CUSTOM_KEY);
            etLocation.setText(location);
            etMainQuantity.setText(quantity);
            etQuantityCustom.setText(quantityCustom);
        }
    }

    @Override
    public void initDataLazily() {

    }


    @Override
    public boolean checkCollectedDataBeforeSave() {
        final float quantityV = CommonUtil.convertToFloat(getString(etMainQuantity), 0.0F);
        final float secondQuantityV = CommonUtil.convertToFloat(getString(etQuantityCustom), 0.0F);
        if (Float.compare(quantityV, 0.0f) <= 0.0f) {
            showMessage("您输入主计量单位的盘点数量不合理");
            return false;
        }

        if (Float.compare(secondQuantityV, 0.0f) <= 0.0f) {
            showMessage("您输入副计量单位的盘点数量不合理");
            return false;
        }

        if (mRefData == null) {
            showMessage("请现在抬头页面初始化本次盘点");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.checkLevel)) {
            showMessage("未获取到盘点级别");
            return false;
        }

        if (isEmpty(mRefData.checkId)) {
            showMessage("请先在抬头界面初始化本次盘点");
            return false;
        }
        if ("01".equals(mRefData.checkLevel) && TextUtils.isEmpty(mRefData.storageNum)) {
            showMessage("未获取到仓库号");
            return false;
        }


        if (etLocation.isEnabled() && isEmpty(getString(etLocation))) {
            showMessage("请输入盘点仓位");
            return false;
        }
        return true;
    }

    @Override
    public void saveCollectedData() {
        if (!checkCollectedDataBeforeSave()) {
            return;
        }
        ResultEntity result = provideResult();
        if(result == null) {
            showMessage("未获取到上传的数据");
            return;
        }
        mPresenter.uploadCheckDataSingle(result);
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = new ResultEntity();
        result.businessType = mRefData.bizType;
        result.checkId = mRefData.checkId;
        result.workId = mRefData.workId;
        result.invId = mRefData.invId;
        result.storageNum = mRefData.storageNum;
        result.location = CommonUtil.toUpperCase(getString(etLocation));
        result.voucherDate = mRefData.voucherDate;
        if(!TextUtils.isEmpty(result.voucherDate)) {
            result.year = result.voucherDate.substring(0,4);
            result.duration = result.voucherDate.substring(4,6);
        }
        result.userId = Global.USER_ID;
        result.workId = mRefData.workId;
        result.invId = mRefData.invId;
        result.checkLevel = mRefData.checkLevel;
        result.quantity = getString(etMainQuantity);
        result.quantityCustom = getString(etQuantityCustom);
        result.modifyFlag = "Y";
        return result;
    }

}
