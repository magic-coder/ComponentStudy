package com.richfit.module_qhyt.module_pd;

import com.richfit.sdk_wzpd.checkn.detail.CNDetailFragment;
import com.richfit.sdk_wzpd.checkn.detail.imp.CNDetailPresenterImp;

/**
 * 2017年06月23日为了盘点不上传到SAP需要扩展明细界面
 * Created by monday on 2017/6/23.
 */

public class QHYTCNDetailFragment extends CNDetailFragment<CNDetailPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new CNDetailPresenterImp(mActivity);
    }
}
