package com.richfit.sdk_xxcx.material_liaoqian.detail.imp;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.richfit.common_lib.lib_base_sdk.base_detail.BaseDetailPresenterImp;
import com.richfit.common_lib.lib_rx.RxSubscriber;
import com.richfit.data.constant.Global;
import com.richfit.data.helper.TransformerHelper;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.sdk_xxcx.material_liaoqian.detail.ILQDetailPresenter;
import com.richfit.sdk_xxcx.material_liaoqian.detail.ILQDetailView;

import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/3/16.
 */

public class LQDetailPresenterImp extends BaseDetailPresenterImp<ILQDetailView>
        implements ILQDetailPresenter {

    ILQDetailView mView;

    public LQDetailPresenterImp(Context context) {
        super(context);
    }

    @Override
    public void getInventoryInfo(String queryType, String workId, String invId, String workCode, String invCode,
                                 String storageNum, String materialNum, String materialId, String location,
                                 String batchFlag, String specialInvFlag, String specialInvNum, String invType,String deviceId,
                                 Map<String,Object> extraMap) {

        mView = getView();
        RxSubscriber<List<InventoryEntity>> subscriber;
        if ("04".equals(queryType)) {
            subscriber = mRepository.getStorageNum(workId, workCode, invId, invCode)
                    .filter(num -> !TextUtils.isEmpty(num))
                    .flatMap(num -> mRepository.getInventoryInfo(queryType, workId, invId,
                            workCode, invCode, num, materialNum, materialId, "", "", batchFlag,
                            location, specialInvFlag, specialInvNum, invType,deviceId,extraMap))
                    .filter(res -> res != null && res.size() > 0)
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new InventorySubscriber(mContext, "正在获取库存"));

        } else {
            subscriber = mRepository.getInventoryInfo(queryType, workId, invId,
                    workCode, invCode, storageNum, materialNum, materialId, "", "",
                    batchFlag, location, specialInvFlag, specialInvNum, invType,deviceId,extraMap)
                    .filter(res -> res != null && res.size() > 0)
                    .compose(TransformerHelper.io2main())
                    .subscribeWith(new InventorySubscriber(mContext, "正在获取库存"));
        }
        addSubscriber(subscriber);
    }

    protected class InventorySubscriber extends RxSubscriber<List<InventoryEntity>> {

        public InventorySubscriber(Context context, String msg) {
            super(context, msg);
        }

        @Override
        public void _onNext(List<InventoryEntity> list) {
            if (mView != null) {
                mView.showInventory(list);
            }
        }

        @Override
        public void _onNetWorkConnectError(String message) {
            if (mView != null) {
                mView.networkConnectError(Global.RETRY_LOAD_INVENTORY_ACTION);
            }
        }

        @Override
        public void _onCommonError(String message) {
            if (mView != null) {
                mView.setRefreshing(true,message);
            }
        }

        @Override
        public void _onServerError(String code, String message) {
            if (mView != null) {
                mView.setRefreshing(true,message);
            }
        }

        @Override
        public void _onComplete() {
            if (mView != null) {
                mView.setRefreshing(true, "获取库存成功");
            }
        }
    }
}
