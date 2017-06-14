package com.richfit.sdk_wzck.base_dsn_head;


import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadPresenter;

/**
 * Created by monday on 2017/2/23.
 */

public interface IDSNHeadPresenter extends IBaseHeadPresenter<IDSNHeadView> {

    void getWorks(int flag);
    void getAutoCompleteList(String workCode, String keyWord, int defaultItemNum, int flag, String bizType);
    /**
     * 删除整单缓存
     * @param bizType：业务类型
     * @param userId:用户id
     */
    void deleteCollectionData(String refType, String bizType, String userId,
                              String companyCode);
}
