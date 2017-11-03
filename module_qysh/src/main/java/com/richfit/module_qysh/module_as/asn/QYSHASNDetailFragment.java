package com.richfit.module_qysh.module_as.asn;

import android.view.View;

import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzrk.base_asn_detail.BaseASNDetailFragment;
import com.richfit.sdk_wzrk.base_asn_detail.imp.ASNDetailPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHASNDetailFragment extends BaseASNDetailFragment<ASNDetailPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new ASNDetailPresenterImp(mActivity);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        super.initView();
        //隐藏批次
        View batchFlag = mView.findViewById(R.id.batchFlag);
        if(batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = super.provideDefaultBottomMenu();
        menus.get(0).transToSapFlag = "01";
        menus.get(1).transToSapFlag = "03";
        return menus;
    }

}
