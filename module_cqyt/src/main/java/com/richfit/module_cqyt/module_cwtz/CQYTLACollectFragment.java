package com.richfit.module_cqyt.module_cwtz;

import android.widget.EditText;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_cwtz.collect.LACollectFragment;

/**
 * Created by monday on 2017/7/20.
 */

public class CQYTLACollectFragment extends LACollectFragment {

    EditText etQuantityCustom;

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_la_collect;
    }

    @Override
    public void initView() {
        super.initView();
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.quantityCustom = getString(etQuantityCustom);
        return result;
    }
}
