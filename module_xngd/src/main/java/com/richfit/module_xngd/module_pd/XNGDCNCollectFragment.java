package com.richfit.module_xngd.module_pd;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzpd.checkn.collect.CNCollectFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/10/31.
 */

public class XNGDCNCollectFragment extends CNCollectFragment {

    Spinner spMaterialState;
    EditText etRemark;

    List<String> mMaterialStates;

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_cn_collect;
    }


    @Override
    public void initView() {
        super.initView();
        spMaterialState = mView.findViewById(R.id.xngd_sp_material_state);
        etRemark = mView.findViewById(R.id.et_remark);
    }

    @Override
    public void bindCommonCollectUI() {
        super.bindCommonCollectUI();
        mPresenter.getDictionaryData("materialState");
    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        //获取物资状态成功
        List<SimpleEntity> list = data.get("materialState");
        if (list == null || list.size() <= 0)
            return;
        if (mMaterialStates == null)
            mMaterialStates = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        for (SimpleEntity item : list) {
            strings.add(item.name);
        }
        mMaterialStates.clear();
        mMaterialStates.addAll(strings);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, mMaterialStates);
        spMaterialState.setAdapter(adapter);
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        if (mMaterialStates != null && mMaterialStates.size() > 0)
            result.materialState = mMaterialStates.get(spMaterialState.getSelectedItemPosition());
        result.remark = getString(etRemark);
        return result;
    }

    @Override
    public void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etRemark);
        if (mMaterialStates != null && mMaterialStates.size() > 0) {
            spMaterialState.setSelection(0);
        }
    }
}
