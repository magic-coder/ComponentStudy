package com.richfit.module_qhyt.module_ys.edit.imp;

import android.content.Context;
import android.text.TextUtils;

import com.richfit.common_lib.lib_base_sdk.base_edit.BaseEditPresenterImp;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InvEntity;
import com.richfit.module_qhyt.module_ys.edit.IQHYTAOEditPresenter;
import com.richfit.module_qhyt.module_ys.edit.IQHYTAOEditView;

import java.util.ArrayList;

import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by monday on 2017/3/1.
 */

public class QHYTAOEditPresenterImp extends BaseEditPresenterImp<IQHYTAOEditView>
        implements IQHYTAOEditPresenter {

    public QHYTAOEditPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getInvsByWorkId(String workId,int flag) {
        mView = getView();
        if(TextUtils.isEmpty(workId) && mView != null) {
            mView.loadInvsFail("工厂Id为空");
            return;
        }
        ResourceSubscriber<ArrayList<InvEntity>> subscriber =
                mRepository.getInvsByWorkId(workId,flag)
                        .compose(TransformerHelper.io2main())
                        .subscribeWith(new ResourceSubscriber<ArrayList<InvEntity>>() {
                            @Override
                            public void onNext(ArrayList<InvEntity> invs) {
                                if(mView != null) {
                                    mView.showInvs(invs);
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                if(mView != null) {
                                    mView.loadInvsFail(t.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
        addSubscriber(subscriber);
    }
}
