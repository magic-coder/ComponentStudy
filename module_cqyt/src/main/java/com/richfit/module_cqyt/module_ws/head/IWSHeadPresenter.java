package com.richfit.module_cqyt.module_ws.head;

import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadPresenter;

/**
 * Created by monday on 2017/9/21.
 */

public interface IWSHeadPresenter extends IBaseHeadPresenter<IWSHeadView> {
    /**
     * 获取发出工厂列表
     */
    void getWorks(int flag);

    /**
     * 删除整单缓存
     * @param bizType：业务类型
     * @param userId:用户id
     */
    void deleteCollectionData(String refType, String bizType, String userId, String companyCode);
}
