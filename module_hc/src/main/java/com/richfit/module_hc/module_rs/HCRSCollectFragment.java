package com.richfit.module_hc.module_rs;

import android.view.View;

import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

/**
 * Created by monday on 2017/10/18.
 */

public class HCRSCollectFragment extends BaseASCollectFragment<ASCollectPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        tvActQuantityName.setText("应退数量");
        tvQuantityName.setText("实退数量");
        llLocationType.setVisibility(View.VISIBLE);
    }
}
