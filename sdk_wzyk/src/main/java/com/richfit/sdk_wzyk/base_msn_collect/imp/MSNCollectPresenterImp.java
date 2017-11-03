package com.richfit.sdk_wzyk.base_msn_collect.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.common_lib.utils.L;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzyk.base_msn_collect.IMSNCollectPresenter;
import com.richfit.sdk_wzyk.base_msn_collect.IMSNCollectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2016/11/20.
 */

public class MSNCollectPresenterImp extends BaseCollectPresenterImp<IMSNCollectView>
        implements IMSNCollectPresenter {

    public MSNCollectPresenterImp(Context context) {
        super(context);
    }

    /**
     * 保存单条，检查接收仓位
     * @param result:用户采集的数据(json格式)
     */
    @Override
    public void uploadCollectionDataSingle(ResultEntity result) {
        mView = getView();

        Flowable<String> flowable;
        if (!TextUtils.isEmpty(result.recLocation) && !"barcode".equalsIgnoreCase(result.recLocation)) {
            //如果检查的接收仓位那么需要使用接收工厂和接收库存地点
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("locationType", result.locationType);
            flowable = Flowable.zip(mRepository.getLocationInfo("04", result.recWorkId, result.recInvId, "",
                    result.recLocation, extraMap),
                    mRepository.uploadCollectionDataSingle(result), (s, s2) -> s + ";" + s2);
        } else {
            //意味着不上架
            flowable =    mRepository.uploadCollectionDataSingle(result);
        }

        ResourceSubscriber<String> subscriber =
                flowable.compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<String>(mContext) {
                            @Override
                            public void _onNext(String s) {
                                if (mView != null) {
                                    mView.saveCollectedDataSuccess(s);
                                }
                            }

                            @Override
                            public void _onNetWorkConnectError(String message) {
                                if (mView != null) {
                                    mView.networkConnectError(Global.RETRY_SAVE_COLLECTION_DATA_ACTION);
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

    @Override
    public void getSendInvsByWorks(String workId, int flag) {
        mView = getView();
        ResourceSubscriber<ArrayList<InvEntity>> subscriber =
                mRepository.getInvsByWorkId(workId, flag)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<ArrayList<InvEntity>>() {
                            @Override
                            public void onNext(ArrayList<InvEntity> list) {
                                if (mView != null) {
                                    mView.showSendInvs(list);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.loadSendInvsFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getTransferInfoSingle(String bizType, String materialNum, String userId, String workId,
                                      String invId, String recWorkId, String recInvId, String batchFlag,
                                      String refDoc, int refDocItem) {
        mView = getView();
        RxSubscriber<ReferenceEntity> subscriber = mRepository.getTransferInfoSingle("", "", bizType, "",
                workId, invId, recWorkId, recInvId, materialNum, batchFlag, "", refDoc, refDocItem, userId)
                .filter(refData -> refData != null && refData.billDetailList.size() > 0)
                .flatMap(refData -> Flowable.just(addBatchManagerStatus(refData,workId)))
                .map(refData -> calcTotalQuantity(refData))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<ReferenceEntity>(mContext, "正在获取缓存信息...") {
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
                        if (mView != null) {
                            mView.loadTransferSingleInfoComplete();
                        }
                    }
                });
        addSubscriber(subscriber);
    }

    private ReferenceEntity calcTotalQuantity(ReferenceEntity refData) {
        List<RefDetailEntity> billDetailList = refData.billDetailList;
        for (RefDetailEntity target : billDetailList) {
            HashSet<String> sendLocationSet = new HashSet<>();
            List<LocationInfoEntity> locationList = target.locationList;
            if (locationList == null || locationList.size() == 0)
                return refData;
            //保存所有不重复的发出仓位
            for (LocationInfoEntity loc : locationList) {
                if (!TextUtils.isEmpty(loc.location)) {
                    sendLocationSet.add(loc.location);
                }
            }


            //计算发出仓位的所有接收仓位的数量作为该发出仓位的仓位数量
            HashMap<String, String> locQuantityMap = new HashMap<>();
            for (String sendLocation : sendLocationSet) {
                //将缓存中相同的发出仓位的所有接收仓位的仓位数量累加
                float totalQuantity = 0;
                for (LocationInfoEntity loc : locationList) {
                    if (sendLocation.equalsIgnoreCase(loc.location)) {
                        totalQuantity += CommonUtil.convertToFloat(loc.quantity, 0.0F);
                    }
                }
                locQuantityMap.put(sendLocation, String.valueOf(totalQuantity));
            }
            L.e("发出仓位的仓位数量 = " + locQuantityMap);
            //改变所有发出仓位的quantity
            for (LocationInfoEntity loc : locationList) {
                loc.quantity = locQuantityMap.get(loc.location);
            }

        }
        return refData;
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
    public void getInventoryInfo(String queryType, String workId, String invId, String workCode,
                                 String invCode, String storageNum, String materialNum, String materialId, String location, String batchFlag,
                                 String specialInvFlag, String specialInvNum, String invType,Map<String,Object> extraMap) {
        mView = getView();

        RxSubscriber<List<InventoryEntity>> subscriber =
                mRepository.getInventoryInfo(queryType, workId, invId, workCode, invCode, storageNum, materialNum,
                        materialId, "", "", batchFlag, location,
                        specialInvFlag, specialInvNum, invType,extraMap)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<List<InventoryEntity>>(mContext) {
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
                                if (mView != null) {
                                    mView.loadInventoryComplete();
                                }
                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getInventoryInfoOnRecLocation(String queryType, String workId, String invId, String workCode,
                                              String invCode, String storageNum, String materialNum,
                                              String materialId, String location, String batchFlag,
                                              String specialInvFlag, String specialInvNum,
                                              String invType,Map<String,Object> extraMap) {
        mView = getView();


        ResourceSubscriber<ArrayList<String>> subscriber = mRepository.getInventoryInfo(queryType, workId, invId, workCode, invCode, storageNum, materialNum,
                materialId, "", "", batchFlag, location,
                specialInvFlag, specialInvNum, invType, extraMap)
                .filter(list -> list != null && list.size() > 0)
                .map(list -> convert2Strings(list))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<ArrayList<String>>() {
                    @Override
                    public void onNext(ArrayList<String> strings) {
                        if (mView != null) {
                            mView.showRecLocations(strings);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.loadRecLocationsFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.loadRecLocationsComplete();
                        }
                    }
                });
        addSubscriber(subscriber);
    }

    private ArrayList<String> convert2Strings(List<InventoryEntity> list) {
        ArrayList<String> tmp = new ArrayList<>();
        for (InventoryEntity item : list) {
            tmp.add(item.location);
        }
        return tmp;
    }

    @Override
    public void checkWareHouseNum(boolean isOpenWM, String sendWorkId, String sendInvCode,
                                  String recWorkId, String recInvCode, int flag) {
        mView = getView();
        mRepository.checkWareHouseNum(sendWorkId, sendInvCode, recWorkId, recInvCode, flag)
                .compose(TransformerHelper.io2main())
                .subscribe(new ResourceSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.checkWareHouseFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.checkWareHouseSuccess();
                        }
                    }
                });
    }

    @Override
    public void getDeviceInfo(String deviceId) {
        mView = getView();

        mRepository.getDeviceInfo(deviceId)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<ResultEntity>() {
                    @Override
                    public void onNext(ResultEntity result) {
                        if (mView != null) {
                            mView.getDeviceInfoSuccess(result);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.getDeviceInfoFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.getDeviceInfoComplete();
                        }
                    }
                });
    }
}
