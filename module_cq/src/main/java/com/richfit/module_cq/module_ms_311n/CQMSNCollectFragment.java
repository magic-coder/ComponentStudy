package com.richfit.module_cq.module_ms_311n;

import android.text.TextUtils;
import android.view.TextureView;
import android.widget.TextView;

import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzyk.base_msn_collect.BaseMSNCollectFragment;
import com.richfit.sdk_wzyk.base_msn_collect.imp.MSNCollectPresenterImp;

/**
 * 无参考的建议批次和建议仓位需要调用特定的接口去获取
 * Created by monday on 2017/12/5.
 */

public class CQMSNCollectFragment extends BaseMSNCollectFragment<MSNCollectPresenterImp> {

    TextView tvSugggestedLocation;
    TextView tvSuggestedBatchFlag;

    @Override
    public int getContentId() {
        return R.layout.cqzt_fragment_msn311_collect;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MSNCollectPresenterImp(mActivity);
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
    protected boolean checkHeaderData() {
        if (TextUtils.isEmpty(mRefData.workId)) {
            showMessage("请先选择工厂");
            return false;
        }

        if (TextUtils.isEmpty(mRefData.invId)) {
            showMessage("请先在抬头界面选择发出库位");
        }

        if (TextUtils.isEmpty(mRefData.recInvId)) {
            showMessage("请先在抬头界面选择接收库位");
            return false;
        }
        return true;
    }

    @Override
    public void loadInventoryComplete() {
        //获取建议仓位和批次
        InventoryQueryParam queryParam = provideInventoryQueryParam();
        InvEntity invEntity = mSendInvs.get(spSendInv.getSelectedItemPosition());
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
    protected boolean getWMOpenFlag() {
        return false;
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
}
