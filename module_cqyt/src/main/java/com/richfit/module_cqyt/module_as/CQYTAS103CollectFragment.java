package com.richfit.module_cqyt.module_as;

import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

/**
 * Created by monday on 2017/6/29.
 */

public class CQYTAS103CollectFragment extends BaseASCollectFragment<ASCollectPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
