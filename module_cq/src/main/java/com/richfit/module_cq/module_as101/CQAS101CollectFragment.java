package com.richfit.module_cq.module_as101;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter.BottomDialogMenuAdapter;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.L;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;
import com.richfit.sdk_wzys.camera.TakephotoActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/12/8.
 */

public class CQAS101CollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    TextView tvManufacturer;
    TextView tvDurabilityPeriod;
    TextView tvProductionDate;
    TextView tvSugggestedLocation;
    TextView tvSuggestedBatchFlag;


    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type, list);
        int length = list.length;
        //|batchFlag| |20180207|90|ABC|
        tvManufacturer.setText(list[length - 2]);
        tvDurabilityPeriod.setText(list[length - 3]);
        tvProductionDate.setText(list[length - 4]);
    }

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_as101_collect;
    }

    @Override
    protected void initVariable(@Nullable Bundle savedInstanceState) {
        super.initVariable(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }


    @Override
    public void initView() {
        super.initView();
        tvManufacturer = mView.findViewById(R.id.cqzt_tv_manufacturer);
        tvDurabilityPeriod = mView.findViewById(R.id.cqzt_tv_durabilityPeriod);
        tvProductionDate = mView.findViewById(R.id.cqzt_tv_productionDate);
        tvSugggestedLocation = mView.findViewById(R.id.cqzt_tv_suggested_location);
        tvSuggestedBatchFlag = mView.findViewById(R.id.cqzt_tv_suggested_batch_flag);
    }

    @Override
    protected void initData() {
    }

    /**
     * 这里不需要去验证仓位是否存在
     *
     * @param batchFlag
     * @param location
     */
    @Override
    protected void getTransferSingle(String batchFlag, String location) {
        checkLocationSuccess(batchFlag, location);
    }

    @Override
    public void loadLocationList(int position) {
        super.loadLocationList(position);
        mActLocation = null;
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        InvEntity invEntity = mInvDatas.get(position);
        InventoryQueryParam param = provideInventoryQueryParam();
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("batchFlag", getString(etBatchFlag));
        mPresenter.getSuggestInventoryInfo(param.queryType, lineData.workCode, invEntity.invCode,
                getString(etMaterialNum), extraMap);
    }

    @Override
    public void getSuggestedLocationSuccess(InventoryEntity suggestedInventory) {
        super.getSuggestedLocationSuccess(suggestedInventory);
        tvSugggestedLocation.setText(suggestedInventory.suggestLocation);
        tvSuggestedBatchFlag.setText(suggestedInventory.suggestBatch);
    }

    @Override
    public void onBindCache(RefDetailEntity cache, String batchFlag, String location) {
        super.onBindCache(cache, batchFlag, location);
        if (!isNLocation && cache != null) {
            //绑定数据
            tvManufacturer.setText(cache.manufacturer);
            tvDurabilityPeriod.setText(cache.durabilityPeriod);
            tvProductionDate.setText(cache.productionDate);
        }
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.manufacturer = getString(tvManufacturer);
        result.durabilityPeriod = getString(tvDurabilityPeriod);
        result.productionDate = getString(tvProductionDate);
        result.suggestLocation = getString(tvSugggestedLocation);
        result.suggestBatch = getString(tvSuggestedBatchFlag);
        return result;
    }

    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.queryType = "01";
        return queryParam;
    }


    @Override
    public void showOperationMenuOnCollection(final String companyCode) {
        if ((TextUtils.isEmpty(getString(tvLocQuantity)) || "0".equals(getString(tvLocQuantity)))
                && !TextUtils.isEmpty(mActLocation)) {
            String location = getString(etLocation);
            if (mActLocation.equalsIgnoreCase(location)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("提示");
                StringBuffer sb = new StringBuffer();
                sb.append("物料").append(getString(etMaterialNum))
                        .append(getString(etBatchFlag)).append("批次在工厂").append(mRefData.workCode)
                        .append("下已存在仓位").append(mActLocation).append("是否继续");
                builder.setMessage("您真的需要保存数据吗?点击确定将保存数据.");
                builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
                builder.setPositiveButton("继续", (dialog, which) -> {
                    dialog.dismiss();
                    saveCollectedData();
                });
                builder.show();
                return;
            }
        }
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
        menu.menuName = "拍照";
        menu.menuImageRes = R.mipmap.icon_take_photo1;
        menu.takePhotoType = 1;
        menus.add(menu);
        return menus;
    }

    protected void toTakePhoto(String menuName, int takePhotoType) {
        if (!checkBeforeTakePhoto()) {
            return;
        }
        Intent intent = new Intent(mActivity, TakephotoActivity.class);
        Bundle bundle = new Bundle();
        //入库的子菜单的名称
        bundle.putString(Global.EXTRA_TITLE_KEY, "物资101入库-" + menuName);
        //拍照类型
        bundle.putInt(Global.EXTRA_TAKE_PHOTO_TYPE, takePhotoType);
        //单据号
        bundle.putString(Global.EXTRA_REF_NUM_KEY, mRefData.recordNum);

        bundle.putString(Global.EXTRA_BIZ_TYPE_KEY, mBizType);
        bundle.putString(Global.EXTRA_REF_TYPE_KEY, mRefType);
        //单据行号
        int selectedLineNum = getIndexByLineNum(mSelectedRefLineNum);
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        //行号
        bundle.putString(Global.EXTRA_REF_LINE_NUM_KEY, lineData.lineNum);
        //行id
        bundle.putString(Global.EXTRA_REF_LINE_ID_KEY, lineData.refLineId);
        bundle.putInt(Global.EXTRA_POSITION_KEY, selectedLineNum);
        bundle.putBoolean(Global.EXTRA_IS_LOCAL_KEY, mPresenter.isLocal());
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

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
}
