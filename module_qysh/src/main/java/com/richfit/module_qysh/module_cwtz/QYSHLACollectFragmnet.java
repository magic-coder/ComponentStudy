package com.richfit.module_qysh.module_cwtz;

import android.view.View;
import android.widget.TextView;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_cwtz.collect.LACollectFragment;

/**
 *
 * Created by monday on 2017/11/2.
 */

public class QYSHLACollectFragmnet extends LACollectFragment {

    @Override
    public void initView() {
        super.initView();
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if(batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
    }
}
