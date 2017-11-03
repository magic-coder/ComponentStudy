package com.richfit.common_lib.lib_base_sdk.base_edit;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;

import java.util.HashMap;
import java.util.List;
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

    /**
     * 保存单条修改数据，注意这没有检查仓位，如果子类需要检查，请
     * 重写该方法，可以检查发出或者接收仓位
     * @param result:用户采集的数据(json格式)
     */
    @Override
    public void uploadCollectionDataSingle(ResultEntity result) {
        mView = getView();
        ResourceSubscriber<String> subscriber =
                mRepository.uploadCollectionDataSingle(result).compose(TransformerHelper.io2main())
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
}
