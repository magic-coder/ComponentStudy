package com.richfit.module_xngd.module_pd;

import android.view.View;
import android.widget.LinearLayout;

import com.richfit.module_xngd.R;
import com.richfit.sdk_wzpd.checkn.head.CNHeadFragment;

/**
 * Created by monday on 2017/6/20.
 */

public class XNGDCNHeadFragment extends CNHeadFragment {

    @Override
    protected void initView() {
        super.initView();
        LinearLayout llCheckGuide = mView.findViewById(R.id.ll_check_guide);
        llCheckGuide.setVisibility(View.GONE);
        rbStorageNumLevel.setEnabled(false);
    }
}
