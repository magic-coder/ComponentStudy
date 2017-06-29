package com.richfit.module_xngd.module_ys;

import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.richfit.module_xngd.R;
import com.richfit.sdk_wzys.basehead.BaseApprovalHeadFragment;
import com.richfit.sdk_wzys.basehead.imp.ApprovalHeadPresenterImp;

import java.util.ArrayList;

/**
 * Created by monday on 2017/6/26.
 */

public class XNGDAOHeadFragment extends BaseApprovalHeadFragment {

    private Spinner spInvFlag;

    @Override
    public void initPresenter() {
        mPresenter = new ApprovalHeadPresenterImp(mActivity);
    }

    @Override
    public int getContentId() {
        return R.layout.xngd_fragment_ao_head;
    }

    @Override
    public void initDataLazily() {

    }

    @Override
    public void initView() {
        super.initView();
        spInvFlag = (Spinner) mView.findViewById(R.id.xngd_sp_inv_flag);
    }

    @Override
    public void initData() {
        super.initData();
        ArrayList<String> items = new ArrayList<>();
        items.add("应急");
        items.add("非应急");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity, R.layout.item_simple_sp, items);
        spInvFlag.setAdapter(adapter);
    }


    @Override
    public void _onPause() {
        super._onPause();
        if (mRefData != null) {
            //应急为0
            mRefData.invFlag = String.valueOf(spInvFlag.getSelectedItemPosition());
        }
    }


    @NonNull
    @Override
    protected String getMoveType() {
        return null;
    }
}
