package com.richfit.module_xngd.module_sxcl;

import android.view.View;
import android.widget.CheckBox;

import com.richfit.module_xngd.R;
import com.richfit.sdk_sxcl.basehead.LocQTHeadFragment;

/**
 * Created by monday on 2017/9/22.
 */

public class XNGDLocHeadFragment extends LocQTHeadFragment {

    //应急物资
    private CheckBox cbInvFlag;

    @Override
    public int getContentId() {
        return R.layout.xngd_framgent_loc_head;
    }


    @Override
    public void initView() {
        super.initView();
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
