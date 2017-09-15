package com.richfit.module_mcq.module_pd.collect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;

import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectFragment;
import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectView;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_mcq.R;
import com.richfit.sdk_wzpd.blind.collect.imp.BlindCollectPresenterImp;

/**
 * Created by monday on 2017/8/29.
 */

public class MCQBCColectFragment extends BaseCollectFragment<BlindCollectPresenterImp>
        implements IBaseCollectView {

    //盘点仓位
    EditText etLocation;
    //主计量单位盘点数据
    EditText etMainQuantity;
    //副计量单位盘点数量
    EditText etQuantityCustom;

    @Override
    protected int getContentId() {
        return R.layout.mcq_fragment_bc_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new BlindCollectPresenterImp(mActivity);
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

    }

    @Override
    public void initDataLazily() {
        etLocation.setEnabled(false);

        if (mRefData == null) {
            showMessage("请现在抬头页面初始化本次盘点");
            return;
        }

        if (isEmpty(mRefData.bizType)) {
            showMessage("未获取到业务类型");
            return;
        }

        if (isEmpty(mRefData.checkId)) {
            showMessage("请先在抬头界面初始化本次盘点");
            return;
        }

        if ("01".equals(mRefData.checkLevel) && TextUtils.isEmpty(mRefData.storageNum)) {
            showMessage("请先在抬头界面选择仓库号");
            return;
        }
        if ("02".equals(mRefData.checkLevel) && TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先在抬头界面选择工厂");
            return;
        }

        if ("02".equals(mRefData.checkLevel) && TextUtils.isEmpty(mRefData.invId)) {
            showMessage("请先在抬头界面选择库存");
            return;
        }

        if (isEmpty(mRefData.voucherDate)) {
            showMessage("请先在抬头界面选择过账日期");
            return;
        }


        String transferKey = (String) SPrefUtil.getData(mBizType, "0");
        if ("1".equals(transferKey)) {
            showMessage("本次采集已经过账,请先到数据明细界面进行数据上传操作");
            return;
        }

        etLocation.setEnabled(true);
    }

    @Override
    public void showOperationMenuOnCollection(final String companyCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("提示");
        builder.setMessage("您真的确定要提交本次盘点结果吗?");
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            saveCollectedData();
        });
        builder.show();
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
        if (result == null) {
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
        result.modifyFlag = "N";
        return result;
    }


    @Override
    public void saveCollectedDataSuccess(String message) {
        showMessage(message);
    }

    @Override
    public void saveCollectedDataFail(String message) {
        showMessage(message);
    }

    private void clearAllUI() {
        clearCommonUI(etLocation, etMainQuantity, etQuantityCustom);
    }

    @Override
    public void _onPause() {
        super._onPause();
        clearAllUI();
    }

}
