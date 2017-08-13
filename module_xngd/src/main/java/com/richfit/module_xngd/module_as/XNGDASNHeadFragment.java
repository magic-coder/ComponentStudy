package com.richfit.module_xngd.module_as;

import android.view.View;

import com.richfit.module_xngd.module_as.imp.XNGDASNHeadPresenterImp;
import com.richfit.sdk_wzrk.base_asn_head.BaseASNHeadFragment;

/**
 * 库存类型和项目编号的逻辑是:
 * 如果库存类型选择项目物资，那么项目编号必输，在数据
 * 采集获取库存的时候，需要加入项目编号作为搜索维度。
 * 2017年08月03日去除库存类型和项目编号
 * 库存类型保存到specialInvFlag
 * Created by monday on 2017/6/23.
 */

public class XNGDASNHeadFragment extends BaseASNHeadFragment<XNGDASNHeadPresenterImp> {


    @Override
    public void initPresenter() {
        mPresenter = new XNGDASNHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void initView() {
        super.initView();
        llSupplier.setVisibility(View.GONE);
    }
}
