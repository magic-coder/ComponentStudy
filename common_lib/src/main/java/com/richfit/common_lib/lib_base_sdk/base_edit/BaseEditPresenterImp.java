package com.richfit.common_lib.lib_base_sdk.base_edit;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ResultEntity;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/4/6.
 */

public class BaseEditPresenterImp<V extends IBaseEditView> extends BasePresenter<V>
        implements IBaseEditPresenter<V> {

    protected V mView;

    public BaseEditPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void uploadCollectionDataSingle(ResultEntity result) {
        mView = getView();
        Flowable<String> flowable;
        if (!TextUtils.isEmpty(result.location) && "barcode".equalsIgnoreCase(result.location)) {
            //意味着不上架
            flowable = mRepository.uploadCollectionDataSingle(result);
        } else {
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("locationType", result.locationType);
            flowable = Flowable.concat(mRepository.getLocationInfo("04", result.workId, result.invId, "", result.location, extraMap),
                    mRepository.uploadCollectionDataSingle(result));
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
}
