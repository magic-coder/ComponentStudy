package com.richfit.module_cqyt.module_as105;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
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
   // EditText etProjectText;
    EditText etMoveCauseDesc;
    //Spinner spStrategyCode;
    Spinner spMoveCause;

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
        //如果输入的退货交货数量，那么移动原因必输，如果退货交货数量没有输入那么移动原因可输可不输
        //etProjectText = (EditText) mActivity.findViewById(R.id.et_project_text);
        etMoveCauseDesc = (EditText) mActivity.findViewById(R.id.et_move_cause_desc);
        spMoveCause = (Spinner) mActivity.findViewById(R.id.sp_move_cause);
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        String returnQuantity = bundle.getString(Global.EXTRA_RETURN_QUANTITY_KEY);
        String projectText = bundle.getString(Global.EXTRA_PROJECT_TEXT_KEY);
        String moveCauseDesc = bundle.getString(Global.EXTRA_MOVE_CAUSE_DESC_KEY);
        etReturnQuantity.setText(returnQuantity);
        //etProjectText.setText(projectText);
        etMoveCauseDesc.setText(moveCauseDesc);
        spMoveCause.setEnabled(false);
        //初始化移动原因
        String moveCause = getArguments().getString(Global.EXTRA_MOVE_CAUSE_KEY);
        if (!TextUtils.isEmpty(moveCause)) {
            mPresenter.getDictionaryData("moveCause");
        }
        super.initData();
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        List<SimpleEntity> moveCauses = data.get("moveCause");
        String moveCause = getArguments().getString(Global.EXTRA_MOVE_CAUSE_KEY);
        if (moveCauses == null || TextUtils.isEmpty(moveCause))
            return;
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


        Log.e("yff", "moveCause = " + moveCause);
        UiUtil.setSelectionForSimpleSp(mMoveCauses, moveCause, spMoveCause);
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        //退货交货数量
        result.returnQuantity = getString(etReturnQuantity);
        //项目文本
       // result.projectText = getString(etProjectText);
        //移动原因说明
        result.moveCauseDesc = getString(etMoveCauseDesc);

        return result;
    }
}
