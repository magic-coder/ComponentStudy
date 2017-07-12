package com.richfit.module_cqyt.module_xj.collect;

import android.content.Context;

import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;

import java.util.List;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/7/3.
 */

public class CQYTXJCollectPresenterImp extends BasePresenter<CQYTXJCollectContract.View>
        implements CQYTXJCollectContract.Presenter {

    CQYTXJCollectContract.View mView;

    public CQYTXJCollectPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void uploadCollectionDataSingle(ResultEntity result) {
        mView = getView();
        ResourceSubscriber<String> subscriber =
                mRepository.uploadCollectionDataSingle(result)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new RxSubscriber<String>(mContext) {
                            @Override
                            public void _onNext(String s) {

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
                                if (mView != null) {
                                    mView.saveCollectedDataSuccess();
                                }
                            }
                        });
        addSubscriber(subscriber);
    }

    @Override
    public void getTransferInfo(final ReferenceEntity refData, String refCodeId, String bizType, String refType,
                                String userId, String workId, String invId, String recWorkId, String recInvId) {
        mView = getView();

        mRepository.getTransferInfo("", "", bizType, refType, userId, workId, invId, recWorkId, recInvId)
                .filter(data -> data != null && data.billDetailList.size() > 0)
                .map(data -> data.billDetailList.get(0).locationList)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<List<LocationInfoEntity>>() {
                    @Override
                    public void onNext(List<LocationInfoEntity> locations) {
                        if (mView != null) {
                            mView.showLocations(locations);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.getTransferInfoFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


}
