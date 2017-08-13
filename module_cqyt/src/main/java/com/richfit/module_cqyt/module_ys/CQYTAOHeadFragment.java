package com.richfit.module_cqyt.module_ys;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.richfit.sdk_wzrk.base_as_head.BaseASHeadFragment;
import com.richfit.sdk_wzrk.base_as_head.imp.ASHeadPresenterImp;

/**
 * Created by monday on 2017/7/5.
 */

public class CQYTAOHeadFragment extends BaseASHeadFragment<ASHeadPresenterImp> {

    @Override
    public void initPresenter() {
        mPresenter = new ASHeadPresenterImp(mActivity);
    }

    @Override
    public void initView() {
        llSupplier.setVisibility(View.VISIBLE);
        super.initView();
    }

    @Override
    public void initDataLazily() {

    }

    /**
     * 这里直接将103的缓存带出来
     * @param cacheFlag：缓存标志。有可能是Y,N或者TransId标识
     * @param transId：缓存id,用于删除缓存
     * @param refNum：单据号
     * @param refCodeId
     * @param refType
     * @param bizType：业务类型
     */
    @Override
    public void cacheProcessor(String cacheFlag, String transId, String refNum,
                               String refCodeId, String refType, String bizType) {
        if (!TextUtils.isEmpty(cacheFlag)) {
            if (mUploadMsgEntity != null && mPresenter != null && mPresenter.isLocal()) {
                mPresenter.getTransferInfo(mRefData, refCodeId, bizType, refType);
                return;
            }
            mPresenter.getTransferInfo(mRefData, refCodeId, bizType, refType);
        } else {
            bindCommonHeaderUI();
        }
    }

    @NonNull
    @Override
    protected String getMoveType() {
        return "1";
    }
}
