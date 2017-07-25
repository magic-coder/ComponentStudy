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
        tvSendWork = (TextView) mView.findViewById(R.id.tv_send_work);
        llCreator.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
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
    public void clearAllUIAfterSubmitSuccess() {
       super.clearAllUIAfterSubmitSuccess();
        clearCommonUI(tvSendWork);
    }
}
