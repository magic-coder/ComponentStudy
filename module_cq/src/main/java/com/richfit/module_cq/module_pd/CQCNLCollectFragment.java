package com.richfit.module_cq.module_pd;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzpd.checkn.collect.CNCollectFragment;

/**
 * 离线盘点
 * Created by monday on 2017/8/10.
 */

public class CQCNLCollectFragment extends CNCollectFragment {

    EditText etBatchFlag;


    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type, list);
        if (list != null && list.length > 12) {
            final String materialNum = list[Global.MATERIAL_POS];
            final String batchFlag = list[Global.BATCHFALG_POS];
            if (cbSingle.isChecked() && materialNum.equalsIgnoreCase(getString(etMaterialNum))) {
                saveCollectedData();
            } else if (!cbSingle.isChecked()) {
                etMaterialNum.setText(materialNum);
                etBatchFlag.setText(batchFlag);
                getCheckTransferInfoSingle(materialNum, getString(etCheckLocation));
            }

        } else if (list != null && list.length == 1 & !cbSingle.isChecked()) {
            final String location = list[0];
            etCheckLocation.setText("");
            etCheckLocation.setText(location);
        } else if (list != null && list.length == 2 & !cbSingle.isChecked()) {
            final String location = list[0];
            etCheckLocation.setText("");
            etCheckLocation.setText(location);
            return;
        }
    }

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_cnl_collect;
    }

    @Override
    public void initDataLazily() {
        etMaterialNum.setEnabled(false);
        if (mRefData == null) {
            showMessage("请先在抬头页面初始化本次盘点");
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

        //处理特殊标识
        final String specialFlag = mRefData.specialFlag;
        final boolean isEditable = "Y".equals(specialFlag) ? true : false;
        etSpecialInvFlag.setEnabled(isEditable);
        etSpecialInvNum.setEnabled(isEditable);
        etMaterialNum.setEnabled(true);
    }

    @Override
    public void initView() {
        super.initView();
        etBatchFlag = mView.findViewById(R.id.et_batch_flag);
    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.batchFlag = getString(etBatchFlag);
        return result;
    }

}
