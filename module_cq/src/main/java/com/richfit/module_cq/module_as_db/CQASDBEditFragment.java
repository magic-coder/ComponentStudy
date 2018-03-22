package com.richfit.module_cq.module_as_db;

import android.os.Bundle;
import android.widget.TextView;

import com.richfit.data.constant.Global;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cq.R;
import com.richfit.module_cq.module_as_db.impl.CQASEditPresenterImpl;
import com.richfit.sdk_wzrk.base_as_edit.BaseASEditFragment;
import com.richfit.sdk_wzrk.base_as_edit.imp.ASEditPresenterImp;

/**
 * Created by monday on 2017/12/5.
 */

public class CQASDBEditFragment extends BaseASEditFragment<CQASEditPresenterImpl> {

    TextView tvSugggestedLocation;
    TextView tvSuggestedBatchFlag;

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_asdb_edit;
    }


    @Override
    protected void initPresenter() {
        mPresenter = new CQASEditPresenterImpl(mActivity);
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
        Bundle bundle = getArguments();
        String suggestLocation = bundle.getString(Global.EXTRA_SUGGEST_LOCATION_KEY);
        String suggestBatchFlag = bundle.getString(Global.EXTRA_SUGGEST_BATCH_FLAG_KEY);
        tvSugggestedLocation.setText(suggestLocation);
        tvSuggestedBatchFlag.setText(suggestBatchFlag);
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
        result.glf =  mRefData.glf;
        result.lyf =  mRefData.lyf;
        result.ckf =  mRefData.ckf;
        result.yfhj =  mRefData.yfhj;
        result.suggestLocation = getString(tvSugggestedLocation);
        result.suggestBatch = getString(tvSuggestedBatchFlag);
        return result;
    }
}
