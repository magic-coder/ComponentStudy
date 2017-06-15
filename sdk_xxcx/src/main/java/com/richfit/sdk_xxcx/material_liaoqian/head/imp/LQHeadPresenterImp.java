package com.richfit.sdk_xxcx.material_liaoqian.head.imp;

import android.content.Context;

import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.sdk_xxcx.material_liaoqian.head.ILQHeadPresenter;
import com.richfit.sdk_xxcx.material_liaoqian.head.ILQHeadView;

/**
 * Created by monday on 2017/3/16.
 */

public class LQHeadPresenterImp extends BasePresenter<ILQHeadView>
        implements ILQHeadPresenter {

    ILQHeadView mView;

    public LQHeadPresenterImp(Context context) {
        super(context);
    }


    @Override
    public void getMaterialInfo(String queryType, String materialNum) {
        mView = getView();

        RxSubscriber<MaterialEntity> subscriber = mRepository.getMaterialInfo(queryType, materialNum)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<MaterialEntity>(mContext, "正在查询...") {
                    @Override
                    public void _onNext(MaterialEntity materialEntity) {
                        if (mView != null) {
                            mView.querySuccess(materialEntity);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_QUERY_MATERIAL_INFO);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.queryFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.queryFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {

                    }
                });

        addSubscriber(subscriber);
    }
}
