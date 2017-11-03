package com.richfit.module_xngd.module_sxcl.imp;

import android.content.Context;

import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.common_lib.utils.SPrefUtil;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.sdk_sxcl.basedetail.imp.LocQTDetailPresenterImp;

import java.util.Map;

import io.reactivex.Flowable;

/**
 * Created by monday on 2017/10/18.
 */

public class XNGDLocDetailPresenterImp extends LocQTDetailPresenterImp {

    public XNGDLocDetailPresenterImp(Context context) {
        super(context);
    }


    @Override
    public void submitData2BarcodeSystem(String refCodeId, String transId, String bizType, String refType, String userId, String voucherDate,
                                         String transToSap, Map<String, Object> extraHeaderMap) {
        mView = getView();
        RxSubscriber<String> subscriber =mRepository.uploadCollectionData(refCodeId, transId, bizType, refType, -1, voucherDate, "", userId, extraHeaderMap)
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
