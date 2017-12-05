package com.richfit.module_qysh.module_ms.dgms301;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzyk.base_msn_collect.BaseMSNCollectFragment;
import com.richfit.sdk_wzyk.base_msn_collect.imp.MSNCollectPresenterImp;

import java.util.HashMap;

/**
 * 增加设备位号和设备名称。注意设备位号和设备名称只能通过设备id通过接口获取。
 * 主要的逻辑是如果通过扫描条码未获取到设备Id那么用户不能做该业务，如果存在
 * 设备Id,那么在获取物料信息后，先尝试调用getDeviceInfo的接口获取相关的
 * 信息，不管获取成功或者失败都应该让用户继续操作。
 * 另外庆阳的没有打开批次管理
 * Created by monday on 2017/2/8.
 */

public class QYSHMSN301CollectFragment extends BaseMSNCollectFragment<MSNCollectPresenterImp> {

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

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        super.initVariable(savedInstanceState);
        isOpenLocationType = false;
        isOpenRecLocationType = false;
    }

    @Override
    public int getContentId() {
        return R.layout.qysh_fragment_msy301_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MSNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        tvDeviceLocation = mView.findViewById(R.id.qysh_tv_device_location);
        tvDeviceName = mView.findViewById(R.id.qysh_tv_device_name);

        //隐藏发出和接收批次
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if (batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
        llRecBatchFlag.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadMaterialInfo(String materialNum, String batchFlag) {
        if (isEmpty(mDeviceId)) {
            showMessage("设备位Id为空");
            return;
        }
        super.loadMaterialInfo(materialNum, batchFlag);
    }

    /**
     * 重写该方法的目的是改变获取发出库位的时机，也就是说必须先去尝试获取设备相关的信息
     */
    @Override
    public void loadTransferSingleInfoComplete() {
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
    }

    /**
     * 如果设备信息失败不允许做其他的业务
     */
    @Override
    public void getDeviceInfoFail(String message) {
        showMessage(message);
        //禁用掉发出库位，以便禁止业务继续
        mSendInvs.clear();
        if (mSendInvAdapter != null) {
            mSendInvAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void getDeviceInfoComplete() {
        mPresenter.getSendInvsByWorks(mRefData.workId, getOrgFlag());
    }

    @Override
    protected boolean checkHeaderData() {
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先选择发出工厂");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.invId)) {
            showMessage("请先选择发出库位");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.recWorkId)) {
            showMessage("请先选择接收工厂");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.recInvId)) {
            showMessage("请先选择接收库位");
            return false;
        }
        return true;
    }

    @Override
    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(tvDeviceName, tvDeviceLocation);
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (isEmpty(mDeviceId)) {
            showMessage("设备Id为空");
            return false;
        }
        //发出工厂
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先选择发出工厂");
            return false;
        }
        //接收工厂
        if (TextUtils.isEmpty(mRefData.recWorkId)) {
            showMessage("请先选择接收工厂");
            return false;
        }
        //检查发出批次
        if (isOpenBatchManager && TextUtils.isEmpty(getString(etSendBatchFlag))) {
            showMessage("发出批次为空");
            return false;
        }

        //检查接收批次
        if (isOpenBatchManager && TextUtils.isEmpty(getString(etRecBatchFlag))) {
            showMessage("请输入接收批次");
            return false;
        }

        //检查发出仓位
        final int sendLocPos = spSendLoc.getSelectedItemPosition();
        if (sendLocPos <= 0) {
            showMessage("请先选择发出仓位");
            return false;
        }
        final String sendLocation = mInventoryDatas.get(sendLocPos).location;
        if (!TextUtils.isEmpty(sendLocation) && sendLocation.length() != 11) {
            showMessage("您选择的发出仓位格式不合理");
            return false;
        }

        //检查接收仓位
        if (isWareHouseSame && TextUtils.isEmpty(getString(autoRecLoc))) {
            showMessage("请输入接收仓位");
            return false;
        }

        if (isWareHouseSame && getString(autoRecLoc).length() != 11) {
            showMessage("您输入的接收仓位格式不对");
            return false;
        }

        return super.checkCollectedDataBeforeSave();
    }


    @Override
    public void _onPause() {
        clearCommonUI(tvDeviceLocation, tvDeviceName);
        super._onPause();
    }

    @Override
    protected boolean getWMOpenFlag() {
        return false;
    }

    @Override
    protected int getOrgFlag() {
        return getInteger(R.integer.orgSecond);
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.deviceId = mDeviceId;
        result.batchFlag = null;
        result.recBatchFlag = null;
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
