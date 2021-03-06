package com.richfit.sdk_wzyk.base_msn_collect;


import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectPresenter;
import com.richfit.domain.bean.ResultEntity;

import java.util.Map;

/**
 * Created by monday on 2016/11/20.
 */

public interface IMSNCollectPresenter extends IBaseCollectPresenter<IMSNCollectView> {
    /**
     * 获取发出库存地点列表
     *
     * @param workId
     */
    void getSendInvsByWorks(String workId, int flag);

    /**
     * 获取数据采集界面的缓存
     *
     * @param bizType：业务类型
     * @param materialNum：物资编码
     * @param userId：用户id
     * @param workId：发出工厂id
     * @param recWorkId：接收工厂id
     * @param recInvId：接收库存点id
     * @param batchFlag：发出批次
     */
    void getTransferInfoSingle(String bizType, String materialNum, String userId, String workId,
                               String invId, String recWorkId, String recInvId, String batchFlag,
                               String refDoc, int refDocItem);
    //离线检查仓位
    void checkLocation(String queryType, String workId, String invId, String batchFlag, String location,Map<String,Object> extraMap);
    /**
     * 获取库存信息
     *
     * @param queryType:查询类型
     * @param workId：发出工厂id
     * @param invId:发出库位id
     * @param materialId:发出物料id
     * @param location：发出仓位
     * @param batchFlag：发出批次
     * @param invType：库存类型
     */
    void getInventoryInfo(String queryType, String workId, String invId, String workCode,
                          String invCode, String storageNum, String materialNum, String materialId,
                          String location, String batchFlag, String specialInvFlag, String specialInvNum,
                          String invType,Map<String,Object> extraMap);

    /**
     * 获取接收仓位的库存
     * @param queryType
     * @param workId
     * @param invId
     * @param workCode
     * @param invCode
     * @param storageNum
     * @param materialNum
     * @param materialId
     * @param location
     * @param batchFlag
     * @param specialInvFlag
     * @param specialInvNum
     * @param invType
     */
    void getInventoryInfoOnRecLocation(String queryType, String workId, String invId, String workCode,
                                       String invCode, String storageNum, String materialNum, String materialId,
                                       String location, String batchFlag, String specialInvFlag, String specialInvNum,
                                       String invType,Map<String,Object> extraMap);

    /**
     * 检查ERP仓库号是否一致
     *
     * @param sendWorkId：发出工厂id
     * @param sendInvCode：发出库位
     * @param recWorkId：接收工厂id
     * @param recInvCode：接收库位
     */
    void checkWareHouseNum(final boolean isOpenWM, final String sendWorkId, final String sendInvCode,
                           final String recWorkId, final String recInvCode, int flag);

    void getDeviceInfo(String deviceId);

    /**
     * 获取建议仓位和建议批次
     * @param workCode
     * @param invCode
     * @param materialNum
     * @param queryType
     */
    void getSuggestLocationAndBatchFlag(String workCode,String invCode,String materialNum,String queryType);
}
