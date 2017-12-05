package com.richfit.module_qysh.module_ms.ms301;

import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;

import com.richfit.domain.bean.ResultEntity;
import com.richfit.module_qysh.R;
import com.richfit.sdk_wzyk.base_ms_collect.BaseMSCollectFragment;
import com.richfit.sdk_wzyk.base_ms_collect.imp.MSCollectPresenterImp;

import org.w3c.dom.Text;

/**
 * Created by monday on 2017/11/2.
 */

public class QYSHMSY301CollectFragment extends BaseMSCollectFragment<MSCollectPresenterImp> {

    @Override
    protected void initPresenter() {
        mPresenter = new MSCollectPresenterImp(mActivity);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void initView() {
        super.initView();
        //打开接收仓位
        llRecLocation.setVisibility(View.VISIBLE);
        //隐藏发出批次
        View batchFlag = mView.findViewById(R.id.ll_batch_flag);
        if (batchFlag != null) {
            batchFlag.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initDataLazily() {
		etMaterialNum.setEnabled(false);
        if (mRefData == null) {
            showMessage("请先在抬头界面获取单据数据");
            return;
        }

        if ("8230".equals(mRefData.workCode) && "8250".equals(mRefData.recWorkCode)
                && TextUtils.isEmpty(mRefData.deliveryTo)) {
            showMessage("请现在抬头界面输入接收方");
            return;
        }
        super.initDataLazily();
    }

    @Override
    public ResultEntity provideResult() {
        ResultEntity result = super.provideResult();
        result.deliveryTo = mRefData.deliveryTo;
        result.batchFlag = null;
        result.recBatchFlag = null;
        return result;
    }

    @Override
    public boolean checkCollectedDataBeforeSave() {
        if (TextUtils.isEmpty(getString(etRecLoc))) {
            showMessage("请输入接收仓位");
            return false;
        }
        return super.checkCollectedDataBeforeSave();
    }

    @Override
    protected int getOrgFlag() {
        return 0;
    }
}
