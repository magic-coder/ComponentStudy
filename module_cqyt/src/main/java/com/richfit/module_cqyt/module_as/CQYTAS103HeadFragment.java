package com.richfit.module_cqyt.module_as;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.richfit.module_cqyt.R;
import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * Created by monday on 2017/6/29.
 */

public class CQYTAS103HeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    EditText etDeliveryOrder;

    @Override
    public int getContentId() {
        return R.layout.cqyt_fragment_as103_head;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void initView() {
        super.initView();
        etDeliveryOrder = (EditText) mView.findViewById(R.id.cqyt_et_delivery_order);
        llSupplier.setVisibility(View.VISIBLE);
        llSendWork.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }

    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            mRefData.deliveryOrder = getString(etDeliveryOrder);
        }
    }

    @Override
    public void bindCommonHeaderUI() {
        super.bindCommonHeaderUI();
        if (mRefData != null) {
            etDeliveryOrder.setText(mRefData.deliveryOrder);
        }
    }

    @Override
    public void clearAllUIAfterSubmitSuccess() {
        super.clearAllUIAfterSubmitSuccess();
        clearCommonUI(etDeliveryOrder);
    }
}
