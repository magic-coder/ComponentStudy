package com.richfit.module_cq.module_dsy;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.IDSCollectView;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by monday on 2017/12/5.
 */

public class CQDSCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {

    TextView tvSugggestedLocation;
    TextView tvSuggestedBatchFlag;

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_dsy_collect;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
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
        super.loadInventoryComplete();
        RefDetailEntity lineData = getLineData(mSelectedRefLineNum);
        InvEntity invEntity = mInvDatas.get(spInv.getSelectedItemPosition());
        InventoryQueryParam param = provideInventoryQueryParam();
        Map<String,Object> extraMap = new HashMap<>();
        extraMap.put("batchFlag",getString(etBatchFlag));
        mPresenter.getSuggestInventoryInfo(param.queryType, lineData.workCode, invEntity.invCode,
                getString(etMaterialNum),extraMap);
    }

    @Override
    public void getSuggestedLocationSuccess(InventoryEntity suggestedInventory) {
        super.getSuggestedLocationSuccess(suggestedInventory);
        tvSugggestedLocation.setText(suggestedInventory.suggestLocation);
        tvSuggestedBatchFlag.setText(suggestedInventory.suggestBatch);
    }


    @Override
    protected int getOrgFlag() {
        return 0;
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
