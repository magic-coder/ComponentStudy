package com.richfit.module_qhyt.module_ms.ubsto101;

import android.support.annotation.NonNull;
import android.view.View;

import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * 青海移库转储101抬头界面(采购订单)。相当于UbSto101转储接收，业务与有参考入库，注意显示字段
 * Created by monday on 2017/2/15。
 */

public class QHYTUbSto101HeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        //需要显示发出工厂
        llSendWork.setVisibility(View.VISIBLE);
        super.initView();
    }

    @Override
    public void initDataLazily() {

    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "3";
    }

}
