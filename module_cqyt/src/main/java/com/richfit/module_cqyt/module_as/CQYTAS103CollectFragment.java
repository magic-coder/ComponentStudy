package com.richfit.module_cqyt.module_as;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;
import com.richfit.sdk_wzys.camera.TakephotoActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by monday on 2017/6/29.
 */

public class CQYTAS103CollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    Spinner spInspectionResult;
    EditText etUnqualifiedQuantity;
    EditText etRemark;
    EditText etQuantityCustom;
    TextView tvTotalQuantityCustom;

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
        etUnqualifiedQuantity = (EditText) mView.findViewById(R.id.cqyt_et_unqualified_quantity);
        etRemark = (EditText) mView.findViewById(R.id.et_remark);
        etQuantityCustom = (EditText) mView.findViewById(R.id.cqyt_et_quantity_custom);
        tvTotalQuantityCustom = (TextView) mView.findViewById(R.id.cqyt_tv_total_quantity_custom);
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

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (spInspectionResult.getSelectedItemPosition() == 1 && TextUtils.isEmpty(getString(etUnqualifiedQuantity))) {
            showMessage("请先输入不合格数量");
            return false;
        }
        if (mRefData == null) {
            showMessage("请先在抬头界面获取单据数据");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.deliveryOrder)) {
            showMessage("请现在抬头界面输入提货单");
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
    public void saveCollectedData() {
        if (!checkCollectedDataBeforeSave()) {
            return;
        }
        Flowable.create((FlowableOnSubscribe<ResultEntity>) emitter -> {
            RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
            ResultEntity result = new ResultEntity();
            result.businessType = mRefData.bizType;
            result.refCodeId = mRefData.refCodeId;
            result.refCode = mRefData.recordNum;
            result.refLineNum = lineData.lineNum;
            result.voucherDate = mRefData.voucherDate;
            result.refType = mRefData.refType;
            result.moveType = mRefData.moveType;
            result.userId = Global.USER_ID;
            result.refLineId = lineData.refLineId;
            result.workId = lineData.workId;
            result.unit = TextUtils.isEmpty(lineData.recordUnit) ? lineData.materialUnit : lineData.recordUnit;
            result.unitRate = Float.compare(lineData.unitRate, 0.0f) == 0 ? 1.f : lineData.unitRate;
            result.invId = mInvDatas.get(spInv.getSelectedItemPosition()).invId;
            result.materialId = lineData.materialId;
            result.location = isNLocation ? "barcode" : getString(etLocation);
            result.batchFlag = getString(etBatchFlag);
            result.quantity = getString(etQuantity);
            result.modifyFlag = "N";
            result.refDoc = lineData.refDoc;
            result.refDocItem = lineData.refDocItem;
            result.supplierNum = mRefData.supplierNum;
            result.inspectionResult = spInspectionResult.getSelectedItemPosition() == 0 ? "01" : "02";
            result.unqualifiedQuantity = getString(etUnqualifiedQuantity);
            result.remark = getString(etRemark);
            //提货单
            result.deliveryOrder = mRefData.deliveryOrder;
            //件数
            result.quantityCustom = getString(etQuantityCustom);
            emitter.onNext(result);
            emitter.onComplete();
        }, BackpressureStrategy.BUFFER).compose(TransformerHelper.io2main())
                .subscribe(result -> mPresenter.uploadCollectionDataSingle(result));
    }


    @Override
    public void saveCollectedDataSuccess() {
        super.saveCollectedDataSuccess();
        float quantityCustomQ = CommonUtil.convertToFloat(getString(etQuantityCustom), 0.0F);
        float totalQuantityCustomQ = CommonUtil.convertToFloat(getString(tvTotalQuantityCustom), 0.0F);
        tvTotalQuantityCustom.setText(String.valueOf(quantityCustomQ + totalQuantityCustomQ));
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

}
