package com.richfit.sdk_wzck.base_ds_collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectPresenter;

import java.util.Map;


/**
 * Created by monday on 2016/11/19.
 */

public interface IDSCollectPresenter extends IBaseCollectPresenter<IDSCollectView> {

    /**
     * 通过工厂id获取该工厂下的库存地点列表
     *
     * @param workId
     */
    void getInvsByWorkId(String workId, int flag);

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
    void getInventoryInfo(String queryType, String workId, String invId, String workCode, String invCode,
                          String storageNum, String materialNum, String materialId, String location, String batchFlag,
                          String specialInvFlag, String specialInvNum, String invType, Map<String, Object> extraMap);

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

    //注意获取整单缓存和库存的workId以及invId不同。库存使用的是明细行的invId
    void getSuggestedLocation(
            //以上是获取整单缓存的参数
            String refCodeId, String bizType, String refType,
            String userId, String workId, String invId,
            String recWorkId, String recInvId,
            String queryType, String lineWorkId, String lineInvId, String lineWorkCode, String lineInvCode,
            String storageNum, String materialNum, String materialId, String location, String batchFlag,
            String specialInvFlag, String specialInvNum, String invType, Map<String, Object> extraMap
    );

    //检查仓位是否存在，离线使用
    void checkLocation(String queryType, String workId, String invId, String batchFlag,
                       String location,Map<String,Object> extraMap);
}
