package com.richfit.module_cq.module_ms_315;

import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_cq.module_ms_315.imp.CQMSY315DetailPresenterImp;
import com.richfit.sdk_wzrk.base_as_detail.BaseASDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 注意工厂和库存地点使用的workCode,和invCode,不涉及到接收
 * Created by monday on 2017/6/30.
 */

public class CQMSY315DetailFragment extends BaseASDetailFragment<CQMSY315DetailPresenterImp> {



    @Override
    public void initPresenter() {
        mPresenter = new CQMSY315DetailPresenterImp(mActivity);
    }



    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {

    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        tmp.get(0).transToSapFlag = "01";
        ArrayList menus = new ArrayList();
        menus.add(tmp.get(0));
        return menus;
    }

    @Override
    protected String getSubFunName() {
        return "315转储接收";
    }
}
