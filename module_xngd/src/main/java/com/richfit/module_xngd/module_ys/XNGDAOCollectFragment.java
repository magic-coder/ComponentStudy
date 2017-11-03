package com.richfit.module_xngd.module_ys;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.richfit.common_lib.lib_adapter.BottomDialogMenuAdapter;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_xngd.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;
import com.richfit.sdk_wzys.camera.TakephotoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/5/26.
 */

public class XNGDAOCollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    //全检数量
    EditText etAllQuantity;
    //抽检数量
    EditText etPartQuantity;
    //检验方法
    Spinner spInspectionType;
    //检验状态
    Spinner spInspectionStatus;
    //处理情况
    EditText etProcessResult;

    ArrayList<SimpleEntity> mInspectionTypes;
    ArrayList<SimpleEntity> mInspectionStatus;

    @Override
    protected int getContentId() {
        return R.layout.xngd_fragment_ao_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        etLocation.setOnRichAutoEditTouchListener((view, location) -> getTransferSingle(getString(etBatchFlag), location));
    }

    @Override
    protected void initView() {
        super.initView();
        etAllQuantity = (EditText) mView.findViewById(R.id.xngd_et_all_quantity);
        etPartQuantity = (EditText) mView.findViewById(R.id.xngd_et_part_quantity);
        spInspectionType = (Spinner) mView.findViewById(R.id.xngd_sp_inspection_type);
        spInspectionStatus = (Spinner) mView.findViewById(R.id.xngd_sp_inspection_status);
        etProcessResult = (EditText) mView.findViewById(R.id.xngd_et_process_result);
    }

    @Override
    public void initData() {
        //初始化检验方法
        if (mInspectionTypes == null) {
            mInspectionTypes = new ArrayList<>();
        }
        mInspectionTypes.clear();
        List<String> inspectionTypes = getStringArray(R.array.xngd_inspection_types);
        for (int i = 0; i < inspectionTypes.size(); i++) {
            SimpleEntity item = new SimpleEntity();
            item.name = inspectionTypes.get(i);
            item.code = String.valueOf(i);
            mInspectionTypes.add(item);
        }

        SimpleAdapter inspectionTypeAdapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, mInspectionTypes);
        spInspectionType.setAdapter(inspectionTypeAdapter);

        //初始化检验状况
        if (mInspectionStatus == null) {
            mInspectionStatus = new ArrayList<>();
        }
        mInspectionStatus.clear();
        List<String> inspectionStatus = getStringArray(R.array.xngd_inspection_status);
        for (int i = 0; i < inspectionStatus.size(); i++) {
            SimpleEntity item = new SimpleEntity();
            item.name = inspectionStatus.get(i);
            item.code = String.valueOf(i);
            mInspectionStatus.add(item);
        }
        SimpleAdapter inspectionStatusAdapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp, mInspectionStatus);
        spInspectionStatus.setAdapter(inspectionStatusAdapter);
    }

    @Override
    public void initDataLazily() {
        etMaterialNum.setEnabled(false);
        if (mRefData == null) {
            showMessage("请先在抬头界面获取单据数据");
            return;
        }
        if (TextUtils.isEmpty(mRefData.refType)) {
            showMessage("请先在抬头界面获取单据数据");
            return;
        }
        if (TextUtils.isEmpty(mRefData.voucherDate)) {
            showMessage("请先在抬头界面选择过账日期");
            return;
        }
        etMaterialNum.setEnabled(true);
    }


    @Override
    public void onBindCache(RefDetailEntity cache, String batchFlag, String location) {
        super.onBindCache(cache, batchFlag, location);
        //增加客户化字段
        if (cache != null) {
            //全检数量
            etAllQuantity.setText(cache.allQuantity);
            //抽检数量
            etPartQuantity.setText(cache.partQuantity);
            //处理情况
            etProcessResult.setText(cache.processResult);
            //检验状况
            UiUtil.setSelectionForSimpleSp(mInspectionTypes, cache.inspectionType, spInspectionType);
            //检验方法
            UiUtil.setSelectionForSimpleSp(mInspectionStatus, cache.inspectionStatus, spInspectionStatus);
        }
    }


    @Override
    protected boolean refreshQuantity(final String quantity) {
        //实收数量
        final float quantityV = CommonUtil.convertToFloat(quantity, 0.0f);
        if (Float.compare(quantityV, 0.0f) <= 0.0f) {
            showMessage("输入实收数量有误,请出现输入");
            etQuantity.setText("");
            return false;
        }

        final float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        if (Float.compare(quantityV, actQuantityV) > 0.0f) {
            showMessage("实收数量不能大于应收数量");
            return false;
        }

        return true;
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (!isNLocation && TextUtils.isEmpty(getString(etLocation))) {
            showMessage("请输入上架仓位");
            return false;
        }
        if (!isNLocation && getString(etLocation).split("\\.").length != 4) {
            showMessage("您输入的仓位格式不正确");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        //是否应急
        //修改成明细取该字段
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        //全检数量
        result.allQuantity = getString(etAllQuantity);
        //抽检数量
        result.partQuantity = getString(etPartQuantity);
        //检验方法
        if (spInspectionType.getSelectedItemPosition() > 0)
            result.inspectionType = spInspectionType.getSelectedItemPosition();
        //检验状况
        if (spInspectionStatus.getSelectedItemPosition() > 0) {
            result.inspectionStatus = String.valueOf(spInspectionStatus.getSelectedItemPosition());
        }
        //处理情况
        result.processResult = getString(etProcessResult);

        result.invFlag = lineData.invFlag;
        result.specialInvFlag = lineData.specialInvFlag;
		result.specialInvNum = lineData.specialInvNum;
        result.projectNum = mRefData.projectNum;
        return result;
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
                    //1.保存数据
                    saveCollectedData();
                    break;
                case 1:
                    //2.拍照
                    toTakePhoto(menus.get(position).menuName, menus.get(position).takePhotoType);
                    break;
            }
            dialog.dismiss();
        });
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

    /**
     * 拍照之前做必要的检查
     *
     * @return
     */
    protected boolean checkBeforeTakePhoto() {
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

    protected void toTakePhoto(String menuName, int takePhotoType) {
        if (!checkBeforeTakePhoto()) {
            return;
        }
        Intent intent = new Intent(mActivity, TakephotoActivity.class);
        Bundle bundle = new Bundle();
        //入库的子菜单的名称
        bundle.putString(Global.EXTRA_TITLE_KEY, "物资验收拍照-" + menuName);
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
    public void _onPause() {
        super._onPause();
        clearAllUI();
        clearCommonUI(etMaterialNum);
    }

    @Override
    public void retry(String retryAction) {
        switch (retryAction) {
            case Global.RETRY_SAVE_COLLECTION_DATA_ACTION:
                saveCollectedData();
                break;
        }
        super.retry(retryAction);
    }

    /**
     * 清除所有的UI字段
     */
    @Override
    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etProcessResult, etAllQuantity, etPartQuantity);
        //检验方法
        if (spInspectionType.getAdapter() != null) {
            spInspectionType.setSelection(0);
        }
        //检验状况
        if (spInspectionStatus.getAdapter() != null) {
            spInspectionStatus.setSelection(0);
        }
    }


    @Override
    public InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam param = super.provideInventoryQueryParam();
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        param.queryType = "03";
        param.invType = TextUtils.isEmpty( mRefData.invType) ? "1" :  mRefData.invType;
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("invFlag", lineData.invFlag);
        extraMap.put("specialInvFlag", mRefData.specialInvFlag);
        extraMap.put("projectNum", mRefData.projectNum);
        param.extraMap = extraMap;
        return param;
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
