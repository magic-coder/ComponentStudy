package com.richfit.module_cqyt.module_pd;

import android.view.View;
import android.widget.LinearLayout;

import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzpd.checkn.head.CNHeadFragment;

/**
 * Created by monday on 2017/8/7.
 */

public class CQYTCNHeadFragment extends CNHeadFragment {

    @Override
    public void initView() {
        super.initView();
        LinearLayout llCheckGuide = mView.findViewById(R.id.ll_check_guide);
        llCheckGuide.setVisibility(View.GONE);
    }


    @Override
    public void initData() {
        super.initData();
        resetUI();
        mRefData = null;
        rbStorageNumLevel.setChecked(true);
        llStorageNumLevel.setVisibility(View.VISIBLE);
        if (spStorageNum.getAdapter() == null) {
            mPresenter.getStorageNums(0);
        }
    }

}
