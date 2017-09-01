package com.richfit.module_cqyt.module_ms.ubsto;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/7/4.
 */

public class CQYTUbstoHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {

    TextView tvSendWork;
    //装运条件
    Spinner spShopCondition;

    List<SimpleEntity> mShopConditions;

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_ubsto_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new DSHeadPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        super.initView();
        tvSendWork = (TextView) mView.findViewById(R.id.tv_send_work);
        llCreator.setVisibility(View.GONE);
        spShopCondition = mView.findViewById(R.id.cqyt_sp_shop_condition);
    }

    @Override
    public void initData() {
        mPresenter.getDictionaryData("shopCondition");
    }


    @Override
    public void initDataLazily() {

    }

    @Override
    public void loadDictionaryDataSuccess(Map<String, List<SimpleEntity>> data) {
        List<SimpleEntity> simpleEntities = data.get("shopCondition");
        if (simpleEntities == null || simpleEntities.size() == 0) {
            return;
        }
        if (mShopConditions == null) {
            mShopConditions = new ArrayList<>();
        }
        mShopConditions.clear();
        mShopConditions.addAll(simpleEntities);
        List<String> list = CommonUtil.toStringArray(mShopConditions);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, list);
        spShopCondition.setAdapter(adapter);
    }

    @Override
    public void bindCommonHeaderUI() {
        super.bindCommonHeaderUI();
        if (mRefData != null && mRefData.billDetailList != null && mRefData.billDetailList.size() > 0) {
            tvSendWork.setText(mRefData.billDetailList.get(0).workCode);
        }
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "3";
    }


    @Override
    public void clearAllUIAfterSubmitSuccess() {
        super.clearAllUIAfterSubmitSuccess();
        clearCommonUI(tvSendWork);
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            mRefData.shopCondition = mShopConditions.get(spShopCondition.getSelectedItemPosition()).code;
        }
    }
}
