package com.richfit.module_xngd.module_ms.ubsto;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;

import com.richfit.module_xngd.R;
import com.richfit.sdk_wzyk.base_ms_head.BaseMSHeadFragment;
import com.richfit.sdk_wzyk.base_ms_head.imp.MSHeadPresenterImp;

/**
 * Created by monday on 2017/7/20.
 */

public class XNGDMSYHeadFragment extends BaseMSHeadFragment<MSHeadPresenterImp> {

    CheckBox cbInvFlag;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_msy_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MSHeadPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        super.initView();
        cbInvFlag = (CheckBox) mView.findViewById(R.id.xngd_cb_inv_flag);
        //隐藏接收库位
        llInv.setVisibility(View.GONE);
        //隐藏接收工厂
        llRecWork.setVisibility(View.GONE);
        llSendWork.setVisibility(View.VISIBLE);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void _onPause() {
        super._onPause();
        if(mRefData  != null ) {
            mRefData.invFlag = cbInvFlag.isChecked() ? "1" : "0";
        }
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "3";
    }
}
