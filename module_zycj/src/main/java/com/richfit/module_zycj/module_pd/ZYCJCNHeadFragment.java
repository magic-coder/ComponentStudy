package com.richfit.module_zycj.module_pd;

import android.view.View;
import android.widget.LinearLayout;

import com.richfit.module_zycj.R;
import com.richfit.sdk_wzpd.checkn.head.CNHeadFragment;

/**
 * Created by monday on 2017/8/7.
 */

public class ZYCJCNHeadFragment extends CNHeadFragment {

    @Override
    public void initView() {
        super.initView();
        LinearLayout llCheckGuide = mView.findViewById(R.id.ll_check_guide);
        llCheckGuide.setVisibility(View.GONE);
    }

}
