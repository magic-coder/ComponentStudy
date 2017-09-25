package com.richfit.module_xngd.module_as;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.richfit.module_xngd.R;
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

    //应急物资
    private CheckBox cbInvFlag;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_asn_head;
    }

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
        cbInvFlag = (CheckBox) mView.findViewById(R.id.xngd_cb_inv_flag);
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            //保存抬头的移动类型，是否应急，项目移交物资，
            mRefData.invFlag = cbInvFlag.isChecked() ? "1" : "0";
        }
    }
}
