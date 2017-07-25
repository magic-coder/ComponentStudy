package com.richfit.module_xngd.module_ms.ubsto;

import android.support.annotation.NonNull;
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
        cbInvFlag = (CheckBox) mView.findViewById(R.id.xngd_cb_inv_flag);
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
