package com.richfit.module_mcq.module_pd.head;

import android.view.View;

import com.richfit.sdk_wzpd.blind.head.BCHeadFragment;

/**
 * 库存级盘点。默认仓库号写死为1Q0
 * Created by monday on 2017/8/29.
 */

public class MCQBCHeadFragment extends BCHeadFragment {

    private final String storageNum = "1Q0";

    @Override
    public void initView() {
        resetUI();
        super.initView();
        mRefData = null;
        rbWarehouseLevel.setChecked(true);
        llWarehouseLevel.setVisibility(View.VISIBLE);
        llCheckGuide.setVisibility(View.GONE);
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            mRefData.storageNum = storageNum;
        }
    }
}
