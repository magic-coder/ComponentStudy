package com.richfit.module_cqyt.module_xxcx;

import android.text.TextUtils;

import com.richfit.sdk_xxcx.inventory_query_n.detail.InvNQueryDetailFragment;

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

        //物料和仓位必输其一
        if (TextUtils.isEmpty(mRefData.materialNum) && TextUtils.isEmpty(mRefData.location)) {
            showMessage("请现在抬头界面输入物料或者仓位");
            return;
        }

        //开始加载
        startAutoRefresh();
    }
}
