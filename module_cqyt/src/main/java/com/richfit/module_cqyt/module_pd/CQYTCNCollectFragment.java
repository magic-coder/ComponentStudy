package com.richfit.module_cqyt.module_pd;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzpd.checkn.collect.CNCollectFragment;

/**
 * 增加过滤单据行信息
 * Created by monday on 2017/8/10.
 */

public class CQYTCNCollectFragment extends CNCollectFragment {

    //增加仓储巡检扫描
    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type,list);
        if (list != null && list.length == 2 & !cbSingle.isChecked()) {
            final String location = list[0];
            etCheckLocation.setText("");
            etCheckLocation.setText(location);
            return;
        }
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.locationType = mRefData.locationType;
        return result;
    }

}
