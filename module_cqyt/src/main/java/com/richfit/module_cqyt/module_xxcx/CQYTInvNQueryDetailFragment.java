package com.richfit.module_cqyt.module_xxcx;

import android.text.TextUtils;

import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.sdk_xxcx.inventory_query_n.detail.InvNQueryDetailFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/8/4.
 */

public class CQYTInvNQueryDetailFragment extends InvNQueryDetailFragment {

    @Override
    public void initDataLazily() {
        if (mRefData == null) {
            showMessage("请现在抬头界面选择必要的信息");
            return;
        }

        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请现在抬头界面选择工厂");
            return;
        }

        if (TextUtils.isEmpty(mRefData.invId)) {
            showMessage("请现在抬头界面选择库存地点");
            return;
        }

        if (TextUtils.isEmpty(mRefData.bizType)) {
            showMessage("未获取到业务类型");
            return;
        }

        if (TextUtils.isEmpty(mRefData.locationType)) {
            showMessage("请现在抬头界面选择仓储类型");
            return;
        }

        //物料和仓位必输其一
        if (TextUtils.isEmpty(mRefData.materialNum) && TextUtils.isEmpty(mRefData.location)) {
            showMessage("请现在抬头界面输入物料或者仓位");
            return;
        }

        //开始加载
        startAutoRefresh();
    }

    @Override
    public boolean isNeedShowFloatingButton() {
        return false;
    }


    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("locationType", mRefData.locationType);
        queryParam.extraMap = extraMap;
        return queryParam;
    }
}
