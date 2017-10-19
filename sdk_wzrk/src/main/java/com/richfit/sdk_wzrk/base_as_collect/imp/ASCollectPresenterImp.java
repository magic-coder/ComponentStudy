package com.richfit.sdk_wzrk.base_as_collect.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzrk.base_as_collect.IASCollectPresenter;
import com.richfit.sdk_wzrk.base_as_collect.IASCollectView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 注意这里在获取缓存时候就已经检查了上架仓位
 * Created by monday on 2016/11/15.
 */

public class ASCollectPresenterImp extends BaseCollectPresenterImp<IASCollectView>
        implements IASCollectPresenter {

    public ASCollectPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getInvsByWorkId(String workId, int flag) {
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
                                    mView.loadInvFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {
                                if (mView != null) {
                                    mView.loadInvComplete();
                                }
                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getInventoryInfo(String queryType, String workId, String invId, String workCode, String invCode, String storageNum,
                                 String materialNum, String materialId, String location, String batchFlag,
                                 String specialInvFlag, String specialInvNum, String invType, String deviceId,
                                 Map<String, Object> extraMap, boolean isDropDown) {
        mView = getView();
        if (isLocal())
            return;
        ResourceSubscriber<List<String>> subscriber;
        if ("04".equals(queryType)) {
            subscriber = mRepository.getStorageNum(workId, workCode, invId, invCode)
                    .filter(num -> !TextUtils.isEmpty(num))
                    .flatMap(num -> mRepository.getInventoryInfo(queryType, workId, invId,
                            workCode, invCode, num, materialNum, materialId, "", "", batchFlag, location,
                            specialInvFlag, specialInvNum, invType, deviceId, extraMap))
                    .filter(list -> list != null && list.size() > 0)
                    .map(list -> changeInv2Locations(list))
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new InventorySubscriber(isDropDown));

        } else {
            subscriber = mRepository.getInventoryInfo(queryType, workId, invId,
                    workCode, invCode, storageNum, materialNum, materialId, "", "", batchFlag, location,
                    specialInvFlag, specialInvNum, invType, deviceId, extraMap)
                    .filter(list -> list != null && list.size() > 0)
                    .map(list -> changeInv2Locations(list))
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new InventorySubscriber(isDropDown));
        }
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

        private boolean isDropDown;

        public InventorySubscriber(boolean isDropDown) {
            this.isDropDown = isDropDown;
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
                mView.loadInventoryComplete(isDropDown);
            }
        }
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


    @Override
    public void getTransferInfoSingle(String refCodeId, String refType, String bizType,
                                      String refLineId, String batchFlag, String location,
                                      String refDoc, int refDocItem, String userId) {
        mView = getView();
        RxSubscriber<RefDetailEntity> subscriber =
                mRepository.getTransferInfoSingle(refCodeId, refType, bizType, refLineId, "", "", "", "", "",
                        batchFlag, location, refDoc, refDocItem, userId)
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
