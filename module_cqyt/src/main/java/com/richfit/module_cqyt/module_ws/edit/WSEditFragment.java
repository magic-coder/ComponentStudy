package com.richfit.module_cqyt.module_ws.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditFragment;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.module_cqyt.adapter.InspectInfoAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/9/21.
 */

public class WSEditFragment extends BaseEditFragment<WSEditPresenterImp>
        implements IWSEditlView {
    public static final String EXTRA_DECLARATION_REF_KEY = "declaration_ref";
    public static final String EXTRA_TRANS_LINE_ID_KEY = "extra_trans_line_id_key";

    //物料
    TextView tvMaterialNum;
    TextView tvMaterialDesc;
    TextView tvMaterialGroup;
    EditText etQuantity;
    TextView tvBatchFlag;
    //报检单
    Spinner spDeclarationRef;
    //备注
    EditText etRemark;

    String mInspectionNum;

    String mTransLineId;

    /*报检单库存*/
    List<InventoryEntity> mDatas;
    InspectInfoAdapter mAdapter;

    @Override
    protected int getContentId() {
        return R.layout.cqyt_fragment_ws_edit;
    }

    @Override
    public void initPresenter() {
        mPresenter = new WSEditPresenterImp(mActivity);
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        tvMaterialNum = mView.findViewById(R.id.tv_material_num);
        tvMaterialDesc = mView.findViewById(R.id.tv_material_desc);
        tvMaterialGroup = mView.findViewById(R.id.tv_material_group);
        etQuantity = mView.findViewById(R.id.et_quantity);
        tvBatchFlag = mView.findViewById(R.id.tv_batch_flag);
        spDeclarationRef = mView.findViewById(R.id.cqyt_sp_declaration_ref);
        etRemark = mView.findViewById(R.id.et_remark);
    }

    @Override
    public void initEvent() {

    }

    @Override
    public void initData() {
        Bundle bundle = getArguments();
        //物料编码
        String materialNum = bundle.getString(Global.EXTRA_MATERIAL_NUM_KEY);
        String materialId = bundle.getString(Global.EXTRA_MATERIAL_ID_KEY);
        //批次
        String batchFlag = bundle.getString(Global.EXTRA_BATCH_FLAG_KEY);
        //备注
        String remark = bundle.getString(Global.EXTRA_REMARK_KEY);
        //数量
        String quantity = bundle.getString(Global.EXTRA_QUANTITY_KEY);

        //transLineId
        mTransLineId = bundle.getString(EXTRA_TRANS_LINE_ID_KEY);

        //绑定数据
        tvMaterialNum.setText(materialNum);
        tvMaterialNum.setTag(materialId);
        etQuantity.setText(quantity);
        tvBatchFlag.setText(batchFlag);
        etRemark.setText(remark);
        //获取报检单
        mInspectionNum = bundle.getString(EXTRA_DECLARATION_REF_KEY);

        //获取缓存信息
        mPresenter.getTransferInfoSingle(mRefData.bizType, materialNum,
                Global.USER_ID, mRefData.workId, mRefData.invId, mRefData.recWorkId,
                mRefData.recInvId, batchFlag, "", -1);

    }

    @Override
    public void initDataLazily() {
        if (mRefData == null) {
            showMessage("请先在抬头界面输入相应的数据");
            return;
        }
        if (TextUtils.isEmpty(mRefData.workCode)) {
            showMessage("请先在抬头界面选择工厂");
            return;
        }
        if (TextUtils.isEmpty(mRefData.voucherDate)) {
            showMessage("请先在抬头界面选择取样日期");
            return;
        }
    }


    @Override
    public void onBindCommonUI(ReferenceEntity refData, String batchFlag) {
        RefDetailEntity data = refData.billDetailList.get(0);
        //刷新UI
        tvMaterialNum.setTag(data.materialId);
        tvMaterialDesc.setText(data.materialDesc);
        tvMaterialGroup.setText(data.materialGroup);
    }

    @Override
    public void loadTransferSingleInfoFail(String message) {
        showMessage(message);
    }

    @Override
    public void loadTransferSingeInfoComplete() {
        String materialNum = getString(tvMaterialNum);
        String bachFlag = getString(tvBatchFlag);
        if (TextUtils.isEmpty(materialNum)) {
            showMessage("未获取到物料编码");
            return;
        }
        if (TextUtils.isEmpty(bachFlag)) {
            showMessage("未获取到批次");
            return;
        }
        //加载报检单列表
        mPresenter.getInspectionInfo(mBizType, materialNum, Global.USER_ID, mRefData.workCode, null);
    }



    @Override
    public void showInventory(List<InventoryEntity> list) {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        mDatas.addAll(list);
        if (mAdapter == null) {
            mAdapter = new InspectInfoAdapter(mActivity, R.layout.item_simple_sp, mDatas);
            spDeclarationRef.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInventoryFail(String message) {
        showMessage(message);
        if (mDatas != null) {
            mDatas.clear();
        }
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void loadInventoryComplete() {
        //自动匹配缓存中的报检单
        if (!TextUtils.isEmpty(mInspectionNum)) {
            UiUtil.setSelectionForLocation(mDatas, mInspectionNum, spDeclarationRef);
        }
    }


    @Override
    public void showOperationMenuOnCollection(final String companyCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("提示");
        builder.setMessage("您真的需要保存数据吗?点击确定将保存数据.");
        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton("确定", (dialog, which) -> {
            dialog.dismiss();
            saveCollectedData();
        });
        builder.show();
    }


    @Override
    public boolean checkCollectedDataBeforeSave() {

        if (TextUtils.isEmpty(getString(tvMaterialNum))) {
            showMessage("物料编码为空");
            return false;
        }

        if (TextUtils.isEmpty(getString(tvBatchFlag))) {
            showMessage("请输入批次");
            return false;
        }

        if (mDatas == null || mDatas.size() <= 0) {
            showMessage("请先获取报检单数据");
            return false;
        }

        if(TextUtils.isEmpty(mTransLineId)) {
            showMessage("未获取到该行id");
            return false;
        }

        String quantity = getString(etQuantity);
        if (TextUtils.isEmpty(quantity)) {
            showMessage("请输入数量");
            return false;
        }

        if (Float.valueOf(quantity) < 0.0f) {
            showMessage("输入数量不合理");
            return false;
        }

        return true;
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = new ResultEntity();
        result.quantity = getString(etQuantity);
        result.materialNum = getString(tvMaterialNum);
        result.materialId = CommonUtil.Obj2String(tvMaterialNum.getTag());
        result.remark = getString(etRemark);
        result.businessType = mBizType;
        result.batchFlag = getString(tvBatchFlag);
        result.userId = Global.USER_ID;
        result.voucherDate = mRefData.voucherDate;
        InventoryEntity item = mDatas.get(spDeclarationRef.getSelectedItemPosition());
        result.arrivalDate = item.arrivalDate;
        result.contractNum = item.contractNum;
        result.inspectionDate = item.inspectionDate;
        result.inspectionNum = item.inspectionNum;
        result.invCode = item.invCode;
        result.invName = item.invName;
        result.planNum = item.planNum;
        result.supplierNum = item.supplierNum;
        result.supplierDesc = item.supplierDesc;
        result.workCode = item.workCode;
        result.workName = item.workName;
        result.transLineId = mTransLineId;
        result.modifyFlag = "Y";
        return result;
    }

    @Override
    public void saveEditedDataSuccess(String message) {
        super.saveEditedDataSuccess(message);
        //清除
        etQuantity.setText("");
    }
}
