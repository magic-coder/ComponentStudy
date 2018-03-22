package com.richfit.module_cq.module_dsn;

import android.widget.TextView;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzck.base_dsn_edit.BaseDSNEditFragment;
import com.richfit.sdk_wzck.base_dsn_edit.imp.DSNEditPresenterImp;

/**
 * Created by monday on 2017/12/8.
 */

public class CQDSNEditFragment extends BaseDSNEditFragment<DSNEditPresenterImp>{
    TextView tvSugggestedLocation;
    TextView tvSuggestedBatchFlag;

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_dsn_edit;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DSNEditPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        super.initView();
        tvSugggestedLocation = mView.findViewById(R.id.cqzt_tv_suggested_location);
        tvSuggestedBatchFlag = mView.findViewById(R.id.cqzt_tv_suggested_batch_flag);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    protected void initData() {
        super.initData();
        //获取建议批次和仓位
        InventoryQueryParam queryParam = provideInventoryQueryParam();
        mPresenter.getSuggestLocationAndBatchFlag(mRefData.workCode, getString(tvInv),
                getString(tvMaterialNum), queryParam.queryType);
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
