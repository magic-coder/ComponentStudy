package com.richfit.module_xngd.module_ds.dsn;


import android.text.TextUtils;

import com.richfit.module_xngd.R;
import com.richfit.sdk_wzck.base_dsn_collect.BaseDSNCollectFragment;
import com.richfit.sdk_wzck.base_dsn_collect.imp.DSNCollectPresenterImp;

/**
 * Created by monday on 2017/3/27.
 */

public class XNGDDSNCollectFragment extends BaseDSNCollectFragment<DSNCollectPresenterImp> {

    @Override
    protected String getInvType() {
        return "1";
    }

    @Override
    protected String getInventoryQueryType() {
        return getString(R.string.inventoryQueryTypePrecise);
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {

    }

    /**
     * 检查移动类型，成本中心，以及领料日期
     */
    @Override
    public void initDataLazily() {
        if (mRefData == null) {
            showMessage("请先在抬头界面选择工厂");
            return;
        }

        if (TextUtils.isEmpty(mRefData.moveType)) {
            showMessage("请先在抬头界面选择移动类型");
            return;
        }

        if (TextUtils.isEmpty(mRefData.costCenter)) {
            showMessage("请现在抬头界面输入成本中心");
            return;
        }

        if (TextUtils.isEmpty(mRefData.voucherDate)) {
            showMessage("请现在抬头界面输入领料日期");
            return;
        }
        super.initDataLazily();

    }
}
