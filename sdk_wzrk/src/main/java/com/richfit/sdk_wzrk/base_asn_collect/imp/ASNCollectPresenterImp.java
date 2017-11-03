package com.richfit.sdk_wzrk.base_asn_collect.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzrk.base_as_collect.imp.ASCollectPresenterImp;
import com.richfit.sdk_wzrk.base_asn_collect.IASNCollectPresenter;
import com.richfit.sdk_wzrk.base_asn_collect.IASNCollectView;

import java.util.ArrayList;
import java.util.HashSet;
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
    public void getInventoryInfo(String queryType, String workId, String invId, String workCode, String invCode, String storageNum,
                                 String materialNum, String materialId, String location, String batchFlag,
                                 String specialInvFlag, String specialInvNum, String invType,
                                 Map<String, Object> extraMap) {
        mView = getView();
        if (isLocal())
            return;
        ResourceSubscriber<List<String>> subscriber;
        if ("04".equals(queryType)) {
            subscriber = mRepository.getStorageNum(workId, workCode, invId, invCode)
                    .filter(num -> !TextUtils.isEmpty(num))
                    .flatMap(num -> mRepository.getInventoryInfo(queryType, workId, invId,
                            workCode, invCode, num, materialNum, materialId, "", "", batchFlag, location,
                            specialInvFlag, specialInvNum, invType, extraMap))
                    .filter(list -> list != null && list.size() > 0)
                    .map(list -> changeInv2Locations(list))
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new InventorySubscriber());

        } else {
            subscriber = mRepository.getInventoryInfo(queryType, workId, invId,
                    workCode, invCode, storageNum, materialNum, materialId, "", "", batchFlag, location,
                    specialInvFlag, specialInvNum, invType, extraMap)
                    .filter(list -> list != null && list.size() > 0)
                    .map(list -> changeInv2Locations(list))
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new InventorySubscriber());
        }
        addSubscriber(subscriber);
    }


    private List<String> changeInv2Locations(List<InventoryEntity> invs) {
        List<String> locations = new ArrayList<>();
        HashSet<String> set = new HashSet<>();
        for (InventoryEntity data : invs) {
            if (!set.contains(data.location)) {
                set.add(data.location);
                locations.add(data.location);
            }
        }
        return locations;
    }

    protected class InventorySubscriber extends ResourceSubscriber<List<String>> {

        public InventorySubscriber() {
        }

        @Override
        public void onNext(List<String> list) {
            if (mView != null) {
                mView.showInventory(list);
            }
        }


        @Override
        public void onError(Throwable e) {
            if (mView != null) {
                mView.loadInventoryFail(e.getMessage());
            }
        }


        @Override
        public void onComplete() {
            if (mView != null) {
                mView.loadInventoryComplete();
            }
        }
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
