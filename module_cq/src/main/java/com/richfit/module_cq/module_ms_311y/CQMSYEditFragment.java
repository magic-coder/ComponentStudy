package com.richfit.module_cq.module_ms_311y;

import android.view.View;
import android.widget.TextView;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzyk.base_ms_edit.BaseMSEditFragment;
import com.richfit.sdk_wzyk.base_ms_edit.imp.MSEditPresenterImp;

/**
 * Created by monday on 2017/12/5.
 */

public class CQMSYEditFragment extends BaseMSEditFragment<MSEditPresenterImp> {

    TextView tvSugggestedLocation;
    TextView tvSuggestedBatchFlag;

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_msy311_edit;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MSEditPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        super.initView();
        tvSugggestedLocation = mView.findViewById(R.id.cqzt_tv_suggested_location);
        tvSuggestedBatchFlag = mView.findViewById(R.id.cqzt_tv_suggested_batch_flag);
        //打开接收仓位和接收批次
        llRecLocation.setVisibility(View.VISIBLE);
        llRecBatch.setTag(View.VISIBLE);
    }

    @Override
    protected void initDataLazily() {

    }

    @Override
    public void initData() {
        super.initData();
        RefDetailEntity lineData = mRefData.billDetailList.get(mPosition);
        tvSugggestedLocation.setText(lineData.suggestLocation);
        tvSuggestedBatchFlag.setText(lineData.suggestBatch);
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
        result.suggestBatch = getString(tvSuggestedBatchFlag);
        result.suggestLocation = getString(tvSugggestedLocation);
        return result;
    }
}
