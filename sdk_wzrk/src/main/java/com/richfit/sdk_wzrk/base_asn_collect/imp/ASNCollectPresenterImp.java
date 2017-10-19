package com.richfit.sdk_wzrk.base_asn_collect.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzrk.base_asn_collect.IASNCollectPresenter;
import com.richfit.sdk_wzrk.base_asn_collect.IASNCollectView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public void getLocationList(String workId, String workCode, String invId, String invCode,
                                String keyWord, int defaultItemNum, int flag,
                                boolean isDropDown) {
        mView = getView();

        if (TextUtils.isEmpty(workCode) && TextUtils.isEmpty(workId)) {
            mView.loadInventoryFail("获取到工厂信息");
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
                                    mView.showInventory(list);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.loadInventoryFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {
                                if(mView != null) {
                                    mView.loadInventoryComplete(isDropDown);
                                }
                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getTransferSingleInfo(String bizType, String materialNum, String userId, String workId,
                                      String invId, String recWorkId, String recInvId, String batchFlag,
                                      String refDoc, int refDocItem) {
        mView = getView();
        RxSubscriber<ReferenceEntity> subscriber =
                mRepository.getTransferInfoSingle("", "", bizType, "",
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

    @Override
    public void getDictionaryData(String... codes) {
        mView = getView();
        mRepository.getDictionaryData(codes)
                .filter(data -> data != null && data.size() > 0)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<Map<String,List<SimpleEntity>>>() {
                    @Override
                    public void onNext(Map<String,List<SimpleEntity>> data) {
                        if (mView != null) {
                            mView.loadDictionaryDataSuccess(data);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.loadDictionaryDataFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void checkLocation(String queryType, String workId, String invId, String batchFlag,
                              String location,Map<String,Object> extraMap) {
        mView = getView();
        if (TextUtils.isEmpty(workId) && mView != null) {
            mView.checkLocationFail("工厂为空");
            return;
        }

        if (TextUtils.isEmpty(invId) && mView != null) {
            mView.checkLocationFail("库存地点为空");
            return;
        }

        ResourceSubscriber<String> subscriber =
                mRepository.getLocationInfo(queryType, workId, invId, "", location,extraMap)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<String>() {
                            @Override
                            public void onNext(String s) {

                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.checkLocationFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {
                                if (mView != null) {
                                    mView.checkLocationSuccess(batchFlag, location);
                                }
                            }
                        });
        addSubscriber(subscriber);
    }

}
