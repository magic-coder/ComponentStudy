package com.richfit.module_cq.module_ms_311n;

import android.widget.TextView;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzyk.base_msn_edit.BaseMSNEditFragment;
import com.richfit.sdk_wzyk.base_msn_edit.imp.MSNEditPresenterImp;

/**
 * Created by monday on 2017/12/5.
 */

public class CQMSNEditFragment extends BaseMSNEditFragment<MSNEditPresenterImp> {

    TextView tvSugggestedLocation;
    TextView tvSuggestedBatchFlag;

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_msn311_edit;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MSNEditPresenterImp(mActivity);
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
        mPresenter.getSuggestLocationAndBatchFlag(mRefData.workCode, getString(tvSendInv),
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
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.queryType = "01";
        return queryParam;
    }
}
