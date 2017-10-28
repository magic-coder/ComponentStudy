package com.richfit.module_cqyt.module_xj.collect;

import android.content.Context;

import com.richfit.common_lib.lib_base_sdk.base_collect.BaseCollectPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/7/3.
 */

public class CQYTXJCollectPresenterImp extends BaseCollectPresenterImp<CQYTXJCollectContract.View>
        implements CQYTXJCollectContract.Presenter {

    public CQYTXJCollectPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getTransferInfo(final ReferenceEntity refData, String refCodeId, String bizType, String refType,
                                String userId, String workId, String invId, String recWorkId, String recInvId,Map<String,Object> extraMap) {
        mView = getView();

        mRepository.getTransferInfo("", "", bizType, refType, userId, workId, invId, recWorkId, recInvId,extraMap)
                .filter(data -> data != null && data.billDetailList.size() > 0
                        && data.billDetailList.get(0).locationList != null && data.billDetailList.get(0).locationList.size() >0)
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


    @Override
    public void deleteNode(String lineDeleteFlag, String transId, String transLineId, String locationId,
                           String refType, String bizType, int position, String companyCode) {
        ResourceSubscriber<String> subscriber = mRepository.deleteCollectionDataSingle(lineDeleteFlag, transId, transLineId,
                locationId, refType, bizType, "", Global.USER_ID, position, companyCode)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new ResourceSubscriber<String>() {

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable t) {
                        if (mView != null) {
                            mView.deleteNodeFail(t.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.deleteNodeSuccess(position);
                        }
                    }
                });
        addSubscriber(subscriber);
    }

}
