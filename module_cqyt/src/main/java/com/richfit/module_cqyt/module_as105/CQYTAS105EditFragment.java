package com.richfit.module_cqyt.module_as105;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

/**
 * Created by monday on 2017/3/8.
 */

public class CQYTAS105EditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    EditText etReturnQuantity;
    EditText etProjectText;
    EditText etMoveCauseDesc;
    Spinner spStrategyCode;
    Spinner spMoveReason;

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
        //如果输入的退货交货数量，那么移动原因必输，如果退货交货数量没有输入那么移动原因可输可不输
        etProjectText = (EditText) mActivity.findViewById(R.id.et_project_text);
        etMoveCauseDesc = (EditText) mActivity.findViewById(R.id.et_move_cause_desc);
        spStrategyCode = (Spinner) mActivity.findViewById(R.id.sp_strategy_code);
        spMoveReason = (Spinner) mActivity.findViewById(R.id.sp_move_cause);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        String returnQuantity = bundle.getString(Global.EXTRA_RETURN_QUANTITY_KEY);
        String projectText = bundle.getString(Global.EXTRA_PROJECT_TEXT_KEY);
        String moveCauseDesc = bundle.getString(Global.EXTRA_MOVE_CAUSE_DESC_KEY);
        String moveCause = bundle.getString(Global.EXTRA_MOVE_CAUSE_KEY);
        String decisionCode = bundle.getString(Global.EXTRA_DECISION_CAUSE_KEY);
        etReturnQuantity.setText(returnQuantity);
        etProjectText.setText(projectText);
        etMoveCauseDesc.setText(moveCauseDesc);
        //注意实发数量不能修改
        etQuantity.setEnabled(false);
        if (spStrategyCode != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp,
                    getStringArray(R.array.cqyt_strategy_codes));
            spStrategyCode.setAdapter(adapter);
        }
        if (spMoveReason != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp,
                    getStringArray(R.array.cqyt_move_reasons));
            spMoveReason.setAdapter(adapter);
        }
        spStrategyCode.setEnabled(false);
        spMoveReason.setEnabled(false);
        UiUtil.setSelectionForSp(getStringArray(R.array.cqyt_strategy_codes), decisionCode, spStrategyCode);
        UiUtil.setSelectionForSp(getStringArray(R.array.cqyt_move_reasons), moveCause, spMoveReason);

        super.initData();
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        //退货交货数量
        result.returnQuantity = getString(etReturnQuantity);
        //项目文本
        result.projectText = getString(etProjectText);
        //移动原因说明
        result.moveCauseDesc = getString(etMoveCauseDesc);
        return result;
    }
}
