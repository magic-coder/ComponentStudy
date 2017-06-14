package com.richfit.module_qhyt.module_rs.rsy;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

/**
 * Created by monday on 2017/2/27.
 */

public class QHYTRSYEditFragment extends BaseASEditFragment<ASEditPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        actQuantityName.setText("应退数量");
        quantityName.setText("实退数量");
    }

    @Override
    public void initDataLazily() {

    }

}
