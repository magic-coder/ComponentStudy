package com.richfit.sdk_wzck.base_dsn_head.imp;

import android.content.Context;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.sdk_wzck.base_dsn_head.IDSNHeadPresenter;
import com.richfit.sdk_wzck.base_dsn_head.IDSNHeadView;

import java.util.ArrayList;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/2/23.
 */

public abstract class DSNHeadPresenterImp extends BaseHeadPresenterImp<IDSNHeadView>
        implements IDSNHeadPresenter {

    public DSNHeadPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void deleteCollectionData(String refType, String bizType, String userId,
                                     String companyCode) {
        mView = getView();
        RxSubscriber<String> subscriber = mRepository.deleteCollectionData("", "", "", refType, bizType,
                userId, companyCode)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在删除缓存...") {
                    @Override
                    public void _onNext(String message) {
                        if (mView != null) {
                            mView.deleteCacheSuccess(message);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {

                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.deleteCacheFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.deleteCacheFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {

                    }
                });
        addSubscriber(subscriber);
    }

    @Override
    public void getWorks(int flag) {
        mView = getView();
        ResourceSubscriber<ArrayList<WorkEntity>> subscriber = mRepository.getWorks(flag)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<ArrayList<WorkEntity>>() {
                    @Override
                    public void onNext(ArrayList<WorkEntity> works) {
                        if (mView != null) {
                            mView.showWorks(works);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.loadWorksFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        addSubscriber(subscriber);
    }


}
