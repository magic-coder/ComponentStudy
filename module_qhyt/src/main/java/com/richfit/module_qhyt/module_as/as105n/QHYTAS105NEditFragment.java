package com.richfit.module_qhyt.module_as.as105n;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;


/**
 * Created by monday on 2017/2/20.
 */

public class QHYTAS105NEditFragment extends BaseASEditFragment<ASEditPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }


    @Override
    public void initDataLazily() {

    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        final String location = getString(etLocation);
        if (TextUtils.isEmpty(location) || location.length() > 10) {
            showMessage("您输入的上架仓位不合理");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }
}
