package com.richfit.module_xngd.module_rg;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Spinner;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/6/20.
 */

public class XNGDRGEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {

    public static final String EXTRA_MOVE_CAUSE_KEY = "extra_move_cause";
    private Spinner spMoveCauses;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_rg_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSEditPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        spMoveCauses = mView.findViewById(R.id.xngd_sp_move_causes);
    }

    @Override
    public void initData() {
        super.initData();
        //初始化退货原因
        List<String> moveCauses = getStringArray(R.array.xngd_move_causes);
        List<String> moveCauseCodes = getStringArray(R.array.xngd_move_cause_codes);
        List<SimpleEntity> items = new ArrayList<>();
        for (int i = 0; i < moveCauseCodes.size(); i++) {
            SimpleEntity item = new SimpleEntity();
            item.name = moveCauses.get(i);
            item.code = moveCauseCodes.get(i);
            items.add(item);
        }

        SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, items);
        spMoveCauses.setAdapter(adapter);
        String moveCauseCode = getArguments().getString(EXTRA_MOVE_CAUSE_KEY);
        Log.e("yff","moveCauseCode = " + moveCauseCode);
        UiUtil.setSelectionForSimpleSp(items,moveCauseCode,spMoveCauses);
    }


    @Override
    public void initDataLazily() {

    }

    @Override
    public ResultEntity provideResult() {
        final ResultEntity result = super.provideResult();
        if(spMoveCauses.getSelectedItemPosition() > 0) {
            result.moveCause = ((SimpleEntity)spMoveCauses.getSelectedItem()).code;
        }
        RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        result.invFlag = lineData.invFlag;
        return result;
    }

    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        InventoryQueryParam param = super.provideInventoryQueryParam();
        param.queryType = "03";
        param.invType = TextUtils.isEmpty(mRefData.invType) ? "1" : mRefData.invType;
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", lineData.invFlag);
        param.extraMap = extraMap;
        return param;
    }
}
