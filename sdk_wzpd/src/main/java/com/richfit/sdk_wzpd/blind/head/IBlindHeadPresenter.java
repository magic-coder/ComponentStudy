package com.richfit.sdk_wzpd.blind.head;


import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadPresenter;

import java.util.Map;

/**
 * Created by monday on 2017/3/3.
 */

public interface IBlindHeadPresenter extends IBaseHeadPresenter<IBlindHeadView> {
    /**
     * 获取发出工厂列表
     */
    void getWorks(int flag);

    /**
     * 获取仓库号列表
     *
     * @param flag
     */
    void getStorageNums(int flag);

    /**
     * 通过工厂id获取该工厂下库存地点列表
     *
     * @param workId
     */
    void getInvsByWorkId(String workId, int flag);

    /**
     * 获取盘点抬头信息
     *
     * @param userId:用户id
     * @param bizType:业务类型
     * @param checkLevel:盘点级别              01:仓位级 02:仓库级
     * @param checkSpecial:盘点特殊标识，针对特殊寄售库存
     * @param storageNum:仓库号
     * @param workId:工厂id
     * @param invId:库存地点id
     */
    void getCheckInfo(String userId, String bizType, String checkLevel, String checkSpecial,
                      String storageNum, String workId, String invId, String checkDate,Map<String,Object> extraMap);


    void deleteCheckData(String storageNum, String workId, String invId, String checkId, String userId, String bizType);

    void getDictionaryData(String... codes);
}
