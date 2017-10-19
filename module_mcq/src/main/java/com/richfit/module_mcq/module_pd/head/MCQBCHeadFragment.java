package com.richfit.module_mcq.module_pd.head;

import android.util.Log;
import android.view.View;
import android.widget.Spinner;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_mcq.R;
import com.richfit.sdk_wzpd.blind.head.BCHeadFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //打开仓储类型
        llLocationType.setVisibility(View.VISIBLE);
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            mRefData.storageNum = storageNum;
        }
    }
}
