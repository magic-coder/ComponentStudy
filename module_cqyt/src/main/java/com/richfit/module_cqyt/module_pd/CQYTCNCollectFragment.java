package com.richfit.module_cqyt.module_pd;

import android.text.TextUtils;

import com.richfit.sdk_wzpd.checkn.collect.CNCollectFragment;

import java.util.ArrayList;

/**
 * 增加过滤单据行信息
 * Created by monday on 2017/8/10.
 */

public class CQYTCNCollectFragment extends CNCollectFragment {

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        mLineNumForFilter = list[list.length - 1];
        super.handleBarCodeScanResult(type, list);
    }

    /**
     * 设置单据行信息之前，过滤掉
     *
     * @param refLines
     */
    @Override
    public void setupRefLineAdapter(ArrayList<String> refLines) {
        if (!TextUtils.isEmpty(mLineNumForFilter)) {
            //过滤掉重复行号
            ArrayList<String> lines = new ArrayList<>();
            for (String refLine : refLines) {
                if (refLine.equalsIgnoreCase(mLineNumForFilter)) {
                    lines.add(refLine);
                }
            }
            if (lines.size() == 0) {
                showMessage("未获取到条码的单据行信息");
            }
            super.setupRefLineAdapter(lines);
            return;
        }
        //如果单据中没有过滤行信息那么直接显示所有的行信息
        super.setupRefLineAdapter(refLines);
    }
}
