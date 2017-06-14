package com.richfit.barcodesystemproduct.upload;

import com.richfit.barcodesystemproduct.upload.imp.UploadPresenterImp;

/**
 * 出入库上传页面
 * Created by monday on 2017/4/21.
 */

public class BuziUploadFragment extends BaseUploadFragment<UploadPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new UploadPresenterImp(mActivity);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        startAutoRefresh();
    }
}
