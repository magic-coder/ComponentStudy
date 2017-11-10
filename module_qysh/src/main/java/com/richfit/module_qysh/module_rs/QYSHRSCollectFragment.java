package com.richfit.module_qysh.module_rs;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzrk.base_as_collect.BaseASCollectFragment;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;

import butterknife.Optional;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHRSCollectFragment extends BaseASCollectFragment<ASCollectPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new ASCollectPresenterImp(mActivity);
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
    protected void initData() {

    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.batchFlag = null;
        result.recBatchFlag = null;
        return result;
    }
}
