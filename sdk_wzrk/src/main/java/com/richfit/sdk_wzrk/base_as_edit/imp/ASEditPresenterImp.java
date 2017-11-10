package com.richfit.sdk_wzrk.base_as_edit.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzrk.base_as_edit.IASEditPresenter;
import com.richfit.sdk_wzrk.base_as_edit.IASEditView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * 在数据保存修改的数据之前检查上架仓位是否存在
 * Created by monday on 2016/11/19.
 */

public class ASEditPresenterImp extends BaseEditPresenterImp<IASEditView>
        implements IASEditPresenter {

    public ASEditPresenterImp(Context context) {
        super(context);
    }

    /**
     * 检查上架仓位
     * @param result:用户采集的数据(json格式)
     */
    @Override
    public void uploadCollectionDataSingle(ResultEntity result) {
        mView = getView();
        Flowable<String> flowable;
        if (!TextUtils.isEmpty(result.location) && !"barcode".equalsIgnoreCase(result.location)) {
           //如果仓位有值，并且不为默认值，那么说明已经填写了仓位，需要检查
            Map<String, Object> extraMap = new HashMap<>();
            extraMap.put("locationType", result.locationType);
            flowable = Flowable.zip(mRepository.getLocationInfo(result.queryType, result.workId, result.invId, "",
                    result.location, extraMap),
                    mRepository.uploadCollectionDataSingle(result), (s, s2) -> s + ";" + s2);
        } else {
            //意味着不上架
            flowable = mRepository.uploadCollectionDataSingle(result);
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
