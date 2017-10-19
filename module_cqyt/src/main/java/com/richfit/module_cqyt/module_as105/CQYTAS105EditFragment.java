package com.richfit.module_cqyt.module_as105;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.module_ms.y313.CQYTMSY313EditFragment;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/3/8.
 */

public class CQYTAS105EditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    EditText etReturnQuantity;
    EditText etMoveCauseDesc;
    Spinner spMoveCause;
    EditText etQuantityCustom;
    List<SimpleEntity> mMoveCauses;

    @Override
    protected int getContentId() {
        return R.layout.cqyt_fragment_as105_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        //退货交货数量
        etReturnQuantity = (EditText) mActivity.findViewById(R.id.et_return_quantity);
        etReturnQuantity.setEnabled(false);
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        //如果输入的退货交货数量，那么移动原因必输，如果退货交货数量没有输入那么移动原因可输可不输
        etMoveCauseDesc = (EditText) mActivity.findViewById(R.id.et_move_cause_desc);
        spMoveCause = (Spinner) mActivity.findViewById(R.id.sp_move_cause);
        //显示仓储类型
        llLocationType.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        String returnQuantity = bundle.getString(Global.EXTRA_RETURN_QUANTITY_KEY);
        String moveCauseDesc = bundle.getString(Global.EXTRA_MOVE_CAUSE_DESC_KEY);
        String quantityCustom = bundle.getString(Global.EXTRA_QUANTITY_CUSTOM_KEY);
        etReturnQuantity.setText(returnQuantity);
        etMoveCauseDesc.setText(moveCauseDesc);
        spMoveCause.setEnabled(false);
        etQuantityCustom.setText(quantityCustom);
        mPresenter.getDictionaryData("moveCause");
        super.initData();
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        super.loadDictionaryDataSuccess(data);
        List<SimpleEntity> moveCauses = data.get("moveCause");
        String moveCause = getArguments().getString(Global.EXTRA_MOVE_CAUSE_KEY);
        if (moveCauses != null && !TextUtils.isEmpty(moveCause)) {
            if (mMoveCauses == null) {
                mMoveCauses = new ArrayList<>();
            }
            mMoveCauses.clear();
            SimpleEntity tmp = new SimpleEntity();
            tmp.name = "请选择";
            mMoveCauses.add(tmp);
            mMoveCauses.addAll(moveCauses);
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, mMoveCauses);
            spMoveCause.setAdapter(adapter);
            UiUtil.setSelectionForSimpleSp(mMoveCauses, moveCause, spMoveCause);
        }
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {

        final String quantityCustom = getString(etQuantityCustom);
        if (TextUtils.isEmpty(quantityCustom)) {
            showMessage("请先输入件数");
            return false;
        }
        if (Float.valueOf(quantityCustom) < 0.0f) {
            showMessage("件数不合理");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        //退货交货数量
        result.returnQuantity = getString(etReturnQuantity);
        //移动原因说明
        result.moveCauseDesc = getString(etMoveCauseDesc);
        //件数
        result.quantityCustom = getString(etQuantityCustom);
        return result;
    }
}
