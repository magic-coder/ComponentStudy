package com.richfit.sdk_cwtz.collect.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_cwtz.collect.ILACollectPresenter;
import com.richfit.sdk_cwtz.collect.ILACollectView;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/2/7.
 */

public class LACollectPresenterImp extends BaseCollectPresenterImp<ILACollectView>
        implements ILACollectPresenter {

    public LACollectPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getMaterialInfo(String queryType, String materialNum, String workId) {
        mView = getView();

        RxSubscriber<MaterialEntity> subscriber =
                mRepository.getMaterialInfo(queryType, materialNum)
                        .flatMap(entity -> Flowable.just(addBatchManagerStatus(entity, workId)))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<MaterialEntity>(mContext, "正在获取物料信息...") {
                            @Override
                            public void _onNext(MaterialEntity materialEntity) {
                                if (mView != null) {
                                    mView.getMaterialInfoSuccess(materialEntity);
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
                                    mView.getMaterialInfoFail(message);
                                }
                            }

                            @Override
                            public void _onServerError(String code, String message) {
                                if (mView != null) {
                                    mView.getMaterialInfoFail(message);
                                }
                            }

                            @Override
                            public void _onComplete() {
                                if (mView != null) {
                                    mView.getMaterialInfoComplete();
                                }
                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getInventoryInfo(String queryType, String workId, String invId, String workCode,
                                 String invCode, String storageNum, String materialNum, String materialId,
                                 String materialGroup, String materialDesc,
                                 String batchFlag, String location, String specialInvFlag,
                                 String specialInvNum, String invType, Map<String, Object> extraMap) {
        mView = getView();

        RxSubscriber<List<InventoryEntity>> subscriber =
                mRepository.getInventoryInfo(queryType, workId, invId, workCode, invCode, storageNum,
                        materialNum, materialId, materialGroup,
                        materialDesc, batchFlag, location, specialInvFlag, specialInvNum, invType, extraMap)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<List<InventoryEntity>>(mContext, "正在获取库存信息...") {
                            @Override
                            public void _onNext(List<InventoryEntity> inventoryEntities) {
                                if (mView != null) {
                                    mView.showInventory(inventoryEntities);
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
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getTipInventoryInfo(String queryType, String workId, String invId, String workCode, String invCode, String storageNum, String materialNum, String materialId, String materialGroup, String materialDesc, String batchFlag, String location, String specialInvFlag, String specialInvNum, String invType, Map<String, Object> extraMap) {
        mView = getView();

        ResourceSubscriber<List<String>> subscriber = mRepository.getInventoryInfo(queryType, workId, invId, workCode, invCode, storageNum,
                materialNum, materialId, materialGroup,
                materialDesc, batchFlag, location, specialInvFlag, specialInvNum, invType, extraMap)
                .map(list -> changeInv2Locations(list))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<List<String>>() {
                    @Override
                    public void onNext(List<String> list) {
                        if (mView != null) {
                            mView.showTipInventory(list);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.loadTipInventoryFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.loadITipInventoryComplete();
                        }
                    }
                });
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

    /**
     * 注意仓位调整比较特殊，直接将数据保存到sap
     *
     * @param result:用户采集的数据(json格式)
     */
    @Override
    public void uploadCollectionDataSingle(ResultEntity result) {
        mView = getView();
        Flowable<String> flowable;
        if (!TextUtils.isEmpty(result.recLocation) && !"barcode".equalsIgnoreCase(result.recLocation)) {
            //检查目标仓位
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("locationType", result.locationType);
            flowable = Flowable.zip(mRepository.getLocationInfo(result.queryType, result.workId, result.invId, "", result.recLocation, extraMap),
                    mRepository.transferCollectionData(result), (s, s2) -> s + "\n" + s2);
        } else {
            //意味着不上架
            flowable = mRepository.transferCollectionData(result);
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
