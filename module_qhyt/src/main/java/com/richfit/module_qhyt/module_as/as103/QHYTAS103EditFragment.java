package com.richfit.module_qhyt.module_as.as103;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.richfit.module_qhyt.module_as.as103.imp.QHYTAS103EditPresenterImp;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;

/**
 * Created by monday on 2017/2/17.
 */

public class QHYTAS103EditFragment extends BaseASEditFragment<QHYTAS103EditPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new QHYTAS103EditPresenterImp(mActivity);
    }


    @Override
    protected void initView() {
        super.initView();
        etLocation.setEnabled(false);
        llLocation.setVisibility(View.GONE);
        llLocationQuantity.setVisibility(View.GONE);
    }

    @Override
    public void initDataLazily() {

    }
}
