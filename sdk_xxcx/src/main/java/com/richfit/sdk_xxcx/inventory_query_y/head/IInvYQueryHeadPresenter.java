package com.richfit.sdk_xxcx.inventory_query_y.head;

import android.support.annotation.NonNull;

import com.richfit.common_lib.lib_mvp.IPresenter;


/**
 * Created by monday on 2017/5/25.
 */

public interface IInvYQueryHeadPresenter extends IPresenter<IInvYQueryHeadView> {
    /**
     * 获取单据数据
     *
     * @param refNum：单号
     * @param refType:单据类型
     * @param bizType：业务类型
     * @param moveType:移动类型
     * @param userId：用户loginId
     */
    void getReference(@NonNull String refNum, @NonNull String refType,
                      @NonNull String bizType, @NonNull String moveType,
                      @NonNull String refLineId, @NonNull String userId);
}
