package com.richfit.module_qysh.module_cwtz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_cwtz.collect.LACollectFragment;

import java.util.HashMap;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHDGLACollectFragmnet extends LACollectFragment {

    /*设备位号*/
    TextView tvDeviceLocation;
    /*设备名称*/
    TextView tvDeviceName;
    String mDeviceId;

    /**
     * 处理扫描。重写该方法的目的是增加设备Id的处理逻辑
     *
     * @param type
     * @param list
     */
    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        if (list != null && list.length >= 12) {
            mDeviceId = list[list.length - 2];
        }
        super.handleBarCodeScanResult(type, list);
    }

    //由于代管料单独记账所以强制由bizType级控制是否打开了仓储类型
    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        super.initVariable(savedInstanceState);
        isOpenLocationType = false;
        isOpenRecLocationType = false;
    }

    @Override
    public int getContentId() {
        return R.layout.qysh_fragment_la_collect;
    }

    @Override
    public void initView() {
        super.initView();
        tvDeviceLocation = mView.findViewById(R.id.qysh_tv_device_location);
        tvDeviceName = mView.findViewById(R.id.qysh_tv_device_name);
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if(batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initDataLazily() {
        etMaterialNum.setEnabled(false);
        if (mRefData == null) {
            showMessage("在先在抬头界面选择相关的信息");
            return;
        }

        if (TextUtils.isEmpty(mRefData.workCode)) {
            showMessage("请现在抬头界面选择工厂");
            return;
        }

        if (TextUtils.isEmpty(mRefData.invCode)) {
            showMessage("请先在抬头界面选择库存地点");
            return;
        }

        etMaterialNum.setEnabled(true);
        isOpenBatchManager = true;
        etBatchFlag.setEnabled(true);
    }

    @Override
    protected void loadMaterialInfo(String materialNum, String batchFlag) {
        if (isEmpty(mDeviceId)) {
            showMessage("设备位Id为空");
            return;
        }
        super.loadMaterialInfo(materialNum, batchFlag);
    }


    @Override
    public void getMaterialInfoComplete() {
        super.getMaterialInfoComplete();
        //获取设备信息
        if (isEmpty(mDeviceId)) {
            showMessage("设备Id为空");
            return;
        }
        mPresenter.getDeviceInfo(mDeviceId);
    }

    /**
     * 获取设备信息成功，开始将信息显示
     *
     * @param result
     */
    @Override
    public void getDeviceInfoSuccess(ResultEntity result) {
        tvDeviceLocation.setText(result.deviceLocation);
        tvDeviceName.setText(result.deviceName);
        if (isOpenLocationType) {
            mPresenter.getDictionaryData("locationType");
        } else {
            //获取提示库存
            loadTipInventory();
        }
    }


    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (isEmpty(mDeviceId)) {
            showMessage("设备Id为空");
            return false;
        }
        if (!etMaterialNum.isEnabled()) {
            showMessage("请先获取物料信息");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.bizType)) {
            showMessage("业务类型为空");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先在抬头界面选择工厂");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.invId)) {
            showMessage("请先在抬头界面选择库存地点");
            return false;
        }

        if (spSpecialInv.getSelectedItemPosition() <= 0) {
            showMessage("请先选择特殊库存标识");
            return false;
        }

        Object tag = etMaterialNum.getTag();
        if (tag == null || TextUtils.isEmpty(tag.toString())) {
            showMessage("请先获取物料信息");
            return false;
        }

        if (TextUtils.isEmpty(getString(tvSendInvQuantity))) {
            showMessage("请先获取有效库存");
            return false;
        }

        //必须先判断调整数据是否输入
        if (TextUtils.isEmpty(getString(etRecQuantity))) {
            showMessage("调整数量有误");
            return false;
        }

        if (!refreshQuantity(getString(etRecQuantity))) {
            showMessage("调整数量有误");
            return false;
        }
        return true;
    }

    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(tvDeviceLocation,tvDeviceName);
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.deviceId = mDeviceId;
        result.batchFlag = null;
        return result;
    }

    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.invType = "0";
        queryParam.queryType = "03";
        if (queryParam.extraMap == null)
            queryParam.extraMap = new HashMap<>();
        queryParam.extraMap.put("deviceId", mDeviceId);
        return queryParam;
    }
}
