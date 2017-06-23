package com.richfit.module_xngd.module_cwtz;

import android.text.TextUtils;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_cwtz.collect.LACollectFragment;

/**
 * Created by monday on 2017/6/15.
 */

public class CNGDLACollectFragment extends LACollectFragment {

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
        if (TextUtils.isEmpty(location) || location.length() > 11) {
            showMessage("源仓位不合理");
            return false;
        }

        final String recLocation = getString(etRecLocation);
        if (TextUtils.isEmpty(recLocation) || recLocation.length() > 11) {
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
        return param;
    }

}
