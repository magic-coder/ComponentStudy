package com.richfit.module_cq.module_as_db;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.richfit.common_lib.lib_adapter.BottomDialogMenuAdapter;
import com.richfit.domain.bean.BottomMenuEntity;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/12/5.
 */

public class CQASDBCollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    TextView tvSugggestedLocation;
    TextView tvSuggestedBatchFlag;

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_asdb_collect;
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
        //隐藏工厂
        mView.findViewById(R.id.ll_work).setVisibility(View.GONE);
        tvSugggestedLocation = mView.findViewById(R.id.cqzt_tv_suggested_location);
        tvSuggestedBatchFlag = mView.findViewById(R.id.cqzt_tv_suggested_batch_flag);
    }

    @Override
    public void bindCommonCollectUI() {
        //实收默认等于应收
        etQuantity.setText(getString(tvActQuantity));
        mSelectedRefLineNum = mRefLines.get(spRefLine.getSelectedItemPosition());
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        lineData.workId = mRefData.workId;
        lineData.workCode = mRefData.workCode;
        lineData.invId = mRefData.invId;
        lineData.invCode = mRefData.invCode;
        super.bindCommonCollectUI();
    }

    /**
     * 库存地点列表获取完毕后，禁用用户操作。系统自动选择，触发相应的逻辑
     */
    @Override
    public void loadInvComplete() {
        spInv.setEnabled(false);
        super.loadInvComplete();
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
        super.showOperationMenuOnCollection(companyCode);
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.glf = mRefData.glf;
        result.lyf = mRefData.lyf;
        result.ckf = mRefData.ckf;
        result.yfhj = mRefData.yfhj;
        result.suggestLocation = getString(tvSugggestedLocation);
        result.suggestBatch = getString(tvSuggestedBatchFlag);
        return result;
    }
}
