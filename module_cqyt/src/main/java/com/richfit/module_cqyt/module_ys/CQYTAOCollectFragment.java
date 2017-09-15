package com.richfit.module_cqyt.module_ys;


import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.richfit.common_lib.lib_adapter.SimpleAdapter;
import com.richfit.common_lib.utils.L;
import com.richfit.common_lib.utils.UiUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注意该业务比较特别，每一个单据的所有的行明细都是由一行生成拆分出来的，这就意味着
 * 单据中所有的行的refLineId和lineNum都一致，所以不能将它作为唯一的表示来标识该行。
 * 这里采用的方式使用每一行明细的insLot字段来标识每一行的明细。
 * Created by monday on 2017/3/1.
 */

public class CQYTAOCollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    EditText etReturnQuantity;

    //移动原因
    Spinner spMoveCause;

    List<SimpleEntity> mMoveCauses;

    //仓储类型
    Spinner spLocationType;
    List<SimpleEntity> mLocationTypes;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        if (list != null && list.length == 2 && !cbSingle.isChecked()) {
            String location = list[Global.LOCATION_POS];
            String locationType = list[Global.LOCATION_TYPE_POS];
            clearCommonUI(etLocation);
            etLocation.setText(location);
            //自动选择仓储类型
            UiUtil.setSelectionForSimpleSp(mLocationTypes, locationType, spLocationType);
            getTransferSingle(getString(etBatchFlag), location);
            return;
        }
        super.handleBarCodeScanResult(type, list);
    }
    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_ao_collect;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        TextView tvRefLineNumName = (TextView) mView.findViewById(R.id.tv_ref_line_num_name);
        etReturnQuantity = mView.findViewById(R.id.et_return_quantity);
        spMoveCause = mView.findViewById(R.id.sp_move_cause);
        tvRefLineNumName.setText("检验批");
        llInsLostQuantity.setVisibility(View.VISIBLE);
        tvActQuantityName.setText("允许过账数量");
        tvQuantityName.setText("过账数量");

        //显示仓储类型
        mView.findViewById(R.id.ll_location_type).setVisibility(View.VISIBLE);
        spLocationType = mView.findViewById(R.id.sp_location_type);
    }


    @Override
    public void initEvent() {
        super.initEvent();
        etLocation.setOnRichAutoEditTouchListener((view, location) -> getTransferSingle(getString(etBatchFlag), location));

        //增加库存地点选择出发仓储类型的获取
        RxAdapterView.itemSelections(spInv)
                .filter(pos -> pos > 0)
                .subscribe(pos -> {
                    if (isNLocation) {
                        //如果不上架
                        getTransferSingle(getString(etBatchFlag), getString(etLocation));
                    } else {
                        mPresenter.getDictionaryData("locationType");
                    }
                });

        //增加仓储类型的选择获取提示库粗
        RxAdapterView.itemSelections(spLocationType)
                .filter(a -> spLocationType.getAdapter() != null && mLocationTypes != null
                        && mLocationTypes.size() > 0)
                .subscribe(position -> loadLocationList(false));
    }

    @Override
    public void initData() {
        mPresenter.getDictionaryData("moveCause");
    }


    @Override
    public void bindCommonCollectUI() {
        mSelectedRefLineNum = mRefLines.get(spRefLine.getSelectedItemPosition());
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        if ("Y".equalsIgnoreCase(lineData.dangerFlag)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("温馨提示").setMessage("您正在采集的物料是危险品,是否还继续采集?")
                    .setPositiveButton("继续采集", (dialog, which) -> {
                        super.bindCommonCollectUI();
                        dialog.dismiss();
                    })
                    .setNegativeButton("放弃采集", (dialog, which) -> {
                        dialog.dismiss();
                    }).show();
            return;
        }
        super.bindCommonCollectUI();
    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        List<SimpleEntity> moveCauses = data.get("moveCause");
        if (moveCauses != null) {
            if (mMoveCauses == null) {
                mMoveCauses = new ArrayList<>();
            }
            mMoveCauses.clear();
            SimpleEntity tmp = new SimpleEntity();
            tmp.name = "请选择";
            mMoveCauses.add(tmp);
            mMoveCauses.addAll(moveCauses);
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp,
                    mMoveCauses);
            spMoveCause.setAdapter(adapter);
        }

        List<SimpleEntity> locationTypes = data.get("locationType");
        if (locationTypes != null) {
            if (mLocationTypes == null) {
                mLocationTypes = new ArrayList<>();
            }
            mLocationTypes.clear();
            mLocationTypes.addAll(locationTypes);
            SimpleAdapter adapter = new SimpleAdapter(mActivity, R.layout.item_simple_sp,
                    mLocationTypes,false);
            spLocationType.setAdapter(adapter);
        }
    }


    //重写该方法，在获取提示库存之前清除历史库存
    @Override
    public void loadLocationList(boolean isDropDown) {
        if (mLocationList != null && mLocationAdapter != null) {
            mLocationList.clear();
            mLocationAdapter.notifyDataSetChanged();
        }
        super.loadLocationList(isDropDown);
    }


    //重写该方法的目的是获取累计件数缓存以及件数缓存。另外就是增加存储类型匹配条件
    @Override
    public void onBindCache(RefDetailEntity cache, String batchFlag, String location) {

        if (!isNLocation) {
            if (cache != null) {
                tvTotalQuantity.setText(cache.totalQuantity);
                //锁定库存地点
                lockInv(cache.invId);
                //匹配缓存
                List<LocationInfoEntity> locationInfos = cache.locationList;
                if (locationInfos == null || locationInfos.size() == 0) {
                    //没有缓存
                    tvLocQuantity.setText("0");
                    return;
                }
                tvLocQuantity.setText("0");
                /**
                 * 这里匹配缓存是通过批次+仓位匹配的，但是批次即便是在打开了批次管理的情况下
                 * 也可能没有批次。
                 */
                for (LocationInfoEntity cachedItem : locationInfos) {
                    //缓存和输入的都为空或者都不为空而且相等,那么系统默认批次匹配
                    boolean isMatch = false;

                    isBatchValidate = !isOpenBatchManager ? true : ((TextUtils.isEmpty(cachedItem.batchFlag) && TextUtils.isEmpty(batchFlag)) ||
                            (!TextUtils.isEmpty(cachedItem.batchFlag) && !TextUtils.isEmpty(batchFlag) && batchFlag.equalsIgnoreCase(cachedItem.batchFlag)));

                    String locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
                    if (!isOpenBatchManager) {
                        //没有打开批次管理，直接使用仓位匹配
                        isMatch = location.equalsIgnoreCase(cachedItem.location) && locationType.equalsIgnoreCase(cachedItem.locationType);
                    } else {
                        if (TextUtils.isEmpty(cachedItem.batchFlag) && TextUtils.isEmpty(batchFlag)) {
                            //打开批次管理，但是没有输入批次
                            isMatch = location.equalsIgnoreCase(cachedItem.location) && locationType.equalsIgnoreCase(cachedItem.locationType);
                        } else if (!TextUtils.isEmpty(cachedItem.batchFlag) && !TextUtils.isEmpty(batchFlag)) {
                            //打开了批次管理，输入了批次
                            isMatch = location.equalsIgnoreCase(cachedItem.location) && batchFlag.equalsIgnoreCase(cachedItem.batchFlag)
                                    && locationType.equalsIgnoreCase(cachedItem.locationType);
                        }
                    }
                    L.e("isBatchValidate = " + isBatchValidate + "; isMatch = " + isMatch);

                    //注意它没有匹配次成功可能是批次页可能是仓位。
                    if (isMatch) {
                        tvLocQuantity.setText(cachedItem.quantity);
                        break;
                    }
                }

                if (!isBatchValidate) {
                    showMessage("批次输入有误，请检查批次是否与缓存批次输入一致");
                }
            }
        } else {
            //对于不上架的物资，显示累计数量和锁定库存地点
            if (cache != null) {
                tvTotalQuantity.setText(cache.totalQuantity);
                lockInv(cache.invId);
            }
        }

        if (cache != null) {
            etReturnQuantity.setText(cache.returnQuantity);
            //移动原因
            UiUtil.setSelectionForSimpleSp(mMoveCauses, cache.moveCause, spMoveCause);
        }
    }


    @Override
    protected boolean refreshQuantity(final String quantity) {
        //1.实收数量必须大于0
        final float quantityV = CommonUtil.convertToFloat(quantity, 0.0f);
        if (Float.compare(quantityV, 0.0f) <= 0.0f) {
            showMessage("输入过账数量不合理");
            return false;
        }

        //2.实收数量必须小于合格数量(应收数量)
        final float actQuantityV = CommonUtil.convertToFloat(getString(tvActQuantity), 0.0f);
        if (Float.compare(quantityV, actQuantityV) > 0.0f) {
            showMessage("过账数量不能大于应收数量");
            return false;
        }

        // 3.过账数量 <= 检验批数量
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        final float orderQuantityV = CommonUtil.convertToFloat(lineData.orderQuantity, 0.0f);
        if (Float.compare(quantityV, orderQuantityV) > 0.0f) {
            showMessage("过账数量不能大于检验批数量");
            if (!cbSingle.isChecked())
                etQuantity.setText("");
            return false;
        }
        //4. 过账+退货 <= 应收
        final float returnQuantityV = CommonUtil.convertToFloat(getString(etReturnQuantity), 0.0F);
        if (Float.compare(quantityV + returnQuantityV, actQuantityV) > 0.0f) {
            showMessage("过账数量加采购退货数量不能大于应收数量");
            return false;
        }

        return true;
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (!TextUtils.isEmpty(getString(etReturnQuantity))) {
            if (spMoveCause.getSelectedItemPosition() <= 0) {
                showMessage("请先选择移动原因");
                return false;
            }
        }

        if(mLocationTypes == null || mLocationTypes.size() <= 0) {
            showMessage("未获取到仓储类型");
            return false;
        }

        return super.checkCollectedDataBeforeSave();
    }

    @Override
    public void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(etReturnQuantity);
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.returnQuantity = getString(etReturnQuantity);
        //移动原因
        if (spMoveCause.getSelectedItemPosition() > 0) {
            result.moveCause = mMoveCauses.get(spMoveCause.getSelectedItemPosition()).code;
        }
        //仓储类型
        result.locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
        return result;
    }

    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        if (mLocationTypes != null && spLocationType.getSelectedItemPosition() > 0) {
            queryParam.extraMap = new HashMap<>();
            String locationType = mLocationTypes.get(spLocationType.getSelectedItemPosition()).code;
            queryParam.extraMap.put("locationType", locationType);
        }
        return queryParam;
    }
}
