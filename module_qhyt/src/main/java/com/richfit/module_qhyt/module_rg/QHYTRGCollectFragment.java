package com.richfit.module_qhyt.module_rg;

import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

/**
 * Created by monday on 2017/2/23.
 */

public class QHYTRGCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        tvQuantityName.setText("实退数量");
        tvActQuantityName.setText("应退数量");
    }

    @Override
    public void initData() {

    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
