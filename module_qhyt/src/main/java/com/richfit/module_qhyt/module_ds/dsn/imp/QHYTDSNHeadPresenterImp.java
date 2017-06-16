package com.richfit.module_qhyt.module_ds.dsn.imp;

import android.content.Context;

import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzck.base_dsn_head.imp.DSNHeadPresenterImp;

import java.util.ArrayList;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/6/16.
 */

public class QHYTDSNHeadPresenterImp extends DSNHeadPresenterImp {

    public QHYTDSNHeadPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getAutoCompleteList(String workCode, String keyWord, int defaultItemNum, int flag,
                                    String bizType) {
        mView = getView();

        if (("26".equals(bizType) && "27".equals(bizType))) {
            mView.loadAutoCompleteFail("未找到合适业务类型");
            return;
        }
        final Flowable<ArrayList<SimpleEntity>> flowable = "26".equals(bizType) ? mRepository.getCostCenterList(workCode, keyWord, defaultItemNum, flag)
                : mRepository.getProjectNumList(workCode, keyWord, defaultItemNum, flag);

        ResourceSubscriber<ArrayList<String>> subscriber =
                flowable.filter(list -> list != null && list.size() > 0)
                        .map(list -> wrapper2Str(list))
                        .subscribeWith(new ResourceSubscriber<ArrayList<String>>() {
                            @Override
                            public void onNext(ArrayList<String> suppliers) {
                                if (mView != null) {
                                    mView.showAutoCompleteList(suppliers);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if (mView != null) {
                                    mView.loadAutoCompleteFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }
}
