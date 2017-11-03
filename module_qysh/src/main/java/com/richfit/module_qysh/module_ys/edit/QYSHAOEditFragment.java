package com.richfit.module_qysh.module_ys.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditFragment;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qysh.R;
import com.richfit.module_qysh.R2;
import com.richfit.module_qysh.module_ys.edit.imp.QYSHAOEditPresenterImp;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by monday on 2016/11/29.
 */

public class QYSHAOEditFragment extends BaseEditFragment<QYSHAOEditPresenterImp>
        implements IQYSHAOEditView {

    @BindView(R2.id.tv_ref_line_num)
    TextView tvRefLineNum;
    @BindView(R2.id.tv_material_num)
    TextView tvMaterialNum;
    @BindView(R2.id.tv_material_desc)
    TextView tvMaterialDesc;
    @BindView(R2.id.tv_work)
    TextView tvWork;
    @BindView(R2.id.tv_inv)
    TextView tvInv;
    @BindView(R2.id.tv_order_quantity)
    TextView tvOrderQuantity;
    @BindView(R2.id.et_quantity)
    EditText etQuantity;
    @BindView(R2.id.qysh_tv_balance_quantity)
    TextView tvBalanceQuantity;

    String mCompanyCode;
    String mRefLineId;
    //要修改的明细索引
    int mPosition;


    @Override
    protected int getContentId() {
        return R.layout.qysh_fragment_ao_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new QYSHAOEditPresenterImp(mActivity);
    }


    @Override
    protected void initView() {

    }

    @Override
    public void initEvent() {
        //监听到货数量
        RxTextView.textChanges(etQuantity)
                .debounce(500, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .filter(str -> !TextUtils.isEmpty(str) && str.length() > 0)
                .subscribe(str -> tvBalanceQuantity.setText(str));
    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        mPosition = bundle.getInt(Global.EXTRA_POSITION_KEY);
        mRefLineId = bundle.getString(Global.EXTRA_REF_LINE_ID_KEY);
        mCompanyCode = bundle.getString(Global.EXTRA_COMPANY_CODE_KEY);
        final String invId = bundle.getString(Global.EXTRA_INV_ID_KEY);
        final String invCode = bundle.getString(Global.EXTRA_INV_CODE_KEY);
        final String quantity = bundle.getString(Global.EXTRA_TOTAL_QUANTITY_KEY);

        if (mRefData != null) {
            /*单据数据中的库存地点不一定有，而且用户可以录入新的库存地点，所以只有子节点的库存地点才是正确的*/
            final RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
            //拿到上架总数
            tvRefLineNum.setText(lineData.lineNum);
            tvMaterialNum.setText(lineData.materialNum);
            tvMaterialDesc.setText(lineData.materialDesc);
            tvWork.setText(lineData.workCode);
            tvInv.setText(invCode);
            tvInv.setTag(invId);
            tvOrderQuantity.setText(lineData.orderQuantity);
            etQuantity.setText(quantity);
            tvBalanceQuantity.setText(quantity);
        }
    }

    @Override
    public void initDataLazily() {

    }


    @Override
    public ResultEntity provideResult() {
        RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        ResultEntity result = new ResultEntity();
        result.refCodeId = mRefData.refCodeId;
        result.refLineId = lineData.refLineId;
        result.businessType = mRefData.bizType;
        result.refType = mRefData.refType;
        result.moveType = mRefData.moveType;
        result.inspectionPerson = Global.USER_ID;
        result.companyCode = Global.COMPANY_CODE;
        result.userId = Global.USER_ID;
        result.invId = tvInv.getTag() != null ? tvInv.getTag().toString() : "";
        result.modifyFlag = "Y";
        result.quantity = getString(etQuantity);
        return result;
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        super.saveEditedDataSuccess(message);
        showMessage("修改成功");
    }

}
