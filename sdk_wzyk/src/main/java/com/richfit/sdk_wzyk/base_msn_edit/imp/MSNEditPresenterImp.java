package com.richfit.sdk_wzyk.base_msn_edit.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzyk.base_msn_edit.IMSNEditPresenter;
import com.richfit.sdk_wzyk.base_msn_edit.IMSNEditView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2016/11/22.
 */

public class MSNEditPresenterImp extends BaseEditPresenterImp<IMSNEditView>
        implements IMSNEditPresenter {

    public MSNEditPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void uploadCollectionDataSingle(ResultEntity result) {
        mView = getView();
        Flowable<String> flowable;
        if (!TextUtils.isEmpty(result.recLocation) && !"barcode".equalsIgnoreCase(result.recLocation)) {
            //检查接收仓位
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("locationType", result.locationType);
            flowable = Flowable.zip(mRepository.getLocationInfo(result.queryType, result.recWorkId, result.recInvId, "",
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

                            }

                            @Override
                            public void _onNetWorkConnectError(String message) {
                                if (mView != null) {
                                    mView.networkConnectError(Global.RETRY_EDIT_DATA_ACTION);
                                }
                            }

                            @Override
                            public void _onCommonError(String message) {
                                if (mView != null) {
                                    mView.saveEditedDataFail(message);
                                }
                            }

                            @Override
                            public void _onServerError(String code, String message) {
                                if (mView != null) {
                                    mView.saveEditedDataFail(message);
                                }
                            }

                            @Override
                            public void _onComplete() {
                                if (mView != null) {
                                    mView.saveEditedDataSuccess("修改成功");
                                }
                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getTransferInfoSingle(String bizType, String materialNum, String userId, String workId, String invId, String recWorkId,
                                      String recInvId, String batchFlag, String refDoc, int refDocIem) {
        mView = getView();
        RxSubscriber<ReferenceEntity> subscriber =
                mRepository.getTransferInfoSingle("", "", bizType, "",
                        workId, invId, recWorkId, recInvId, materialNum, batchFlag, "", refDoc, refDocIem, userId)
                        .map(refData -> calcTotalQuantity(refData))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<ReferenceEntity>(mContext) {
                            @Override
                            public void _onNext(ReferenceEntity refData) {
                                if (mView != null) {
                                    mView.onBindCommonUI(refData, batchFlag);
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

    protected ReferenceEntity calcTotalQuantity(ReferenceEntity refData) {
        List<RefDetailEntity> billDetailList = refData.billDetailList;
        for (RefDetailEntity target : billDetailList) {
            HashSet<String> sendLocationSet = new HashSet<>();
            List<LocationInfoEntity> locationList = target.locationList;
            if (locationList == null || locationList.size() == 0)
                return refData;
            //保存所有不重复的发出仓位
            if (locationList != null && locationList.size() > 0) {
                for (LocationInfoEntity loc : locationList) {
                    if (!TextUtils.isEmpty(loc.location)) {
                        sendLocationSet.add(loc.location);
                    }
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
            //改变所有发出仓位的quantity
            for (LocationInfoEntity loc : locationList) {
                loc.quantity = locQuantityMap.get(loc.location);
            }

        }
        return refData;
    }



    @Override
    public void getInventoryInfo(String queryType, String workId, String invId, String workCode,
                                 String invCode, String storageNum, String materialNum, String materialId, String location, String batchFlag,
                                 String specialInvFlag, String specialInvNum, String invType,Map<String,Object> extraMap) {
        mView = getView();

        RxSubscriber<List<InventoryEntity>> subscriber =
                mRepository.getInventoryInfo(queryType, workId, invId, workCode, invCode, storageNum,
                        materialNum, materialId, "", "",
                        batchFlag, location, specialInvFlag, specialInvNum, invType,extraMap)
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
                                              String specialInvFlag, String specialInvNum, String invType,
                                              Map<String,Object> extraMap) {
        mView = getView();

        RxSubscriber<List<String>> subscriber =
                mRepository.getInventoryInfo(queryType, workId, invId, workCode, invCode, storageNum, materialNum,
                        materialId, "", "",
                        batchFlag, location, specialInvFlag, specialInvNum, invType,extraMap)
                        .filter(list -> list != null && list.size() > 0)
                        .map(list -> convert2Strings(list))
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<List<String>>(mContext) {
                            @Override
                            public void _onNext(List<String> list) {
                                if (mView != null) {
                                    mView.showRecLocations(list);
                                }
                            }

                            @Override
                            public void _onNetWorkConnectError(String message) {
                                if (mView != null) {
                                    mView.networkConnectError(Global.RETRY_LOAD_REC_INVENTORY_ACTION);
                                }
                            }

                            @Override
                            public void _onCommonError(String message) {
                                if (mView != null) {
                                    mView.loadRecLocationsFail(message);
                                }
                            }

                            @Override
                            public void _onServerError(String code, String message) {
                                if (mView != null) {
                                    mView.loadRecLocationsFail(message);
                                }
                            }

                            @Override
                            public void _onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getSuggestLocationAndBatchFlag(String workCode, String invCode, String materialNum, String queryType) {
        ResourceSubscriber<InventoryEntity> subscriber = mRepository.getSuggestInventoryInfo(workCode, invCode, materialNum, queryType, null)
                .filter(list -> list != null && list.size() > 0)
                .map(list -> list.get(0))
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<InventoryEntity>() {
                    @Override
                    public void onNext(InventoryEntity result) {
                        if (mView != null) {
                            mView.loadSuggestInfoSuccess(result.suggestLocation, result.suggestBatch);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.loadSuggestInfoFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

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
}
