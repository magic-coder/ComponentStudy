package com.richfit.module_mcq.module_ds.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.sdk_wzck.base_ds_collect.IDSCollectPresenter;
import com.richfit.sdk_wzck.base_ds_collect.imp.DSCollectPresenterImp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * 建议仓位以及建议实发数量的获取逻辑:
 * 1.获取库存；
 * 2.获取整单缓存;
 * 3.过滤出当前物料的缓存信息，将多个父节点下相同物料的相同仓位的实发数量合并;
 * 4.将相同仓位下的库存和实发数量计算，如果不为零则认为该仓位是建议仓位，
 * 如果为零，则计算下一个。
 * Created by monday on 2017/9/4.
 */

public class MCQDSCollectPresenterImp extends DSCollectPresenterImp
        implements IDSCollectPresenter {

    public MCQDSCollectPresenterImp(Context context) {
        super(context);
    }


    @Override
    public void getSuggestedLocation(String refCodeId, String bizType, String refType, String userId,
                                     String workId, String invId, String recWorkId, String recInvId,
                                     String queryType, String lineWorkId, String lineInvId,
                                     String lineWorkCode, String lineInvCode, String storageNum,
                                     String materialNum, String materialId, String location,
                                     String batchFlag, String specialInvFlag, String specialInvNum,
                                     String invType, Map<String, Object> extraMap) {

        mView = getView();

        //先获取库存，如果没有库存，那么直接走onError
        //获取库存由于是04需要先获取storageNum
        Flowable<List<InventoryEntity>> inventoryFlowable = mRepository.getStorageNum(lineWorkId, lineWorkCode, lineInvId, lineInvCode)
                .filter(SNum -> !TextUtils.isEmpty(SNum))
                .flatMap(SNum -> mRepository.getInventoryInfo(queryType, workId, invId,
                        lineWorkCode, lineInvCode, SNum, materialNum, materialId, "", "", batchFlag, location,
                        specialInvFlag, specialInvNum, invType, extraMap))
                .onErrorResumeNext(throwable -> {
                    return Flowable.just(new ArrayList<>());
                });

        Flowable<ReferenceEntity> transferInfoFlowable =
                mRepository.getTransferInfo("", refCodeId, bizType, refType,
                        "", "", "", "", "", extraMap).onErrorResumeNext(throwable -> {
                    //这里过滤掉未获取到缓存的错误
                    ReferenceEntity cache = new ReferenceEntity();
                    return Flowable.just(cache);
                });

        //计算建议仓位
        Flowable.zip(inventoryFlowable, transferInfoFlowable,
                (invList, cacheData) -> {
                    InventoryEntity suggestedLocation = new InventoryEntity();
                    if (invList == null || invList.size() == 0) {
                        //说明没有库存，那么直接回调error
                        return suggestedLocation;
                    }

                    //1. 计算该物料在所有仓位上已经出库的数量
                    List<RefDetailEntity> cacheDataList = cacheData.billDetailList;
                    List<RefDetailEntity> filterDataList = new ArrayList<>();

                    //如果缓存不为空,那么计算得到当前物料的缓存
                    if (cacheDataList != null) {
                        for (RefDetailEntity item : cacheDataList) {
                            if (item.materialId.equals(materialId)) {
                                filterDataList.add(item);
                            }
                        }
                    }

                    //说明过滤之后没有该物料的缓存,将第一个库存作为建议仓位
                    if (filterDataList.size() == 0) {
                        suggestedLocation = invList.get(0);
                        return suggestedLocation;
                    }

                    //2.合并该父节点下的所有location的已经出库的数量
                    //key为locationCombine,value为已经发出的数量quantity
                    final Map<String, String> locationMap = new HashMap<>();
                    for (RefDetailEntity item : filterDataList) {
                        List<LocationInfoEntity> locationList = item.locationList;
                        for (LocationInfoEntity locationItem : locationList) {
                            final String key = locationItem.locationCombine;
                            if (locationMap.containsKey(key)) {
                                //如果已经存在，更新value
                                String oldQuantity = locationMap.get(key);
                                float oldQuantityV = Float.parseFloat(oldQuantity);
                                float newQuantityV = Float.parseFloat(locationItem.quantity);
                                locationMap.put(key, String.valueOf(oldQuantityV + newQuantityV));
                            } else {
                                //如果不存在
                                locationMap.put(key, locationItem.quantity);
                            }
                        }
                    }

                    //3.计算建议仓位
                    for (InventoryEntity item : invList) {
                        final String key = item.locationCombine;
                        String quantity = locationMap.get(key);
                        if (!TextUtils.isEmpty(quantity)) {
                            //已经发出的数量
                            float quantityV = Float.parseFloat(quantity);
                            //库存数量
                            float invQuantityV = Float.parseFloat(item.invQuantity);
                            if (Float.compare(invQuantityV - quantityV, 0.0F) > 0) {
                                suggestedLocation = item;
                                break;
                            }
                        }
                    }
                    return suggestedLocation;
                })
                .flatMap(data -> {
                    if (data == null || TextUtils.isEmpty(data.locationCombine)) {
                        return Flowable.error(new Throwable("未获取到建议下架仓位"));
                    }
                    return Flowable.just(data);
                })
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<InventoryEntity>(mContext, "正在获取建议仓位...") {
                    @Override
                    public void _onNext(InventoryEntity data) {
                        if (mView != null) {
                            mView.getSuggestedLocationSuccess(data);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.getSuggestedLocationFail(message);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.getSuggestedLocationFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.getSuggestedLocationFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {
                        if (mView != null) {
                            mView.getSuggestedLocationComplete();
                        }
                    }
                });

    }
}
