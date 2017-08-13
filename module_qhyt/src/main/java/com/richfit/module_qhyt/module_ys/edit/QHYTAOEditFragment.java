package com.richfit.module_qhyt.module_ys.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter.InvAdapter;
import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditFragment;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qhyt.R;
import com.richfit.module_qhyt.R2;
import com.richfit.module_qhyt.module_ys.edit.imp.QHYTAOEditPresenterImp;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.richfit.module_qhyt.module_ys.collect.QHYTAOCollectFragment.DEFUALT_CHOOSED_FLAG;


/**
 * Created by monday on 2017/3/1.
 */

public class QHYTAOEditFragment extends BaseEditFragment<QHYTAOEditPresenterImp>
        implements IQHYTAOEditView {

    @BindView(R2.id.tv_order_quantity)
    TextView tvOrderQuantity;
    @BindView(R2.id.tv_act_quantity)
    TextView tvActQuantity;
    @BindView(R2.id.sp_inv)
    Spinner spInv;
    @BindView(R2.id.et_quantity)
    EditText etQuantity;
    @BindView(R2.id.et_manufacturer)
    EditText etManufacturer;
    @BindView(R2.id.cb_certificate)
    CheckBox cbCertificate;
    @BindView(R2.id.cb_instructions)
    CheckBox cbInstructions;
    @BindView(R2.id.cb_qm_certificate)
    CheckBox cbQmCertificate;
    @BindView(R2.id.et_sample_quantity)
    EditText etSampleQuantity;
    @BindView(R2.id.et_qualified_quantity)
    EditText etQualifiedQuantity;
    @BindView(R2.id.et_rust_quantity)
    EditText etRustQuantity;
    @BindView(R2.id.et_damaged_quantity)
    EditText etDamagedQuantity;
    @BindView(R2.id.et_bad_quantity)
    EditText etBadQuantity;
    @BindView(R2.id.et_other_quantity)
    EditText etOtherQuantity;
    @BindView(R2.id.sp_sap_package)
    Spinner spSapPackage;
    @BindView(R2.id.sp_inspection_result)
    Spinner spInspectionResult;
    @BindView(R2.id.et_inspection_quantity)
    EditText etInspectionQuantity;
    @BindView(R2.id.et_qm_num)
    EditText etQmNum;
    @BindView(R2.id.et_claim_num)
    EditText etClaimNum;
    @BindView(R2.id.et_remark)
    EditText etRemark;

    int mPosition = -1;

    List<InvEntity> mInvs;


    @Override
    protected int getContentId() {
        return R.layout.qhyt_fragment_ao_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new QHYTAOEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        mInvs = new ArrayList<>();
    }

    @Override
    protected void initView() {

    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        //初始化包装结果以及验收结果
        ArrayAdapter<String> pakageConditionAdapter = new ArrayAdapter<>(mActivity,
                android.R.layout.simple_spinner_item, getStringArray(R.array.qhyt_package_conditions));
        ArrayAdapter<String> inspectionResultAdapter = new ArrayAdapter<>(mActivity,
                android.R.layout.simple_spinner_item, getStringArray(R.array.qhyt_inspection_results));
        spSapPackage.setAdapter(pakageConditionAdapter);
        spInspectionResult.setAdapter(inspectionResultAdapter);

        Bundle bundle = getArguments();
        if (bundle == null) {
            showMessage("未获取到需要修改的数据");
            return;
        }
        final String totalQuantity = bundle.getString(Global.EXTRA_QUANTITY_KEY);
        final String manufacturer = bundle.getString(Global.EXTRA_MANUFUCTURER_KEY);
        //抽检数量
        final String randomQuantity = bundle.getString(Global.EXTRA_SAMPLE_QUANTITY_KEY);
        //完好数量
        final String qualifiedQuantity = bundle.getString(Global.EXTRA_QUALIFIED_QUANTITY_KEY);
        //损坏数量
        final String damagedQuantity = bundle.getString(Global.EXTRA_DAMAGED_QUANTITY_KEY);
        //送检数量
        final String inspectionQuantity = bundle.getString(Global.EXTRA_INSPECTION_QUANTITY_KEY);
        //锈蚀数量
        final String rustQuantity = bundle.getString(Global.EXTRA_RUST_QUANTITY_KEY);
        //变质
        final String badQuantity = bundle.getString(Global.EXTRA_BAD_QUANTITY_KEY);
        //其他数量
        final String otherQuantity = bundle.getString(Global.EXTRA_OTHER_QUANTITY_KEY);
        //包装情况
        final String sapPackage = bundle.getString(Global.EXTRA_PACKAGE_KEY);
        //质检单号
        final String qmNum = bundle.getString(Global.EXTRA_QM_NUM_KEY);
        //索赔单号
        final String claimNum = bundle.getString(Global.EXTRA_CLAIM_NUM_KEY);
        //合格证
        final String certificate = bundle.getString(Global.EXTRA_CERTIFICATE_KEY);
        //说明书
        final String instructions = bundle.getString(Global.EXTRA_INSTRUCTIONS_KEY);
        //质检证书
        final String qmCertificate = bundle.getString(Global.EXTRA_QM_CERTIFICATE_KEY);
        //检验结果
        final String inspectionResult = bundle.getString(Global.EXTRA_INSPECTION_RESULT_KEY);
        //备注
        final String remark = bundle.getString(Global.EXTRA_REMARK_KEY);

        mPosition = bundle.getInt(Global.EXTRA_POSITION_KEY);

        if (mRefData != null) {
            /*单据数据中的库存地点不一定有，而且用户可以录入新的库存地点，所以只有子节点的库存地点才是正确的*/
            final RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
            //单据数量
            tvOrderQuantity.setText(lineData.orderQuantity);
            //应收数量
            tvActQuantity.setText(lineData.actQuantity);
            //实收数量
            etQuantity.setText(totalQuantity);
            //制造商
            etManufacturer.setText(manufacturer);
            //抽检数
            etSampleQuantity.setText(randomQuantity);
            //完好数量
            etQualifiedQuantity.setText(qualifiedQuantity);
            //损坏数量
            etDamagedQuantity.setText(damagedQuantity);
            //送检数量
            etInspectionQuantity.setText(inspectionQuantity);
            //锈蚀数量
            etRustQuantity.setText(rustQuantity);
            //变质
            etBadQuantity.setText(badQuantity);
            //其他数量
            etOtherQuantity.setText(otherQuantity);
            //包装情况
            spSapPackage.setSelection(CommonUtil.convertToInt(sapPackage, 1) - 1);
            //质检单号
            etQmNum.setText(qmNum);
            //索赔单号
            etClaimNum.setText(claimNum);
            //合格证
            cbCertificate.setChecked(DEFUALT_CHOOSED_FLAG.equalsIgnoreCase(certificate) ? true : false);
            //说明书
            cbInstructions.setChecked(DEFUALT_CHOOSED_FLAG.equalsIgnoreCase(instructions) ? true : false);
            //质检证书
            cbQmCertificate.setChecked(DEFUALT_CHOOSED_FLAG.equalsIgnoreCase(qmCertificate) ? true : false);
            //检验结果
            spInspectionResult.setSelection("01".equalsIgnoreCase(inspectionResult) ? 0 : 1);
            etRemark.setText(remark);
            /*获取库存地点列表*/
            mPresenter.getInvsByWorkId(lineData.workId, 0);
        }
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void showInvs(List<InvEntity> invs) {
        mInvs.clear();
        mInvs.addAll(invs);
        InvAdapter adapter = new InvAdapter(mActivity, android.R.layout.simple_list_item_1,
                mInvs);
        spInv.setAdapter(adapter);
        //默认选择
        Bundle bundle = getArguments();
        final String invCode = bundle.getString(Global.EXTRA_INV_CODE_KEY);
        if (TextUtils.isEmpty(invCode)) {
            spInv.setSelection(0);
            return;
        }
        int pos = -1;
        for (InvEntity item : mInvs) {
            pos++;
            if (invCode.equalsIgnoreCase(item.invCode)) {
                break;
            }
        }
        spInv.setSelection(pos == -1 ? 0 : pos);
    }

    @Override
    public void loadInvsFail(String message) {
        showMessage(message);
        mInvs.clear();
        if (spInv.getAdapter() != null) {
            InvAdapter adapter = (InvAdapter) spInv.getAdapter();
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 检查用户输入的实收数量是否合理
     *
     * @param quantity:实收数量
     * @param sampleQuantity:抽检数量
     * @return
     */
    private boolean refreshQuantity(final String quantity, final String sampleQuantity) {
        //实收数量
        final float quantityV = CommonUtil.convertToFloat(quantity, 0.0f);

        if (Float.compare(quantityV, 0.0f) <= 0.0f) {
            showMessage("输入实收数量有误,请出现输入");
            etQuantity.setText("");
            return false;
        }
        //抽检数量
        final float sampleQuantityV = CommonUtil.convertToFloat(sampleQuantity, 0.0f);
        if (Float.compare(sampleQuantityV, quantityV) > 0.0f) {
            showMessage("抽检数量不能大于实收数量");
            return false;
        }
        //完好数量
        final float qualifiedQuantityV = CommonUtil.convertToFloat(getString(etQualifiedQuantity), 0.0f);
        //腐蚀
        final float rustCorrosionQuantityV = CommonUtil.convertToFloat(getString(etRustQuantity), 0.0f);
        //损坏
        final float damagedQuantity = CommonUtil.convertToFloat(getString(etDamagedQuantity), 0.0f);
        //变质
        final float badQuantityV = CommonUtil.convertToFloat(getString(etBadQuantity), 0.0f);
        //其他质量问题
        final float otherQuantityV = CommonUtil.convertToFloat(getString(etOtherQuantity), 0.0f);

        if (Float.compare(qualifiedQuantityV + rustCorrosionQuantityV + damagedQuantity + badQuantityV
                + otherQuantityV, quantityV) > 0.0f) {
            showMessage("完好数量,锈蚀,损坏,变质,其他质量问题数量之和大于实收数量");
            return false;
        }
        return true;
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {

        if (mRefData == null || mRefData.billDetailList == null || mRefData.billDetailList.size() == 0) {
            showMessage("请先获取单据数据");
            return false;
        }

        if (mPosition < 0 || mPosition > mRefData.billDetailList.size() - 1) {
            showMessage("您当前需要修改的明细不合理");
            return false;
        }

        if (!refreshQuantity(getString(etQuantity), getString(etSampleQuantity))) {
            return false;
        }
        return true;
    }


    @Override
    public ResultEntity provideResult() {
        RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        ResultEntity result = new ResultEntity();
        result.refCodeId = mRefData.refCodeId;
        result.refLineId = lineData.refLineId;
        result.refLineNum = lineData.lineNum;
        result.materialId = lineData.materialId;
        result.businessType = mRefData.bizType;
        result.companyCode = Global.COMPANY_CODE;
        result.refType = mRefData.refType;
        result.moveType = mRefData.moveType;
        result.inspectionType = mRefData.inspectionType;
        result.inspectionPerson = Global.USER_ID;
        result.workId = lineData.workId;
        result.userId = Global.USER_ID;
        result.invId = mInvs.get(spInv.getSelectedItemPosition()).invId;
        //制造商
        result.manufacturer = getString(etManufacturer);
        //实收数量
        result.quantity = getString(etQuantity);
        //抽检数量
        result.randomQuantity = getString(etSampleQuantity);
        //完好数量
        result.qualifiedQuantity = getString(etQualifiedQuantity);
        //损坏数量
        result.damagedQuantity = getString(etDamagedQuantity);
        //送检数量
        result.inspectionQuantity = getString(etInspectionQuantity);
        //锈蚀数量
        result.rustQuantity = getString(etRustQuantity);
        //变质
        result.badQuantity = getString(etBadQuantity);
        //其他数量
        result.otherQuantity = getString(etOtherQuantity);
        //包装情况
        result.sapPackage = String.valueOf(spSapPackage.getSelectedItemPosition() + 1);
        //质检单号
        result.qmNum = getString(etQmNum);
        //索赔单号
        result.claimNum = getString(etClaimNum);
        //合格证
        result.certificate = cbCertificate.isChecked() ? "X" : "";
        //说明书
        result.instructions = cbInstructions.isChecked() ? "X" : "";
        //质检证书
        result.qmCertificate = cbQmCertificate.isChecked() ? "X" : "";
        //检验结果
        result.inspectionResult = spInspectionResult.getSelectedItemPosition() == 0 ? "01" : "02";
        result.modifyFlag = "Y";
        result.remark = getString(etRemark);
        return result;
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        super.saveEditedDataSuccess(message);
        showMessage("数据修改成功");
    }
}
