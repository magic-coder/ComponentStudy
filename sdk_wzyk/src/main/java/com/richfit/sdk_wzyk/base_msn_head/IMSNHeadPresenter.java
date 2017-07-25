package com.richfit.sdk_wzyk.base_msn_head;


import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadPresenter;

/**
 * Created by monday on 2016/11/20.
 */

public interface IMSNHeadPresenter extends IBaseHeadPresenter<IMSNHeadView> {

    /**
     * 获取发出工厂列表
     */
    void getWorks(int flag);

    /**
     * 通过工厂id获取该工厂下的接收库存地点列表
     * @param workId
     */
    void getRecInvsByWorkId(String workId, int flag);

    void getSendInvsByWorkId(String workId, int flag);

    /**
     * 删除整单缓存
     * @param bizType：业务类型
     * @param userId:用户id
     */
    void deleteCollectionData(String refType, String bizType, String userId,
                              String companyCode);

    /**
     * 获取项目编号列表
     * @param workCode
     * @param keyWord
     * @param defaultItemNum
     * @param flag
     * @param bizType
     */
    void getProjectNumList(String workCode, String keyWord, int defaultItemNum, int flag, String bizType);

}
