package com.richfit.module_hc.module_ds;

import android.view.View;

import com.richfit.data.helper.CommonUtil;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

/**
 * Created by monday on 2017/10/18.
 */

public class HCDSCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        llLocationType.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    public boolean refreshQuantity(String quantity) {
        if(CommonUtil.isInteger(quantity)) {
            return super.refreshQuantity(quantity);
        }
        showMessage("实发数量必须为整数");
        return false;
    }
}
