package com.richfit.barcodesystemproduct.upload;

import com.richfit.barcodesystemproduct.upload.imp.CheckUploadPresenterImp;

/**
 * 盘点数据上传页面
 * Created by monday on 2017/4/21.
 */

public class CheckUploadFragment extends BaseUploadFragment<CheckUploadPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new CheckUploadPresenterImp(mActivity);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }
}
