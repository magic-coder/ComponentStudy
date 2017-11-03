package com.richfit.module_qysh.module_as.as103;

import android.os.Bundle;
import android.view.View;

import com.richfit.module_qysh.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHAS103EditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    @Override
    protected void initVariable(Bundle savedInstanceState) {
        super.initVariable(savedInstanceState);
        //不上架，不能输入仓位，单条缓存由库存地点触发
        isNLocation = true;
        //强制不打开仓储类型
        isOpenLocationType = false;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    protected void initView() {
        super.initView();
        //没有库存地点
        View inv = mView.findViewById(R.id.ll_inv);
        if(inv != null) {
            inv.setVisibility(View.GONE);
        }
        //没有批次
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if(batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
        //没有上架仓位
        llLocation.setVisibility(View.GONE);
        //没有仓位数量
        llLocationQuantity.setVisibility(View.GONE);
    }
}
