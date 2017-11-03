package com.richfit.module_cqyt.module_cwtz;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_cwtz.collect.LACollectFragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
