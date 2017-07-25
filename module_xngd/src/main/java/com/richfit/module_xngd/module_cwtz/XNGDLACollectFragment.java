package com.richfit.module_xngd.module_cwtz;

import android.text.TextUtils;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_cwtz.collect.LACollectFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/6/15.
 */

public class XNGDLACollectFragment extends LACollectFragment {

    @Override
    public void initDataLazily() {
        etMaterialNum.setEnabled(false);
        if (mRefData == null) {
            showMessage("在先在抬头界面选择相关的信息");
            return;
        }
        if ("1".equals(mRefData.invType) && TextUtils.isEmpty(mRefData.projectNum)) {
            showMessage("请现在抬头界面输入项目编号");
            return;
        }
        super.initDataLazily();
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (TextUtils.isEmpty(mRefData.bizType)) {
            showMessage("业务类型为空");
            return false;
        }

        if (isOpenBatchManager && TextUtils.isEmpty(getString(etBatchFlag))) {
            showMessage("请输入批次");
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

        final String location = getString(etSendLocation);
        if (TextUtils.isEmpty(location) || location.split(".").length != 4) {
            showMessage("源仓位不合理");
            return false;
        }

        final String recLocation = getString(etRecLocation);
        if (TextUtils.isEmpty(recLocation) || location.split(".").length != 4) {
            showMessage("目标仓位不合理");
            return false;
        }

        if (TextUtils.isEmpty(getString(tvSendInvQuantity))) {
            showMessage("请先获取有效库存");
            return false;
        }

        if (!refreshQuantity(getString(etRecQuantity))) {
            showMessage("调整数量有误");
            return false;
        }
        return true;
    }

    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = "1";
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", mRefData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum",mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }

}
