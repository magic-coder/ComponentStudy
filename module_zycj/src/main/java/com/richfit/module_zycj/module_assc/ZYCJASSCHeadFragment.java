package com.richfit.module_zycj.module_assc;

import android.support.annotation.NonNull;
import android.widget.Spinner;

import com.richfit.module_zycj.R;
import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * 生产入库增加产品类型
 * Created by monday on 2018/1/2.
 */

public class ZYCJASSCHeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    Spinner spProductType;

    @Override
    protected int getContentId() {
        return R.layout.zycj_fragment_assc_head;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    protected void initView() {
        super.initView();
        spProductType=mView.findViewById(R.id.zycj_sp_product_type);
    }

    @Override
    protected void initData() {
        super.initData();
        //加载产品类型数据
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }
}
