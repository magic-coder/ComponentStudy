package com.richfit.module_qhyt.module_as.as_ww_component.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditFragment;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qhyt.R;
import com.richfit.module_qhyt.R2;

import butterknife.BindView;

/**
 * Created by monday on 2017/3/13.
 */

public class QHYTASWWCEditFragment extends BaseEditFragment<QHYTASWWCEditPresenterImp>
        implements QHYTASWWCEditContract.IQingHaiWWCEditView {

    @BindView(R2.id.tv_material_num)
    TextView tvMaterialNum;
    @BindView(R2.id.tv_material_desc)
    TextView tvMaterialDesc;
    @BindView(R2.id.tv_special_inv_flag)
    TextView tvSpecialInvFlag;
    @BindView(R2.id.tv_work)
    TextView tvWork;
    @BindView(R2.id.tv_act_quantity)
    TextView tvActQuantity;
    @BindView(R2.id.tv_batch_flag)
    TextView tvBatchFlag;
    @BindView(R2.id.tv_location_quantity)
    TextView tvLocationQuantity;
    @BindView(R2.id.quantity_name)
    TextView quantityName;
    @BindView(R2.id.et_quantity)
    EditText etQuantity;
    @BindView(R2.id.tv_total_quantity)
    TextView tvTotalQuantity;


    int mPosition;
    String mRefLineId;
    String mQuantity;
    String mLocationId;

    @Override
    protected int getContentId() {
        return R.layout.qhyt_fragment_wwc_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new QHYTASWWCEditPresenterImp(mActivity);
    }


    @Override
    public void initData() {
        Bundle bundle = getArguments();
        final String totalQuantity = bundle.getString(Global.EXTRA_TOTAL_QUANTITY_KEY);
        final String batchFlag = bundle.getString(Global.EXTRA_BATCH_FLAG_KEY);

        mPosition = bundle.getInt(Global.EXTRA_POSITION_KEY);
        mQuantity = bundle.getString(Global.EXTRA_QUANTITY_KEY);
        mRefLineId = bundle.getString(Global.EXTRA_REF_LINE_ID_KEY);
        mLocationId = bundle.getString(Global.EXTRA_LOCATION_ID_KEY);

        if (mRefDetail != null && mPosition >= 0) {
            /*单据数据中的库存地点不一定有，而且用户可以录入新的库存地点，所以只有子节点的库存地点才是正确的*/
            final RefDetailEntity lineData = mRefDetail.get(mPosition);
            //拿到上架总数
            tvMaterialNum.setText(lineData.materialNum);

            tvMaterialDesc.setText(lineData.materialDesc);
            tvBatchFlag.setText(batchFlag);
            tvActQuantity.setText(lineData.actQuantity);
            tvLocationQuantity.setText(mQuantity);
            tvSpecialInvFlag.setText("O");
            etQuantity.setText(mQuantity);
            tvLocationQuantity.setText(mQuantity);
            tvTotalQuantity.setText(totalQuantity);
            tvWork.setText(lineData.workCode);

        }
    }

    @Override
    public void initDataLazily() {

    }

    /**
     * 保存修改数据前的检查，注意这里之类必须根据业务检查仓位
     *
     * @return
     */
    @Override
    public boolean checkCollectedDataBeforeSave() {

        if (mRefDetail == null) {
            showMessage("未获取到明细数据");
            return false;
        }

        if (TextUtils.isEmpty(getString(etQuantity))) {
            showMessage("请输入入库数量");
            return false;
        }

        float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        float totalQuantityV = CommonUtil.convertToFloat(getString(tvTotalQuantity), 0.0f);
        float collectedQuantity = CommonUtil.convertToFloat(mQuantity, 0.0f);
        float quantityV = CommonUtil.convertToFloat(getString(etQuantity), 0.0f);
        if (Float.compare(quantityV, 0.0f) <= 0.0f) {
            showMessage("输入数量不合理");
            etQuantity.setText("");
            return false;
        }

        /*lastFlag 委外出库行数量判断标识如果 lastFlag = 'X'  则累计录入数量不能大于 应发数量*/
        RefDetailEntity lineData = mRefDetail.get(mPosition);
        if (lineData != null) {
            if (!"X".equalsIgnoreCase(lineData.lastFlag)) {
                mQuantity = quantityV + "";
                return true;
            }
        }


        float residualQuantity = totalQuantityV - collectedQuantity + quantityV;//减去已经录入的数量
        if (Float.compare(residualQuantity, actQuantityV) > 0.0f) {
            showMessage("输入实收数量有误");
            etQuantity.setText("");
            return false;
        }
        mQuantity = quantityV + "";
        return true;
    }

    @Override
    public ResultEntity provideResult() {
        RefDetailEntity lineData = mRefDetail.get(mPosition);
        ResultEntity result = new ResultEntity();
        result.businessType = mBizType;
        result.refCodeId = mRefData.refCodeId;
        result.refCode = mRefData.recordNum;
        result.refLineNum = lineData.lineNum;
        result.voucherDate = mRefData.voucherDate;
        result.refType = mRefData.refType;
        result.moveType = mRefData.moveType;
        result.userId = Global.USER_ID;
        result.refLineId = lineData.refLineId;
        result.workId = lineData.workId;
        result.materialId = lineData.materialId;
        result.locationId = mLocationId;
        result.location = "barcode";
        result.batchFlag = getString(tvBatchFlag);
        result.quantity = getString(etQuantity);
        result.modifyFlag = "Y";
        result.refDoc = lineData.refDoc;
        result.refDocItem = lineData.refDocItem;
        result.supplierNum = mRefData.supplierNum;
        result.specialInvFlag = getString(tvSpecialInvFlag);
        result.specialInvNum = mRefData.supplierNum;
        return result;
    }


    @Override
    public void saveEditedDataSuccess(String message) {
        super.saveEditedDataSuccess(message);
        tvLocationQuantity.setText(mQuantity);
        tvTotalQuantity.setText(mQuantity);
    }

    /**
     * 获取行明细(这里获取的是界面得到的mRefDetail)
     *
     * @param lineNum:单据行号
     * @return
     */
    protected RefDetailEntity getLineData(String lineNum) {
        final int index = getIndexByLineNum(lineNum);
        return mRefDetail.get(index);
    }

    @Override
    public void retry(String retryAction) {
        switch (retryAction) {
            case Global.RETRY_EDIT_DATA_ACTION:
                saveCollectedData();
                break;
        }
        super.retry(retryAction);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {

    }

    @Override
    public void initEvent() {

    }

    /**
     * 通过单据行的行号得到该行在单据明细列表中的位置
     *
     * @param lineNum:单据行号
     * @return 返回该行号对应的行明细在明细列表的索引
     */
    protected int getIndexByLineNum(String lineNum) {
        int index = -1;
        if (TextUtils.isEmpty(lineNum))
            return index;

        if (mRefData == null || mRefData.billDetailList == null
                || mRefData.billDetailList.size() == 0)
            return index;

        for (RefDetailEntity detailEntity : mRefData.billDetailList) {
            index++;
            if (lineNum.equalsIgnoreCase(detailEntity.lineNum))
                break;

        }
        return index;
    }
}
