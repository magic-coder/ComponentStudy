package com.richfit.module_hc.module_pd;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzpd.checkn.collect.CNCollectFragment;

/**
 * 增加过滤单据行信息
 * Created by monday on 2017/8/10.
 */

public class HCCNCollectFragment extends CNCollectFragment {


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.locationType = mRefData.locationType;
        return result;
    }

}
