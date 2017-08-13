package com.richfit.sdk_wzpd.checkn.collect.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.sdk_wzpd.checkn.collect.ICNCollectPresenter;
import com.richfit.sdk_wzpd.checkn.collect.ICNCollectView;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by monday on 2017/3/3.
 */

public class CNCollectPresenterImp extends BaseCollectPresenterImp<ICNCollectView>
        implements ICNCollectPresenter {


    public CNCollectPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getCheckTransferInfoSingle(final String checkId, String location, String queryType,
                                           String materialNum, String bizType) {
        mView = getView();

        RxSubscriber<List<InventoryEntity>> subscriber =
                mRepository.getMaterialInfo(queryType, materialNum)
                        .flatMap(materialInfo -> {
                            if (materialInfo != null && !TextUtils.isEmpty(materialInfo.id)) {
                                return Flowable.just(materialInfo);
                            }
                            return Flowable.error(new Throwable("未获取到该物料信息"));
                        })
                        .flatMap(materialEntity -> mRepository.getCheckTransferInfoSingle(checkId, materialEntity.id, materialEntity.materialNum, location, bizType))
                        .filter(list -> list != null && list.size() > 0)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<List<InventoryEntity>>(mContext, "正在获取盘点库存信息...") {
                            @Override
                            public void _onNext(List<InventoryEntity> list) {
                                if (mView != null) {
                                    mView.loadInventorySuccess(list);
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
    public void uploadCheckDataSingle(ResultEntity result) {
        mView = getView();
        Flowable<String> flowable;
        final String checkLevel = result.checkLevel;
        if ("01".equals(checkLevel)) {
            flowable = Flowable.concat(mRepository.getLocationInfo("04", result.workId, result.invId, result.storageNum, result.location),
                    mRepository.uploadCheckDataSingle(result));
        } else {
            flowable = mRepository.uploadCheckDataSingle(result);
        }

        RxSubscriber<String> subscriber = flowable
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<String>(mContext, "正在保存本次盘点数量...") {
                    @Override
                    public void _onNext(String s) {
                        if (mView != null) {
                            mView.saveCollectedDataSuccess(s);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_TRANSFER_DATA_ACTION);
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
}
