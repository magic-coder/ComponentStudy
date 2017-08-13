package com.richfit.sdk_sxcl.basecollect.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.sdk_sxcl.basecollect.ILocQTCollectPresenter;
import com.richfit.sdk_sxcl.basecollect.ILocQTCollectView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/5/26.
 */

public class LocQTCollectPresenterImp extends BaseCollectPresenterImp<ILocQTCollectView>
        implements ILocQTCollectPresenter {


    public LocQTCollectPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getLocationList(String queryType, String workId, String invId, String workCode, String invCode,
                                String storageNum, String materialNum, String materialId, String location, String batchFlag,
                                String specialInvFlag, String specialInvNum, String invType, String deviceId, Map<String,Object> extraMap, boolean isDropDown) {
        mView = getView();
        if (isLocal())
            return;
        ResourceSubscriber<List<String>> subscriber;
        if ("04".equals(queryType)) {
            subscriber = mRepository.getStorageNum(workId, workCode, invId, invCode)
                    .filter(num -> !TextUtils.isEmpty(num))
                    .flatMap(num -> mRepository.getInventoryInfo(queryType, workId, invId,
                            workCode, invCode, num, materialNum, materialId, "", "", batchFlag, location,
                            specialInvFlag, specialInvNum, invType, deviceId,extraMap))
                    .filter(list -> list != null && list.size() > 0)
                    .map(list -> changeInv2Locations(list))
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new LocationListSubscriber(isDropDown));

        } else {
            subscriber = mRepository.getInventoryInfo(queryType, workId, invId,
                    workCode, invCode, storageNum, materialNum, materialId, "", "", batchFlag, location,
                    specialInvFlag, specialInvNum, invType, deviceId,extraMap)
                    .filter(list -> list != null && list.size() > 0)
                    .map(list -> changeInv2Locations(list))
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new LocationListSubscriber(isDropDown));
        }
        addSubscriber(subscriber);
    }

    @Override
    public void getInventoryInfo(String queryType, String workId, String invId, String workCode, String invCode, String storageNum,
                                 String materialNum, String materialId, String location, String batchFlag,
                                 String specialInvFlag, String specialInvNum, String invType, String deviceId,Map<String,Object> extraMap) {
        mView = getView();
        RxSubscriber<List<InventoryEntity>> subscriber;
        if ("04".equals(queryType)) {
            subscriber = mRepository.getStorageNum(workId, workCode, invId, invCode)
                    .filter(num -> !TextUtils.isEmpty(num))
                    .flatMap(num -> mRepository.getInventoryInfo(queryType, workId, invId,
                            workCode, invCode, num, materialNum, materialId, "", "", batchFlag, location,
                            specialInvFlag, specialInvNum, invType, deviceId,extraMap))
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new InventorySubscriber(mContext, "正在获取库存"));

        } else {
            subscriber = mRepository.getInventoryInfo(queryType, workId, invId,
                    workCode, invCode, storageNum, materialNum, materialId, "", "", batchFlag, location,
                    specialInvFlag, specialInvNum, invType, deviceId,extraMap)
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new InventorySubscriber(mContext, "正在获取库存"));
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

    protected class LocationListSubscriber extends ResourceSubscriber<List<String>> {

        private boolean isDropDown;

        public LocationListSubscriber(boolean isDropDown) {
            this.isDropDown = isDropDown;
        }

        @Override
        public void onNext(List<String> list) {
            if (mView != null) {
                mView.showLocationList(list);
            }
        }


        @Override
        public void onError(Throwable e) {
            if (mView != null) {
                mView.loadLocationListFail(e.getMessage());
            }
        }


        @Override
        public void onComplete() {
            if (mView != null) {
                mView.loadLocationListComplete(isDropDown);
            }
        }
    }


    protected class InventorySubscriber extends RxSubscriber<List<InventoryEntity>> {

        public InventorySubscriber(Context context, String msg) {
            super(context, msg);
        }

        @Override
        public void _onNext(List<InventoryEntity> list) {
            if (mView != null) {
                mView.showInventory(list);
            }
        }

        @Override
        public void _onNetWorkConnectError(String message) {
            if (mView != null) {
                mView.networkConnectError(Global.RETRY_LOAD_INVENTORY_ACTION);
            }
        }

        @Override
        public void _onCommonError(String message) {
            if (mView != null) {
                mView.loadInventoryFail(message);
            }
        }

        @Override
        public void _onServerError(String code, String message) {
            if (mView != null) {
                mView.loadInventoryFail(message);
            }
        }

        @Override
        public void _onComplete() {

        }
    }


    @Override
    public void checkLocation(String queryType, String workId, String invId, String batchFlag,
                              String location) {
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
                mRepository.getLocationInfo(queryType, workId, invId, "", location)
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

    @Override
    public void getTransferInfoSingle(String refCodeId, String refType, String bizType, String refLineId,
                                      String materialNum, String batchFlag, String location, String refDoc, int refDocItem, String userId) {
        mView = getView();
        RxSubscriber<RefDetailEntity> subscriber =
                mRepository.getTransferInfoSingle(refCodeId, refType, bizType, refLineId,
                        "", "", "", "", materialNum, batchFlag, location, refDoc, refDocItem, userId)
                        .filter(refData -> refData != null && refData.billDetailList != null)
                        .flatMap(refData -> getMatchedLineData(refLineId, refData))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<RefDetailEntity>(mContext) {
                            @Override
                            public void _onNext(RefDetailEntity cache) {
                                //获取缓存数据
                                if (mView != null) {
                                    mView.onBindCache(cache, batchFlag, location);
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
                                    mView.loadCacheFail(message);
                                }
                            }

                            @Override
                            public void _onServerError(String code, String message) {
                                if (mView != null) {
                                    mView.loadCacheFail(message);
                                }
                            }

                            @Override
                            public void _onComplete() {
                                if (mView != null) {
                                    mView.loadCacheSuccess();
                                }
                            }
                        });
        addSubscriber(subscriber);
    }

}
