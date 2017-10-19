package com.richfit.module_hc.module_pd;

import android.view.View;
import com.richfit.sdk_wzpd.checkn.head.CNHeadFragment;

/**
 * Created by monday on 2017/8/7.
 */

public class HCCNHeadFragment extends CNHeadFragment {

    @Override
    public void initView() {
        super.initView();
        //打开仓储类型
        llLocationType.setVisibility(View.VISIBLE);
    }
}
