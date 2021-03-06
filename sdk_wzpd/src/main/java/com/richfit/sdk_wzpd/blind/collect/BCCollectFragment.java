package com.richfit.sdk_wzpd.blind.collect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectFragment;
import com.richfit.common_lib.lib_mvp.BaseFragment;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.common_lib.widget.RichEditText;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzpd.R;
import com.richfit.sdk_wzpd.R2;
import com.richfit.sdk_wzpd.blind.collect.imp.BlindCollectPresenterImp;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 当盘点级别为02(库存级)是盘点仓位不显示。这里通过盘点仓位的etCheckLocation控件的属性
 * enable来控制是否显示和检查盘点仓位的输入。
 * Created by monday on 2017/3/3.
 */

public class BCCollectFragment extends BaseCollectFragment<BlindCollectPresenterImp>
        implements IBlindCollectView {


    @BindView(R2.id.ll_check_location)
    LinearLayout llCheckLocation;
    @BindView(R2.id.et_check_location)
    EditText etCheckLocation;
    @BindView(R2.id.et_material_num)
    RichEditText etMaterialNum;
    @BindView(R2.id.tv_material_desc)
    TextView tvMaterialDesc;
    @BindView(R2.id.tv_material_group)
    TextView tvMaterialGroup;
    @BindView(R2.id.et_quantity)
    EditText etQuantity;
    @BindView(R2.id.cb_single)
    CheckBox cbSingle;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type, list);
        if (list != null && list.length > 12) {
            final String materialNum = list[Global.MATERIAL_POS];
            if (cbSingle.isChecked() && materialNum.equalsIgnoreCase(getString(etMaterialNum))) {
                saveCollectedData();
            } else {
                etMaterialNum.setText(materialNum);
                getCheckTransferInfoSingle(materialNum, getString(etCheckLocation));
            }

        } else if (list != null && list.length == 1 & !cbSingle.isChecked() && etCheckLocation.isEnabled()) {
            final String location = list[0];
            etCheckLocation.setText("");
            etCheckLocation.setText(location);
        }
    }

    @Override
    protected int getContentId() {
        return R.layout.wzpd_fragment_blind_collect;
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

    }
    /**
     * 注册所有UI事件
     */
    @Override
    public void initEvent() {
       /*扫描后者手动输入物资条码*/
        etMaterialNum.setOnRichEditTouchListener((view, materialNum) -> {
            hideKeyboard(etMaterialNum);
            getCheckTransferInfoSingle(materialNum, getString(etCheckLocation));
        });

       /*单品(注意单品仅仅控制实收数量，累计数量是由行信息里面控制)*/
        cbSingle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etQuantity.setText(isChecked ? "1" : "");
            etQuantity.setEnabled(!isChecked);
        });
    }

    @Override
    public void initData() {

    }

    /**
     * 检查抬头界面的必要的字段是否已经赋值
     */
    @Override
    public void initDataLazily() {
        etMaterialNum.setEnabled(false);

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
        //如果是库存级，那么盘点仓位不显示。注意01必须将盘点仓位显示
        final boolean isShowCheckLocation = "02".equals(mRefData.checkLevel);
        llCheckLocation.setVisibility(isShowCheckLocation ? View.GONE : View.VISIBLE);
        etCheckLocation.setEnabled(!isShowCheckLocation);

        String transferKey = (String) SPrefUtil.getData(mBizType, "0");
        if ("1".equals(transferKey)) {
            showMessage("本次采集已经过账,请先到数据明细界面进行数据上传操作");
            return;
        }
        etMaterialNum.setEnabled(true);

    }

    @Override
    public void getCheckTransferInfoSingle(String materialNum, String location) {
        if (!etMaterialNum.isEnabled()) {
            return;
        }
        if (TextUtils.isEmpty(materialNum)) {
            showMessage("请输入物资条码");
            return;
        }

        if (etCheckLocation.isEnabled() && TextUtils.isEmpty(location)) {
            showMessage("请先输入盘点仓位");
            return;
        }

        clearAllUI();
        mPresenter.getCheckTransferInfoSingle(mRefData.checkId, location, "01", materialNum, mBizType);
    }

    /**
     * 获取盘点库存成功。保存到内存。
     */
    @Override
    public void loadMaterialInfoSuccess(MaterialEntity data) {
        tvMaterialDesc.setText(data.materialDesc);
        tvMaterialGroup.setText(data.materialGroup);
        etMaterialNum.setTag(data.id);
    }

    @Override
    public void loadMaterialInfoFail(String message) {
        showMessage(message);
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
        final float quantityV = CommonUtil.convertToFloat(getString(etQuantity), 0.0F);

        if (Float.compare(quantityV, 0.0f) <= 0.0f) {
            showMessage("您输入的盘点数量不合理");
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


        if (etCheckLocation.isEnabled() && isEmpty(getString(etCheckLocation))) {
            showMessage("请输入盘点仓位");
            return false;
        }

        if (etCheckLocation.isEnabled()) {
            if (getString(etCheckLocation).length() > 10) {
                showMessage("您输入的盘点仓位不合理");
                return false;
            }
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
        result.location = CommonUtil.toUpperCase(getString(etCheckLocation));
        result.voucherDate = mRefData.voucherDate;
        result.userId = Global.USER_ID;
        result.workId = mRefData.workId;
        result.invId = mRefData.invId;
        result.checkLevel = mRefData.checkLevel;
        result.materialId = CommonUtil.Obj2String(etMaterialNum.getTag());
        result.quantity = getString(etQuantity);
        result.modifyFlag = "N";
        return result;
    }


    @Override
    public void saveCollectedDataSuccess(String message) {
        showMessage(message);
        if (!cbSingle.isChecked()) {
            etQuantity.setText("");
        }
    }


    private void clearAllUI() {
        clearCommonUI(tvMaterialDesc, tvMaterialGroup, etQuantity, cbSingle);
    }

    @Override
    public void _onPause() {
        super._onPause();
        clearAllUI();
        clearCommonUI(etMaterialNum, etCheckLocation);
    }

    @Override
    public void retry(String action) {
        switch (action) {
            case Global.RETRY_LOAD_INVENTORY_ACTION:
                getCheckTransferInfoSingle(getString(etMaterialNum), getString(etCheckLocation));
                break;
        }
        super.retry(action);
    }




}
