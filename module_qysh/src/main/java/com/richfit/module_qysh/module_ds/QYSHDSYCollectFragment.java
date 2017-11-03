package com.richfit.module_qysh.module_ds;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzck.base_ds_collect.BaseDSCollectFragment;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHDSYCollectFragment extends BaseDSCollectFragment<DSCollectPresenterImp> {


    @Override
    protected void initPresenter() {
        mPresenter = new DSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void initDataLazily() {
        if (mRefData == null) {
            showMessage("请先在抬头界面获取单据数据");
            return;
        }

        if("218".equals(mRefData.sapMoveType) && TextUtils.isEmpty(mRefData.deliveryTo)) {
            showMessage("请先在抬头界面输入接收方");
            return;
        }
        super.initDataLazily();
    }

    @Override
    protected void initView() {
        super.initView();
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if (batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
