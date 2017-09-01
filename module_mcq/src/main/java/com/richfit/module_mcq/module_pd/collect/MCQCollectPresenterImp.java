package com.richfit.module_mcq.module_pd.collect;

import android.content.Context;

import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ResultEntity;

import io.reactivex.Flowable;

/**
 * Created by monday on 2017/8/29.
 */

public class MCQCollectPresenterImp extends BaseCollectPresenterImp<IMCQBCCollectView>
        implements IMCQBCCollectPresenter {

    public MCQCollectPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void uploadCheckDataSingle(ResultEntity result) {
        mView = getView();
        //如果是01仓位级别，需要检查仓位是否存在
        final String checkLevel = result.checkLevel;
        Flowable<String> flowable;
        if ("01".equals(checkLevel)) {
            flowable = Flowable.concat(mRepository.getLocationInfo("04", result.workId, result.invId, result.storageNum, result.location),
                    mRepository.uploadCheckDataSingle(result));
        } else {
            flowable = mRepository.uploadCheckDataSingle(result);
        }

        RxSubscriber<String> subscriber = flowable.compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在保存本次盘点数量...") {
                    @Override
                    public void _onNext(String s) {
                        if (mView != null) {
                            mView.saveCollectedDataSuccess(s);
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
                            mView.saveCollectedDataFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.saveCollectedDataFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {

                    }
                });
        addSubscriber(subscriber);
    }
}
