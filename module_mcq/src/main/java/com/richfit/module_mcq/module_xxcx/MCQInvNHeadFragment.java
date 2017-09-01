package com.richfit.module_mcq.module_xxcx;

import android.view.View;
import android.widget.EditText;

import com.richfit.module_mcq.R;
import com.richfit.sdk_xxcx.inventory_query_n.header.InvNQueryHeaderFragment;

/**
 * Created by monday on 2017/8/28.
 */

public class MCQInvNHeadFragment extends InvNQueryHeaderFragment{

    //仓位
    EditText etLocation;

    @Override
    public int getContentId() {
        return R.layout.mcq_fragment_invn_head;
    }


    @Override
    public void initView() {
        llMaterialClass.setVisibility(View.GONE);
        llMaterialDesc.setVisibility(View.GONE);
        etLocation = mView.findViewById(R.id.et_location);
    }


    @Override
    public void _onPause() {
        super._onPause();
        if(mRefData != null) {
            mRefData.location = getString(etLocation);
        }
    }

}
