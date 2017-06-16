package com.richfit.module_xngd.module_ds.dsn;


import android.widget.Spinner;

import com.richfit.module_xngd.R;
import com.richfit.module_xngd.module_ds.dsn.imp.XNGDDSNPresenterImp;
import com.richfit.sdk_wzck.base_dsn_head.BaseDSNHeadFragment;

/**
 * 西南无参考入库抬头增加客户化字段
 * 移动类型，是否应急，项目移交物资
 * Created by monday on 2017/3/27.
 */

public class XNGDDSNHeadFragment extends BaseDSNHeadFragment<XNGDDSNPresenterImp> {

    private Spinner spMoveType;
//    private CheckBox cb

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_dsn_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new XNGDDSNPresenterImp(mActivity);
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void _onPause() {
        super._onPause();
        if(mRefData != null) {
            //保存抬头的移动类型，是否应急，项目移交物资，
        }
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
