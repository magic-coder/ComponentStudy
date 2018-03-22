package com.richfit.module_cq.module_pd;

import android.view.View;
import android.widget.LinearLayout;

import com.richfit.module_cq.R;
import com.richfit.sdk_wzpd.checkn.head.CNHeadFragment;

/**
 * Created by monday on 2017/8/7.
 */

public class CQCNHeadFragment extends CNHeadFragment {

    @Override
    public void initView() {
        super.initView();
        LinearLayout llCheckGuide = mView.findViewById(R.id.ll_check_guide);
        llCheckGuide.setVisibility(View.GONE);
    }


/*    @Override
    public void initData() {
        resetUI();
        super.initData();
        mRefData = null;
        rbStorageNumLevel.setChecked(true);
        llStorageNumLevel.setVisibility(View.VISIBLE);
        if (spStorageNum.getAdapter() == null) {
            mPresenter.getStorageNums(0);
        }
    }*/
}
