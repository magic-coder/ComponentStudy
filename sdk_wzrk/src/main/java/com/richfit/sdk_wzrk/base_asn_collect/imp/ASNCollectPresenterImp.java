package com.richfit.sdk_wzrk.base_asn_collect.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.sdk_wzrk.base_asn_collect.IASNCollectPresenter;
import com.richfit.sdk_wzrk.base_asn_collect.IASNCollectView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2016/11/27.
 */

public class ASNCollectPresenterImp extends BaseCollectPresenterImp<IASNCollectView>
        implements IASNCollectPresenter {

    public ASNCollectPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getInvsByWorks(String workId, int flag) {
        mView = getView();
        ResourceSubscriber<ArrayList<InvEntity>> subscriber =
                mRepository.getInvsByWorkId(workId, flag)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<ArrayList<InvEntity>>() {
                            @Override
                            public void onNext(ArrayList<InvEntity> list) {
                                if (mView != null) {
                                    mView.showInvs(list);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.loadInvsFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getLocationList(String workId, String workCode, String invId, String invCode, String keyWord, int defaultItemNum, int flag,
                                boolean isDropDown) {
        mView = getView();

        if (TextUtils.isEmpty(workCode) && TextUtils.isEmpty(workId)) {
            mView.getLocationListFail("获取到工厂信息");
            return;
        }

        ResourceSubscriber<List<String>> subscriber =
                mRepository.getLocationList(workId, workCode, invId, invCode, keyWord, defaultItemNum, flag)
                        .filter(list -> list != null && list.size() > 0)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<List<String>>() {
                            @Override
                            public void onNext(List<String> list) {
                                if (mView != null) {
                                    mView.getLocationListSuccess(list, isDropDown);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.getLocationListFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getTransferSingleInfo(String bizType, String materialNum, String userId, String workId,
                                      String invId, String recWorkId, String recInvId, String batchFlag,
                                      String refDoc, int refDocItem) {
        mView = getView();
        RxSubscriber<ReferenceEntity> subscriber = mRepository.getTransferInfoSingle("", "", bizType, "",
                workId, invId, recWorkId, recInvId, materialNum, batchFlag, "", refDoc, refDocItem, userId)
                .filter(refData -> refData != null && refData.billDetailList.size() > 0)
                .flatMap(refData -> Flowable.just(addBatchManagerStatus(refData)))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<ReferenceEntity>(mContext) {
                    @Override
                    public void _onNext(ReferenceEntity refData) {
                        if (mView != null) {
                            mView.bindCommonCollectUI(refData, batchFlag);
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

                    }
                });
        addSubscriber(subscriber);
    }
}
