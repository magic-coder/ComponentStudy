package com.richfit.module_zycj.module_ykn;

import android.text.TextUtils;
import android.view.View;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_zycj.R;
import com.richfit.sdk_wzyk.base_msn_collect.BaseMSNCollectFragment;
import com.richfit.sdk_wzyk.base_msn_collect.imp.MSNCollectPresenterImp;

/**
 * Created by monday on 2017/11/3.
 */

public class ZYCJMSNCollectFragment extends BaseMSNCollectFragment<MSNCollectPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }


    @Override
    protected boolean checkHeaderData() {
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先选择工厂");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.recInvId)) {
            showMessage("请先选择接收库位");
            return false;
        }
        return true;
    }

    @Override
    protected boolean getWMOpenFlag() {
        return false;
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (TextUtils.isEmpty(getString(autoRecLoc))) {
            showMessage("请输入接收仓位");
            return false;
        }
        if (TextUtils.isEmpty(getString(etRecBatchFlag))) {
            showMessage("请输入接收批次");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

}
