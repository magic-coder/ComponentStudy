package com.richfit.module_cqyt.module_inv;

import android.support.annotation.NonNull;

import com.richfit.common_lib.lib_mvp.IPresenter;

import java.util.Map;

/**
 * Created by monday on 2018/3/19.
 */

public interface ICQDSInvYPresenter extends IPresenter<ICQDSInvYView> {
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
