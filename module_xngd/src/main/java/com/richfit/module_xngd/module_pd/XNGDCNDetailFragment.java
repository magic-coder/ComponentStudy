package com.richfit.module_xngd.module_pd;

import android.view.View;

import com.richfit.module_xngd.R;
import com.richfit.module_xngd.module_pd.imp.XNGDDetailPresenterImp;
import com.richfit.sdk_wzpd.checkn.detail.CNDetailFragment;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDCNDetailFragment extends CNDetailFragment<XNGDDetailPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new XNGDDetailPresenterImp(mActivity);
    }
}
