package com.richfit.module_cq.module_ms_315;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.richfit.domain.bean.InventoryQueryParam;
import com.richfit.module_cq.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

/**
 * 注意这里使用的313的布局文件，因为长庆的移库都是在标准的基础上增加了一个件数的字段
 * Created by monday on 2017/6/30.
 */

public class CQMSY315CollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
    }

    @Override
    protected void initView() {
        super.initView();
        TextView tvInvName = (TextView) mView.findViewById(R.id.tv_inv_name);
        tvWorkName.setText("工厂");
        tvActQuantityName.setText("应收数量");
        tvInvName.setText("接收库位");
        tvLocationName.setText("接收仓位");
        tvBatchFlagName.setText("接收批次");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected InventoryQueryParam provideInventoryQueryParam() {
        InventoryQueryParam queryParam = super.provideInventoryQueryParam();
        queryParam.queryType = "01";
        return queryParam;
    }

}
