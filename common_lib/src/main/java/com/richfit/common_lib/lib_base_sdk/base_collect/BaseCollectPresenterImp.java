package com.richfit.common_lib.lib_base_sdk.base_collect;

import android.content.Context;

import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;

import java.util.List;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/7/26.
 */

public abstract class BaseCollectPresenterImp<V extends IBaseCollectView> extends BasePresenter<V>
        implements IBaseCollectPresenter<V> {

    protected V mView;

    public BaseCollectPresenterImp(Context context) {
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
    public void uploadCheckDataSingle(ResultEntity result) {

    }


    @Override
    public MaterialEntity addBatchManagerStatus(MaterialEntity data, String workId) {
        if ("Y".equalsIgnoreCase(Global.BATCHMANAGERSTATUS)) {
            data.batchManagerStatus = true;
        } else if ("N".equalsIgnoreCase(Global.BATCHMANAGERSTATUS)) {
            data.batchManagerStatus = false;
        } else if ("T".equalsIgnoreCase(Global.BATCHMANAGERSTATUS)) {
            String batchManagerStatus = mRepository.getBatchManagerStatus(workId, data.id);
            //如果是X那么表示打开了批次管理
            data.batchManagerStatus = "X".equalsIgnoreCase(batchManagerStatus);
        }
        return data;
    }

    @Override
    public ReferenceEntity addBatchManagerStatus(ReferenceEntity refData, String workId) {
        if ("Y".equalsIgnoreCase(Global.BATCHMANAGERSTATUS)) {
            addBatchManagerStatus(refData.billDetailList, true);
        } else if ("N".equalsIgnoreCase(Global.BATCHMANAGERSTATUS)) {
            addBatchManagerStatus(refData.billDetailList, false);
        } else if ("T".equalsIgnoreCase(Global.BATCHMANAGERSTATUS)) {
            List<RefDetailEntity> list = refData.billDetailList;
            for (RefDetailEntity data : list) {
                String batchManagerStatus = mRepository.getBatchManagerStatus(workId, data.materialId);
                //如果是X那么表示打开了批次管理
                data.batchManagerStatus = "X".equalsIgnoreCase(batchManagerStatus);
            }
        }
        return refData;
    }
}
