package com.richfit.module_qhyt.module_rs.rsn;


import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzrs.base_rsn_collect.BaseRSNCollectFragment;
import com.richfit.sdk_wzrs.base_rsn_collect.imp.RSNCollectPresenterImp;

/**
 * Created by monday on 2017/3/2.
 */

public class QHYTRSNCollectFragment extends BaseRSNCollectFragment<RSNCollectPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new RSNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected String getInvType() {
        return "1";
    }

    @Override
    protected String getInventoryQueryType() {
        return getString(R.string.inventoryQueryTypeSAPLocation);
    }
}
