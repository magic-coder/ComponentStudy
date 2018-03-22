package com.richfit.module_cq.module_rs;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/12/5.
 */

public class CQRSCollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    TextView tvManufacturer;
    TextView tvDurabilityPeriod;
    TextView tvProductionDate;
    TextView tvSugggestedLocation;
    TextView tvSuggestedBatchFlag;

    @Override
    public void handleBarCodeScanResult(String type, String[] list) {
        super.handleBarCodeScanResult(type, list);
        int length = list.length;
        //20180207|90|ABC|
        tvManufacturer.setText(list[length - 2]);
        tvDurabilityPeriod.setText(list[length - 3]);
        tvProductionDate.setText(list[length - 4]);
    }

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_rs_collect;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initView() {
        super.initView();
        tvActQuantityName.setText("应退数量");
        tvQuantityName.setText("实退数量");
        tvManufacturer = mView.findViewById(R.id.cqzt_tv_manufacturer);
        tvDurabilityPeriod = mView.findViewById(R.id.cqzt_tv_durabilityPeriod);
        tvProductionDate = mView.findViewById(R.id.cqzt_tv_productionDate);
        tvSugggestedLocation = mView.findViewById(R.id.cqzt_tv_suggested_location);
        tvSuggestedBatchFlag = mView.findViewById(R.id.cqzt_tv_suggested_batch_flag);
    }

    /**
     * 这里不需要去验证仓位是否存在
     * @param batchFlag
     * @param location
     */
    @Override
    protected void getTransferSingle(String batchFlag, String location) {
        checkLocationSuccess(batchFlag,location);
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
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.queryType = "01";
        return queryParam;
    }


    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.suggestLocation = getString(tvSugggestedLocation);
        result.suggestBatch = getString(tvSuggestedBatchFlag);
        return result;
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
        super.showOperationMenuOnCollection(companyCode);
    }
}
