package com.richfit.module_mcq.module_ascx.detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.RefDetailEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/8/29.
 */

public class ASCXDetailPresenterImp extends BaseDetailPresenterImp<IASCXDetailView>
        implements IASCXDetailPresenter {

    public ASCXDetailPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getArrivalInfo(String createdBy, String creationDate, Map<String, Object> extraMap) {
        mView = getView();

        ResourceSubscriber<List<ReferenceEntity>> subscriber = mRepository.getArrivalInfo(createdBy, creationDate, extraMap)
                .filter(data -> data != null && data.size() > 0)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<List<ReferenceEntity>>(mContext,"正在查询,请稍后...") {
                    @Override
                    public void _onNext(List<ReferenceEntity> list) {
                        if (mView != null) {
                            mView.loadRefListSuccess(list);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {

                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.loadRefListFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.loadRefListFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {

                    }});
        addSubscriber(subscriber);
    }
}
