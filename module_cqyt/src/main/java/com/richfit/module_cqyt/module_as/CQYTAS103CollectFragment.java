package com.richfit.module_cqyt.module_as;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter.BottomDialogMenuAdapter;
import com.richfit.common_lib.utils.ArithUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;
import com.richfit.sdk_wzys.camera.TakephotoActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * private String dangerFlag;// 长庆危险品标识 如果为Y 则表示危险品。
 * 2017年7月27日增加危险品提示。用户输入实收和到货数量
 * Created by monday on 2017/6/29.
 */

public class CQYTAS103CollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    Spinner spInspectionResult;
    //到货数量
    EditText etArrivalQuantity;
    EditText etQuantityCustom;
    TextView tvTotalQuantityCustom;
    //展示不合格数量
    TextView tvUnqualifiedQuantity;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        mLineNumForFilter = list[list.length - 1];
        super.handleBarCodeScanResult(type, list);
    }

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_as103_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        spInspectionResult = (Spinner) mView.findViewById(R.id.sp_inspection_result);
        tvUnqualifiedQuantity = (TextView) mView.findViewById(R.id.cqyt_tv_unqualified_quantity);
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        tvTotalQuantityCustom = (TextView) mView.findViewById(R.id.cqyt_tv_total_quantity_custom);
        etArrivalQuantity = (EditText) mView.findViewById(R.id.et_arrival_quantity);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        etLocation.setOnRichAutoEditTouchListener((view, location) -> getTransferSingle(getString(etBatchFlag), location));
    }

    @Override
    public void initData() {
        //初始化验收结果
        List<String> items = getStringArray(R.array.cqyt_inspection_results);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, items);
        spInspectionResult.setAdapter(adapter);
    }

    @Override
    public void initDataLazily() {
        etMaterialNum.setEnabled(false);
        if (mRefData == null) {
            showMessage("请先在抬头界面获取单据数据");
            return;
        }

        if (TextUtils.isEmpty(mRefData.deliveryOrder)) {
            showMessage("请现在抬头界面输入提货单");
            return;
        }

        if (TextUtils.isEmpty(mRefData.moveType)) {
            showMessage("未获取到移动类型");
            return;
        }
        super.initDataLazily();
    }

    /**
     * 设置单据行信息之前，过滤掉
     *
     * @param refLines
     */
    @Override
    public void setupRefLineAdapter(ArrayList<String> refLines) {
        if (!TextUtils.isEmpty(mLineNumForFilter)) {
            //过滤掉重复行号
            ArrayList<String> lines = new ArrayList<>();
            for (String refLine : refLines) {
                if (refLine.equalsIgnoreCase(mLineNumForFilter)) {
                    lines.add(refLine);
                }
            }
            if (lines.size() == 0) {
                showMessage("未获取到条码的单据行信息");
            }
            super.setupRefLineAdapter(lines);
            return;
        }
        //如果单据中没有过滤行信息那么直接显示所有的行信息
        super.setupRefLineAdapter(refLines);
    }

    @Override
    public void bindCommonCollectUI() {
        mSelectedRefLineNum = mRefLines.get(spRefLine.getSelectedItemPosition());
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        if (!TextUtils.isEmpty(lineData.dangerFlag)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("温馨提示").setMessage(lineData.dangerFlag)
                    .setPositiveButton("确定", (dialog, which) -> {
                        super.bindCommonCollectUI();
                        dialog.dismiss();
                    }).show();
            return;
        }
        super.bindCommonCollectUI();
    }


    @Override
    public void showOperationMenuOnCollection(final String companyCode) {
        View rootView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_bottom_menu, null);
        GridView menu = (GridView) rootView.findViewById(R.id.gd_menus);
        final List<BottomMenuEntity> menus = provideDefaultBottomMenu();
        BottomDialogMenuAdapter adapter = new BottomDialogMenuAdapter(mActivity, R.layout.item_dialog_bottom_menu, menus);
        menu.setAdapter(adapter);

        final Dialog dialog = new Dialog(mActivity, R.style.MaterialDialogSheet);
        dialog.setContentView(rootView);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        menu.setOnItemClickListener((adapterView, view, position, id) -> {
            switch (position) {
                case 0:
                    //保存本次数据
                    saveCollectedData();
                    break;
                case 1:
                    //拍照
                    toTakePhoto(menus.get(position).menuName, menus.get(position).takePhotoType);
                    break;
            }
            dialog.dismiss();
        });
    }

    /**
     * 到货数量小于等于实收
     *
     * @param quantity
     * @return
     */
    @Override
    protected boolean refreshQuantity(final String quantity) {
        //将已经录入的所有的子节点的仓位数量累加
        final float totalQuantityV = CommonUtil.convertToFloat(getString(tvTotalQuantity), 0.0f);
        final float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        final float quantityV = CommonUtil.convertToFloat(quantity, 0.0f);
        if (Float.compare(quantityV, 0.0f) <= 0.0f) {
            showMessage("输入实收数量不合理");
            return false;
        }
        if (Float.compare(quantityV + totalQuantityV, actQuantityV) > 0.0f) {
            showMessage("输入实收数量有误，请重新输入");
            if (!cbSingle.isChecked())
                etQuantity.setText("");
            return false;
        }
        //处理到货数量
        final float arrivalQuantityV = CommonUtil.convertToFloat(getString(etArrivalQuantity), 0.0F);
        if (Float.compare(arrivalQuantityV, 0.0f) <= 0.0f) {
            showMessage("输入到货数量不合理");
            return false;
        }
        if (Float.compare(quantityV,arrivalQuantityV) > 0.0f) {
            showMessage("输入实收数量不能大于到货数量");
            return false;
        }

        return true;
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {

        if (mRefData == null) {
            showMessage("请先在抬头界面获取单据数据");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.deliveryOrder)) {
            showMessage("请先在抬头界面输入提货单");
            return false;
        }
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
        //到货日期
        result.arrivalDate = mRefData.arrivalDate;
        //报检日期
        result.inspectionDate = mRefData.inspectionDate;
        //提货单
        result.deliveryOrder = mRefData.deliveryOrder;
        //件数
        result.quantityCustom = getString(etQuantityCustom);
        //报检单位
        result.declaredUnit = mRefData.declaredUnit;
        // 班
        result.team = mRefData.team;
        // 岗位
        result.post = mRefData.post;
        // 生产厂家
        result.manufacture = mRefData.manufacture;
        //检验单位
        result.inspectionUnit = mRefData.inspectionUnit;
        // 备注
        result.remark = mRefData.remark;
        // 检验标准及特殊要求
        result.inspectionStandard = mRefData.inspectionStandard;
        //验收结果
        result.inspectionResult = spInspectionResult.getSelectedItemPosition() == 0 ? "01" : "02";
        //不合格数量
        //result.unqualifiedQuantity = getString(tvUnqualifiedQuantity);
        //到货数量
        result.arrivalQuantity = getString(etArrivalQuantity);
        //提货单
        result.deliveryOrder = mRefData.deliveryOrder;
        //件数
        result.quantityCustom = getString(etQuantityCustom);
        return result;
    }


    @Override
    public void saveCollectedDataSuccess(String message) {
        //计算不合格数量，到货-实收
        float arrivalQ = CommonUtil.convertToFloat(getString(etArrivalQuantity), 0.0F);
        float quantityQ = CommonUtil.convertToFloat(getString(etQuantity), 0.0F);
        tvUnqualifiedQuantity.setText(String.valueOf(arrivalQ - quantityQ));
        super.saveCollectedDataSuccess(message);
        if (!cbSingle.isChecked()) {
            etQuantityCustom.setText("");
        }
        tvTotalQuantityCustom.setText(String.valueOf(ArithUtil.add(getString(etQuantityCustom),
                getString(tvTotalQuantityCustom))));
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

    /**
     * 拍照之前做必要的检查
     *
     * @return
     */
    private boolean checkBeforeTakePhoto() {
        if (!etMaterialNum.isEnabled()) {
            showMessage("请先获取单据数据");
            return false;
        }
        if (TextUtils.isEmpty(mSelectedRefLineNum)) {
            showMessage("请先选择单据行");
            return false;
        }
        return true;
    }


    private void toTakePhoto(String menuName, int takePhotoType) {
        if (!checkBeforeTakePhoto()) {
            return;
        }
        Intent intent = new Intent(mActivity, TakephotoActivity.class);
        Bundle bundle = new Bundle();
        //入库的子菜单的名称
        bundle.putString(Global.EXTRA_TITLE_KEY, "103入库拍照-" + menuName);
        //拍照类型
        bundle.putInt(Global.EXTRA_TAKE_PHOTO_TYPE, takePhotoType);
        //单据号
        bundle.putString(Global.EXTRA_REF_NUM_KEY, mRefData.recordNum);

        bundle.putString(Global.EXTRA_BIZ_TYPE_KEY, mBizType);
        bundle.putString(Global.EXTRA_REF_TYPE_KEY, mRefType);
        //单据行号
        final int selectedLineNum = getIndexByLineNum(mSelectedRefLineNum);
        final RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        //行号
        bundle.putString(Global.EXTRA_REF_LINE_NUM_KEY, lineData.lineNum);
        //行id
        bundle.putString(Global.EXTRA_REF_LINE_ID_KEY, lineData.refLineId);
        bundle.putInt(Global.EXTRA_POSITION_KEY, selectedLineNum);
        bundle.putBoolean(Global.EXTRA_IS_LOCAL_KEY, mPresenter.isLocal());
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    @Override
    public List<BottomMenuEntity> provideDefaultBottomMenu() {
        List<BottomMenuEntity> menus = new ArrayList<>();

        BottomMenuEntity menu = new BottomMenuEntity();
        menu.menuName = "保存数据";
        menu.menuImageRes = R.mipmap.icon_save_data;
        menus.add(menu);

        menu = new BottomMenuEntity();
        menu.menuName = "外观拍照";
        menu.menuImageRes = R.mipmap.icon_take_photo4;
        menu.takePhotoType = 4;
        menus.add(menu);
        return menus;
    }


    @Override
    public void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etQuantityCustom, tvUnqualifiedQuantity, tvTotalQuantityCustom, etArrivalQuantity);
        if (spInspectionResult.getAdapter() != null) {
            spInspectionResult.setSelection(0);
        }
    }

}