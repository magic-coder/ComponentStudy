package com.richfit.module_qhyt.module_as.as105;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qhyt.R;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by monday on 2017/3/8.
 */

public class QHYTAS105EditFragment extends BaseASEditFragment<ASEditPresenterImp> {

    EditText etReturnQuantity;
    EditText etProjectText;
    EditText etMoveCauseDesc;
    Spinner spStrategyCode;
    Spinner spMoveReason;

    @Override
    protected int getContentId() {
        return R.layout.qhyt_fragment_as105_edit;
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
                    getStringArray(R.array.qhyt_strategy_codes));
            spStrategyCode.setAdapter(adapter);
        }
        if (spMoveReason != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp,
                    getStringArray(R.array.qhyt_move_reasons));
            spMoveReason.setAdapter(adapter);
        }
        spStrategyCode.setEnabled(false);
        spMoveReason.setEnabled(false);
        UiUtil.setSelectionForSp(getStringArray(R.array.qhyt_strategy_codes), decisionCode, spStrategyCode);
        UiUtil.setSelectionForSp(getStringArray(R.array.qhyt_move_reasons), moveCause, spMoveReason);

        super.initData();
    }

    @Override
    public void initDataLazily() {

    }


    @Override
    public void saveCollectedData() {
        if (!checkCollectedDataBeforeSave()) {
            return;
        }
        Flowable.create((FlowableOnSubscribe<ResultEntity>) emitter -> {
            RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
            ResultEntity result = new ResultEntity();
            InventoryQueryParam param = provideInventoryQueryParam();
            result.invType = param.invType;
            result.businessType = mRefData.bizType;
            result.refCodeId = mRefData.refCodeId;
            result.voucherDate = mRefData.voucherDate;
            result.refType = mRefData.refType;
            result.moveType = mRefData.moveType;
            result.userId = Global.USER_ID;
            result.refLineId = lineData.refLineId;
            result.workId = lineData.workId;
            result.invId = CommonUtil.Obj2String(tvInv.getTag());
            result.locationId = mLocationId;
            result.materialId = lineData.materialId;
            result.location = getString(etLocation);
            result.batchFlag = getString(tvBatchFlag);
            result.quantity = getString(etQuantity);
            //物料凭证
            result.refDoc = lineData.refDoc;
            //物料凭证单据行
            result.refDocItem = lineData.refDocItem;
            //退货交货数量
            result.returnQuantity = getString(etReturnQuantity);
            //检验批数量
            result.insLot = lineData.insLot;
            //项目文本
            result.projectText = getString(etProjectText);
            //移动原因说明
            result.moveCauseDesc = getString(etMoveCauseDesc);
            result.unit = TextUtils.isEmpty(lineData.recordUnit) ? lineData.materialUnit : lineData.recordUnit;
            result.unitRate = Float.compare(lineData.unitRate, 0.0f) == 0 ? 1.f : 0.f;
            result.modifyFlag = "Y";
            emitter.onNext(result);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER).compose(TransformerHelper.io2main())
                .subscribe(result -> mPresenter.uploadCollectionDataSingle(result));
    }
}
