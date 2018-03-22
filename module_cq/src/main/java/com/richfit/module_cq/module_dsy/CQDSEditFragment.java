package com.richfit.module_cq.module_dsy;

import android.widget.TextView;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzck.base_ds_edit.BaseDSEditFragment;
import com.richfit.sdk_wzck.base_ds_edit.imp.DSEditPresenterImp;

/**
 * Created by monday on 2017/12/5.
 */

public class CQDSEditFragment extends BaseDSEditFragment<DSEditPresenterImp> {

    TextView tvSugggestedLocation;
    TextView tvSuggestedBatchFlag;

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_dsy_edit;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DSEditPresenterImp(mActivity);
    }

    @Override
    protected void initDataLazily() {

    }


    @Override
    public void initView() {
        super.initView();
        tvSugggestedLocation = mView.findViewById(R.id.cqzt_tv_suggested_location);
        tvSuggestedBatchFlag = mView.findViewById(R.id.cqzt_tv_suggested_batch_flag);
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
        result.suggestLocation = getString(tvSugggestedLocation);
        result.suggestBatch = getString(tvSuggestedBatchFlag);
        return result;
    }
}
