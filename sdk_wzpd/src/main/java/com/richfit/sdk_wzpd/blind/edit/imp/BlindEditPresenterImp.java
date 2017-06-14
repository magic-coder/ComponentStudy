package com.richfit.sdk_wzpd.blind.edit.imp;

import android.content.Context;

import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzpd.blind.edit.IBlindEditPresenter;
import com.richfit.sdk_wzpd.blind.edit.IBlindEditView;

/**
 * Created by monday on 2016/12/6.
 */

public class BlindEditPresenterImp extends BaseEditPresenterImp<IBlindEditView>
        implements IBlindEditPresenter {

    public BlindEditPresenterImp(Context context) {
        super(context);
    }


    @Override
    public void uploadCheckDataSingle(ResultEntity result) {
        mView = getView();
        addSubscriber(mRepository.uploadCheckDataSingle(result)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在保存盘点数据...") {
                    @Override
                    public void _onNext(String s) {

                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_UPLOAD_DATA_ACTION);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.saveEditedDataFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.saveEditedDataFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.saveEditedDataSuccess("修改成功!");
                        }
                    }
                }));
    }
}
