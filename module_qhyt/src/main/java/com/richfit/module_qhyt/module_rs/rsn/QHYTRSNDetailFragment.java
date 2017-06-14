package com.richfit.module_qhyt.module_rs.rsn;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.sdk_wzrs.base_rsn_detail.BaseRSNDetailFragment;
import com.richfit.sdk_wzrs.base_rsn_detail.imp.RSNDetailPresenterImp;

/**
 * 注意这里是标准的无参考退库
 * Created by monday on 2017/3/2.
 */

public class QHYTRSNDetailFragment extends BaseRSNDetailFragment<RSNDetailPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new RSNDetailPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}


