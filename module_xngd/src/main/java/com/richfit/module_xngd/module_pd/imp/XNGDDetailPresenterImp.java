package com.richfit.module_xngd.module_pd.imp;

import android.content.Context;

import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.sdk_wzpd.checkn.detail.imp.CNDetailPresenterImp;

/**
 * Created by monday on 2017/6/23.
 */

public class XNGDDetailPresenterImp extends CNDetailPresenterImp{

    public XNGDDetailPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void transferCheckData(String checkId, String userId, String bizType) {
        mView = getView();
        RxSubscriber<String> subscriber = mRepository.uploadCheckData(checkId, userId, bizType)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在过账...") {
                    @Override
                    public void _onNext(String s) {
                        if (mView != null) {
                            mView.showTransferedNum(s);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_TRANSFER_DATA_ACTION);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.transferCheckDataFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.transferCheckDataFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.transferCheckDataSuccess();
                        }
                    }
                });
        addSubscriber(subscriber);
    }

}
