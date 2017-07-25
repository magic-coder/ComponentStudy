package com.richfit.sdk_wzrk.base_asn_head.imp;

import android.content.Context;

import com.richfit.common_lib.lib_base_sdk.base_head.BaseHeadPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.domain.bean.WorkEntity;
import com.richfit.sdk_wzrk.base_asn_head.IASNHeadPresenter;
import com.richfit.sdk_wzrk.base_asn_head.IASNHeadView;

import java.util.ArrayList;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2016/11/16.
 */

public class ASNHeadPresenterImp extends BaseHeadPresenterImp<IASNHeadView>
        implements IASNHeadPresenter {

    protected IASNHeadView mView;

    public ASNHeadPresenterImp(Context context) {
        super(context);
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
                        if (mView != null) {
                            mView.loadWorksComplete();
                        }
                    }
                });
        addSubscriber(subscriber);
    }

    @Override
    public void getSupplierList(String workCode, String keyWord, int defaultItemNum, int flag) {
        mView = getView();
        ResourceSubscriber<ArrayList<SimpleEntity>> subscriber =
                mRepository.getSupplierList(workCode, keyWord, defaultItemNum, flag)
                        .filter(list -> list != null && list.size() > 0)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<ArrayList<SimpleEntity>>() {
                            @Override
                            public void onNext(ArrayList<SimpleEntity> list) {
                                if (mView != null) {
                                    mView.showSuppliers(list);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.loadSuppliersFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getProjectNumList(String workCode, String keyWord, int defaultItemNum, int flag, String bizType) {

    }


    @Override
    public void getMoveTypeList(int flag) {

    }


    @Override
    public void deleteCollectionData(String refType, String bizType, String userId, String companyCode) {
        mView = getView();
        RxSubscriber<String> subscriber = mRepository.deleteCollectionData("", "", "", refType, bizType, userId, companyCode)
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
}
