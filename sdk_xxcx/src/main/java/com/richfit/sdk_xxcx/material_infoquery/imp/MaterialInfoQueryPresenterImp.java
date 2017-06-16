package com.richfit.sdk_xxcx.material_infoquery.imp;

import android.content.Context;

import com.richfit.common_lib.lib_mvp.BasePresenter;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.sdk_xxcx.material_infoquery.IMaterialInfoQueryPresenter;
import com.richfit.sdk_xxcx.material_infoquery.IMaterialInfoQueryView;

/**
 * Created by monday on 2017/3/16.
 */

public class MaterialInfoQueryPresenterImp extends BasePresenter<IMaterialInfoQueryView>
        implements IMaterialInfoQueryPresenter {

    IMaterialInfoQueryView mView;

    public MaterialInfoQueryPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getMaterialInfo(String queryType, String materialNum) {
        mView = getView();
        RxSubscriber<MaterialEntity> subscriber = mRepository.getMaterialInfo(queryType, materialNum)
                .compose(TransformerHelper.io2main())
                .subscribeWith(new RxSubscriber<MaterialEntity>(mContext, "正在查询...") {
                    @Override
                    public void _onNext(MaterialEntity materialEntity) {
                        if (mView != null) {
                            mView.querySuccess(materialEntity);
                        }
                    }

                    @Override
                    public void _onNetWorkConnectError(String message) {
                        if (mView != null) {
                            mView.networkConnectError(Global.RETRY_QUERY_MATERIAL_INFO);
                        }
                    }

                    @Override
                    public void _onCommonError(String message) {
                        if (mView != null) {
                            mView.queryFail(message);
                        }
                    }

                    @Override
                    public void _onServerError(String code, String message) {
                        if (mView != null) {
                            mView.queryFail(message);
                        }
                    }

                    @Override
                    public void _onComplete() {

                    }
                });
        addSubscriber(subscriber);
    }
}
