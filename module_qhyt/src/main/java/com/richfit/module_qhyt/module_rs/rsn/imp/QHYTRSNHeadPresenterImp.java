package com.richfit.module_qhyt.module_rs.rsn.imp;

import android.content.Context;

import com.richfit.data.constant.Global;
import com.richfit.data.helper.CommonUtil;
import com.richfit.domain.bean.SimpleEntity;
import com.richfit.sdk_wzrs.base_rsn_head.imp.RSNHeadPresenterImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/6/22.
 */

public class QHYTRSNHeadPresenterImp extends RSNHeadPresenterImp {

    public QHYTRSNHeadPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getAutoCompleteList(String workCode, String keyWord, int defaultItemNum, int flag,
                                    String bizType) {
        mView = getView();

        if (("46".equals(bizType) && "47".equals(bizType))) {
            mView.loadAutoCompleteFail("未找到合适业务类型");
            return;
        }

        Flowable<Map<String, List<SimpleEntity>>> flowable = "46".equals(bizType) ?
                mRepository.getAutoComList(workCode, null, keyWord, defaultItemNum, flag, Global.COST_CENTER_DATA)
                : mRepository.getAutoComList(workCode, null, keyWord, defaultItemNum, flag, Global.PROJECT_NUM_DATA);

        ResourceSubscriber<Map<String,List<SimpleEntity>>> subscriber =
                flowable.filter(list -> list != null && list.size() > 0)

                        .subscribeWith(new ResourceSubscriber<Map<String,List<SimpleEntity>>>() {
                            @Override
                            public void onNext(Map<String,List<SimpleEntity>> data) {
                                if (mView != null) {
                                    mView.showAutoCompleteList(data);
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
