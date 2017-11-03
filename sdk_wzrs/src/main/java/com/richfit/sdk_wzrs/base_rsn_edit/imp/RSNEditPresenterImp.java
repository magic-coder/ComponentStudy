package com.richfit.sdk_wzrs.base_rsn_edit.imp;

import android.content.Context;

import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzrs.base_rsn_edit.IRSNEditPresenter;
import com.richfit.sdk_wzrs.base_rsn_edit.IRSNEditView;

import java.util.List;
import java.util.Map;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/3/2.
 */

public class RSNEditPresenterImp extends BaseEditPresenterImp<IRSNEditView>
        implements IRSNEditPresenter {


    public RSNEditPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getTransferInfoSingle(String bizType, String materialNum, String userId, String workId, String invId, String recWorkId, String recInvId, String batchFlag,
                                      String refDoc, int refDocItem) {
        mView = getView();
        RxSubscriber<ReferenceEntity> subscriber = mRepository.getTransferInfoSingle("", "", bizType, "",
                workId, invId, recWorkId, recInvId, materialNum, batchFlag, "", refDoc, refDocItem, userId)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<ReferenceEntity>(mContext) {
                    @Override
                    public void _onNext(ReferenceEntity refData) {
                        if (mView != null) {
                            mView.onBindCommonUI(refData, batchFlag);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_LOAD_SINGLE_CACHE_ACTION);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.loadTransferSingleInfoFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.loadTransferSingleInfoFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.loadTransferSingeInfoComplete();
                        }
                    }
                });
        addSubscriber(subscriber);
    }
}
