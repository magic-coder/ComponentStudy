package com.richfit.module_xngd.module_ds.dsn.imp;

import android.content.Context;

import com.richfit.common_lib.lib_rx.RetryWhenNetworkException;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.sdk_wzck.base_dsn_detail.imp.DSNDetailPresenterImp;

import java.util.Map;

/**
 * Created by monday on 2017/6/20.
 */

public class XNGDDSNDetailPresenterImp extends DSNDetailPresenterImp {

    public XNGDDSNDetailPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void submitData2BarcodeSystem(String refCodeId,String transId, String bizType, String refType, String userId, String voucherDate,
                                         String transToSapFlag, Map<String, Object> extraHeaderMap) {
        mView = getView();
        RxSubscriber<String> subscriber = mRepository.uploadCollectionData(refCodeId, transId, bizType, refType, -1, voucherDate, "", userId,extraHeaderMap)
                .retryWhen(new RetryWhenNetworkException(3, 3000))
                .doOnError(str -> SPrefUtil.saveData(bizType + refType, "0"))
                .doOnComplete(() -> SPrefUtil.saveData(bizType + refType, "1"))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在过账...") {
                    @Override
                    public void _onNext(String message) {
                        if (mView != null) {
                            mView.saveMsgFowShow(message);
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
                            mView.submitBarcodeSystemFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.submitBarcodeSystemFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.submitBarcodeSystemSuccess();
                        }
                    }
                });
        addSubscriber(subscriber);
    }
}
