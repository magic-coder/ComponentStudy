package com.richfit.module_xngd.module_pd;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzpd.checkn.edit.CNEditFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/10/31.
 */

public class XNGDCNEditFragment extends CNEditFragment {


    public static final String EXTRA_MATERIAL_STATE_KEY = "extra_material_state";

    Spinner spMaterialState;
    EditText etRemark;

    List<String> mMaterialStates;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_cn_edit;
    }

    @Override
    public void initView() {
        super.initView();
        spMaterialState = mView.findViewById(R.id.xngd_sp_material_state);
        etRemark = mView.findViewById(R.id.et_remark);
    }

    @Override
    public void initData() {
        super.initData();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String remark = bundle.getString(Global.EXTRA_REMARK_KEY);
            etRemark.setText(remark);
        }
        mPresenter.getDictionaryData("materialState");
    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        super.loadDictionaryDataSuccess(data);
        List<SimpleEntity> list = data.get("materialState");
        if (list == null || list.size() <= 0)
            return;
        if (mMaterialStates == null)
            mMaterialStates = new ArrayList<>();
        List<String> strings = CommonUtil.toStringArray(list, false);
        mMaterialStates.clear();
        mMaterialStates.addAll(strings);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, mMaterialStates);
        spMaterialState.setAdapter(adapter);
        //自动选择
        Bundle bundle = getArguments();
        if (bundle != null) {
            String materialState = bundle.getString(EXTRA_MATERIAL_STATE_KEY);
            UiUtil.setSelectionForSp(mMaterialStates, materialState, spMaterialState);
        }
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        if (mMaterialStates != null && mMaterialStates.size() > 0)
            result.materialState = mMaterialStates.get(spMaterialState.getSelectedItemPosition());
        result.remark = getString(etRemark);
        return result;
    }
}
