package com.richfit.module_xngd.module_sxcl;

import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.module_xngd.module_sxcl.imp.XNGDLocDetailPresenterImp;
import com.richfit.sdk_sxcl.basedetail.LocQTDetailFragment;

import java.util.List;

/**
 * Created by monday on 2017/10/18.
 */

public class XNGDLocDetailFragment extends LocQTDetailFragment<XNGDLocDetailPresenterImp>{

    @Override
    public void initPresenter() {
        mPresenter = new XNGDLocDetailPresenterImp(mActivity);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> tmp = super.provideDefaultBottomMenu();
        return tmp.subList(0, 1);
    }
}
