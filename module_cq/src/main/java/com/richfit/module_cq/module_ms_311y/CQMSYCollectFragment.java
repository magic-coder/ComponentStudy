package com.richfit.module_cq.module_ms_311y;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzyk.base_ms_collect.BaseMSCollectFragment;
import com.richfit.sdk_wzyk.base_ms_collect.imp.MSCollectPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/12/5.
 */

public class CQMSYCollectFragment extends BaseMSCollectFragment<MSCollectPresenterImp> {


    TextView tvSuggestedLocation;
    TextView tvSuggestedBatchFlag;

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_msy311_collect;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MSCollectPresenterImp(mActivity);
    }


    @Override
    public void initView() {
        super.initView();
        tvSuggestedLocation = mView.findViewById(R.id.cqzt_tv_suggested_location);
        tvSuggestedBatchFlag = mView.findViewById(R.id.cqzt_tv_suggested_batch_flag);
        //打开接收仓位和接收批次
        llRecLocation.setVisibility(View.VISIBLE);
        llRecBatch.setVisibility(View.VISIBLE);
    }


    @Override
    protected void initData() {
    }

    @Override
    public void loadInventoryComplete() {
        super.loadInventoryComplete();
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        InvEntity invEntity = mInvDatas.get(spSendInv.getSelectedItemPosition());
        InventoryQueryParam param = provideInventoryQueryParam();
        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("batchFlag", getString(etSendBatchFlag));
        mPresenter.getSuggestInventoryInfo(param.queryType, lineData.workCode, invEntity.invCode,
                getString(etMaterialNum), extraMap);
    }


    @Override
    public void getSuggestedLocationSuccess(InventoryEntity suggestedInventory) {
        super.getSuggestedLocationSuccess(suggestedInventory);
        tvSuggestedLocation.setText(suggestedInventory.suggestLocation);
        tvSuggestedBatchFlag.setText(suggestedInventory.suggestBatch);
        etRecLoc.setText(suggestedInventory.suggestLocation);
    }


    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.queryType = "01";
        return queryParam;
    }


    @Override
    public void showOperationMenuOnCollection(final String companyCode) {
        if ((TextUtils.isEmpty(getString(tvLocQuantity)) || "0".equals(getString(tvLocQuantity)))) {
            if (TextUtils.isEmpty(getString(etRecBatchFlag))) {
                showMessage("请输入接收批次");
                return;
            }
            mActLocation = null;
            //如果是第一次，调用获取接收建议仓位
            RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
            InvEntity invEntity = mInvDatas.get(spSendInv.getSelectedItemPosition());
            InventoryQueryParam param = provideInventoryQueryParam();
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("recBatchFlag", getString(etRecBatchFlag));
            mPresenter.checkRecLocation(param.queryType, lineData.workCode, invEntity.invCode,
                    getString(etMaterialNum), extraMap);
            return;
        }
        super.showOperationMenuOnCollection(companyCode);
    }

    @Override
    public void getActLocationSuccess(InventoryEntity suggestedInventory) {
        super.getActLocationSuccess(suggestedInventory);
        if (!TextUtils.isEmpty(mActLocation) && !mActLocation.equalsIgnoreCase(getString(etRecLoc))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("提示");
            StringBuffer sb = new StringBuffer();
            sb.append("物料").append(getString(etMaterialNum))
                    .append(getString(etRecBatchFlag)).append("接收批次在工厂").append(mRefData.workCode)
                    .append("下已存在接收仓位").append(mActLocation).append("是否继续");
            builder.setMessage("您真的需要保存数据吗?点击确定将保存数据.");
            builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
            builder.setPositiveButton("继续", (dialog, which) -> {
                dialog.dismiss();
                saveCollectedData();
            });
            builder.show();
            return;
        }
        //如果是第一次，而且校验仓位和接收仓位那么直接可以保存
        super.showOperationMenuOnCollection("");
    }

    @Override
    public void getActLocationFail(String message) {
        showMessage(message);
        super.showOperationMenuOnCollection("");
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.suggestBatch = getString(tvSuggestedBatchFlag);
        result.suggestLocation = getString(tvSuggestedLocation);
        return result;
    }

    @Override
    protected void clearAllUI() {
        super.clearAllUI();
        clearCommonUI(tvSuggestedBatchFlag, tvSuggestedLocation);
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }

}
