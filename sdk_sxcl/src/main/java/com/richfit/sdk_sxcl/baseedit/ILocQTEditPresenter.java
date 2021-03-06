package com.richfit.sdk_sxcl.baseedit;


import com.richfit.common_lib.lib_base_sdk.base_edit.IBaseEditPresenter;

import java.util.Map;

/**
 * Created by monday on 2017/5/26.
 */

public interface ILocQTEditPresenter extends IBaseEditPresenter<ILocQTEditView> {
    /**
     * 获取库存信息
     *
     * @param workId:工厂id
     * @param invId：库存地点id
     * @param materialId：物料id
     * @param location：仓位
     * @param batchFlag:批次
     * @param invType：库存类型
     */
    void getInventoryInfo(String queryType, String workId, String invId,
                          String workCode, String invCode, String storageNum,
                          String materialNum, String materialId,
                          String location, String batchFlag, String specialInvFlag,
                          String specialInvNum, String invType,Map<String,Object> extraMap);

    /**
     * 获取单条缓存
     *
     * @param refCodeId：单据id
     * @param refType：单据类型
     * @param bizType：业务类型
     * @param refLineId：单据行id
     * @param batchFlag:批次
     * @param location：仓位
     */
    void getTransferInfoSingle(String refCodeId, String refType, String bizType, String refLineId, String materialNum,
                               String batchFlag, String location, String refDoc, int refDocItem, String userId);
}

