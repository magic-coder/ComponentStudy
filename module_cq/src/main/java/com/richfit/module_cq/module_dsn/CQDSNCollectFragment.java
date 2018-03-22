package com.richfit.module_cq.module_dsn;

import android.widget.TextView;

import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzck.base_dsn_collect.BaseDSNCollectFragment;
import com.richfit.sdk_wzck.base_dsn_collect.imp.DSNCollectPresenterImp;

/**
 * Created by monday on 2017/12/8.
 */

public class CQDSNCollectFragment extends BaseDSNCollectFragment<DSNCollectPresenterImp> {

    TextView tvSugggestedLocation;
    TextView tvSuggestedBatchFlag;

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_dsn_collect;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DSNCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initView() {
        super.initView();
        tvSugggestedLocation = mView.findViewById(R.id.cqzt_tv_suggested_location);
        tvSuggestedBatchFlag = mView.findViewById(R.id.cqzt_tv_suggested_batch_flag);
    }

    @Override
    public void loadInventoryComplete() {
        //获取建议仓位和批次
        InventoryQueryParam queryParam = provideInventoryQueryParam();
        InvEntity invEntity = mInvs.get(spInv.getSelectedItemPosition());
        mPresenter.getSuggestLocationAndBatchFlag(mRefData.workCode, invEntity.invCode,
                getString(etMaterialNum), queryParam.queryType);
        super.loadInventoryComplete();
    }


    @Override
    public void loadSuggestInfoSuccess(String suggestLocation, String suggestBatchFlag) {
        super.loadSuggestInfoSuccess(suggestLocation, suggestBatchFlag);
        tvSugggestedLocation.setText(suggestLocation);
        tvSuggestedBatchFlag.setText(suggestBatchFlag);
    }

    @Override
    public void loadSuggestInfoFail(String message) {
        super.loadSuggestInfoFail(message);
        clearCommonUI(tvSuggestedBatchFlag, tvSugggestedLocation);
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.costCenter = mRefData.costCenter;
        result.projectNum = mRefData.projectNum;
        result.orderNum = mRefData.orderNum;
        result.moveCause = mRefData.moveCause;
        result.reqCompany = mRefData.reqCompany;
        return result;
    }

    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.queryType = "01";
        return queryParam;
    }
}
