package com.richfit.module_cqyt.module_ms.ubsto;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzck.base_ds_head.BaseDSHeadFragment;
import com.richfit.sdk_wzck.base_ds_head.imp.DSHeadPresenterImp;

import java.util.List;

/**
 * Created by monday on 2017/7/4.
 */

public class CQYTUbstoHeadFragment extends BaseDSHeadFragment<DSHeadPresenterImp> {

    Spinner spShopCondition;
    TextView tvSendWork;

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
        spShopCondition = (Spinner) mView.findViewById(R.id.cqyt_sp_shop_condition);
        tvSendWork = (TextView) mView.findViewById(R.id.tv_send_work);
        llCreator.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        List<String> items = getStringArray(R.array.cqyt_shop_conditions);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, items);
        spShopCondition.setAdapter(adapter);
    }


    @Override
    public void initDataLazily() {

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
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            mRefData.shopCondition = spShopCondition.getSelectedItemPosition() == 0 ? "01" : "02";
        }
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {
       super.clearAllUIAfterSubmitSuccess();
        clearCommonUI(tvSendWork);
    }
}
