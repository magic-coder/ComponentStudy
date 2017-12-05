package com.richfit.sdk_wzpd.checkn.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditFragment;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzpd.R;
import com.richfit.sdk_wzpd.R2;
import com.richfit.sdk_wzpd.checkn.edit.imp.CNEditPresenterImp;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by monday on 2016/12/6.
 */

public class CNEditFragment extends BaseEditFragment<CNEditPresenterImp>
        implements ICNEditView {

    @BindView(R2.id.et_check_location)
    TextView etCheckLocation;
    @BindView(R2.id.ll_check_location)
    LinearLayout llCheckLocation;
    @BindView(R2.id.tv_material_num)
    TextView tvMaterialNum;
    @BindView(R2.id.tv_material_desc)
    TextView tvMaterialDesc;
    @BindView(R2.id.tv_material_group)
    TextView tvMaterialGroup;
    @BindView(R2.id.tv_special_inv_flag)
    TextView tvSpecialInvFlag;
    @BindView(R2.id.tv_special_inv_num)
    TextView tvSpecialInvNum;
    @BindView(R2.id.tv_inv_quantity)
    TextView tvInvQuantity;
    @BindView(R2.id.et_check_quantity)
    EditText etCheckQuantity;
    @BindView(R2.id.tv_total_quantity)
    TextView tvTotalQuantity;

    String mCheckLineId;
    String mWorkId;
    String mInvId;

    @Override
    protected int getContentId() {
        return R.layout.wzpd_fragment_cn_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new CNEditPresenterImp(mActivity);
    }


    @Override
    public void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCheckLineId = bundle.getString(Global.EXTRA_REF_LINE_ID_KEY);
            String materialNum = bundle.getString(Global.EXTRA_MATERIAL_NUM_KEY);
            String materialId = bundle.getString(Global.EXTRA_MATERIAL_ID_KEY);
            String materialGroup = bundle.getString(Global.EXTRA_MATERIAL_GROUP_KEY);
            String materialDesc = bundle.getString(Global.EXTRA_MATERIAL_DESC_KEY);
            String location = bundle.getString(Global.EXTRA_LOCATION_KEY);
            String quantity = bundle.getString(Global.EXTRA_QUANTITY_KEY);
            String invQuantity = bundle.getString(Global.EXTRA_INV_QUANTITY_KEY);
            String specialInvFlag = bundle.getString(Global.EXTRA_SPECIAL_INV_FLAG_KEY);
            String specialInvNum = bundle.getString(Global.EXTRA_SPECIAL_INV_NUM_KEY);
            mWorkId = bundle.getString(Global.EXTRA_WORK_ID_KEY);
            mInvId = bundle.getString(Global.EXTRA_INV_ID_KEY);
            tvMaterialNum.setText(materialNum);
            tvMaterialNum.setTag(materialId);
            tvMaterialDesc.setText(materialDesc);
            tvMaterialGroup.setText(materialGroup);
            tvSpecialInvFlag.setText(specialInvFlag);
            tvSpecialInvNum.setText(specialInvNum);
            etCheckLocation.setText(location);
            tvInvQuantity.setText(invQuantity);
            etCheckQuantity.setText(quantity);
            tvTotalQuantity.setText(quantity);
            //如果是库存级别的不允许修改盘点仓位
            etCheckLocation.setEnabled(false);
            if (mRefData != null && (!TextUtils.isEmpty(mRefData.checkLevel))) {
                if ("02".equals(mRefData.checkLevel)) {
                    llCheckLocation.setVisibility(View.GONE);
                    etCheckLocation.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (TextUtils.isEmpty(mCheckLineId)) {
            showMessage("该行的盘点Id为空");
            return false;
        }

        final String checkLevel = mRefData.checkLevel;
        if (TextUtils.isEmpty(checkLevel)) {
            showMessage("请先在抬头选择盘点类型");
            return false;
        }

        //如果是库存级需要检查工厂和库存地点
        if ("02".equals(checkLevel) && TextUtils.isEmpty(mWorkId)) {
            showMessage("盘点工厂为空");
            return false;
        }


        if ("02".equals(checkLevel) && TextUtils.isEmpty(mInvId)) {
            showMessage("盘点库存地点为空");
            return false;
        }
        if (TextUtils.isEmpty(getString(etCheckQuantity))) {
            showMessage("请输入盘点数量");
            return false;
        }
        if (etCheckLocation.isEnabled() && TextUtils.isEmpty(getString(etCheckLocation))) {
            showMessage("请输入盘点仓位");
            return false;
        }

        if (etCheckLocation.isEnabled() && getString(etCheckLocation).length() > 10) {
            showMessage("您输入的盘点仓位不合理");
            return false;
        }

        float quantity = CommonUtil.convertToFloat(getString(etCheckQuantity), 0.0F);
        if (quantity <= 0.0F) {
            showMessage("输入盘点数量小于零");
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
        result.checkLineId = mCheckLineId;
        result.specialInvFlag = getString(tvSpecialInvFlag);
        result.specialInvNum = getString(tvSpecialInvNum);
        result.location = getString(etCheckLocation);
        result.voucherDate = mRefData.voucherDate;
        result.userId = Global.USER_ID;
        result.workId = mRefData.workId;
        result.invId = mRefData.invId;
        result.materialId = CommonUtil.Obj2String(tvMaterialNum.getTag());
        result.quantity = getString(etCheckQuantity);
        result.modifyFlag = "Y";
        return result;
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        super.saveEditedDataSuccess(message);
        tvTotalQuantity.setText(getString(etCheckQuantity));
        etCheckQuantity.setText("");
    }

    @Override
    public void saveEditedDataFail(String message) {
        super.saveEditedDataFail(message);
        etCheckQuantity.setText("");
    }


    @Override
    public void networkConnectError(String retryAction) {
        showNetConnectErrorDialog(retryAction);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void retry(String retryAction) {
        saveCollectedData();
        super.retry(retryAction);
    }
}
