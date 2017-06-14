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
    public void networkConnectError(String retryAction) {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void startAutoRefresh() {

    }

    @Override
    public void setRefreshing(boolean isSuccess, String message) {

    }

    @Override
    public void deleteNodeFail(String message) {

    }

    @Override
    public void showTransferedVisa(String visa) {

    }

    @Override
    public void showInspectionNum(String message) {

    }

    @Override
    public void setTransFlagFail(String message) {

    }

    @Override
    public void setTransFlagsComplete() {

    }
}
