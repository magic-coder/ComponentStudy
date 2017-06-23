package com.richfit.module_qhyt.module_ms.ubsto351;


import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzyk.base_ms_collect.BaseMSCollectFragment;
import com.richfit.sdk_wzyk.base_ms_collect.imp.MSCollectPresenterImp;

/**
 * 注意351发出没有接收仓位和接收批次,同时不需要显示发出工厂。
 * Created by monday on 2017/2/10.
 */

public class QHYTUbSto351CollectFragment extends BaseMSCollectFragment<MSCollectPresenterImp> {


    @Override
    protected int getOrgFlag() {
        return getInteger(R.integer.orgNorm);
    }

    @Override
    public void initPresenter() {
        mPresenter = new MSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {

    }
}
