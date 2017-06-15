package com.richfit.sdk_xxcx.inventory_query_y.head.imp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.sdk_xxcx.inventory_query_y.head.IInvYQueryHeadPresenter;
import com.richfit.sdk_xxcx.inventory_query_y.head.IInvYQueryHeadView;

/**
 * Created by monday on 2017/5/25.
 */

public class InvYQueryHeadPresenterImp extends BasePresenter<IInvYQueryHeadView>
        implements IInvYQueryHeadPresenter {

    IInvYQueryHeadView mView;

    public InvYQueryHeadPresenterImp(Context context) {
        super(context);
    }


    @Override
    public void getReference(@NonNull String refNum, @NonNull String refType, @NonNull String bizType,
                             @NonNull String moveType, @NonNull String refLineId, @NonNull String userId) {
        mView = getView();

        if (TextUtils.isEmpty(refNum) && mView != null) {
            mView.getReferenceFail("单据号为空,请重新输入");
            return;
        }

        if ((TextUtils.isEmpty(refType) || "-1".equals(refType)) && mView != null) {
            mView.getReferenceFail("请选选择单据类型");
            return;
        }
        RxSubscriber<ReferenceEntity> subscriber =
                mRepository.getReference(refNum, refType, bizType, moveType, refLineId, userId)
                        .filter(refData -> refData != null && refData.billDetailList != null && refData.billDetailList.size() > 0)
                        .map(refData -> addTreeInfo(refData))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<ReferenceEntity>(mContext) {
                            @Override
                            public void _onNext(ReferenceEntity refData) {
                                if (mView != null) {
                                    mView.getReferenceSuccess(refData);
                                }
                            }

                            @Override
                            public void _onNetWorkConnectError(String message) {
                                if (mView != null) {
                                    mView.networkConnectError(Global.RETRY_LOAD_REFERENCE_ACTION);
                                }
                            }

                            @Override
                            public void _onCommonError(String message) {
                                if (mView != null) {
                                    mView.getReferenceFail(message);
                                }
                            }

                            @Override
                            public void _onServerError(String code, String message) {
                                if (mView != null) {
                                    mView.getReferenceFail(message);
                                }
                            }

                            @Override
                            public void _onComplete() {
                                if(mView != null) {
                                    mView.getReferenceComplete();
                                }
                            }
                        });
        addSubscriber(subscriber);
    }
}
