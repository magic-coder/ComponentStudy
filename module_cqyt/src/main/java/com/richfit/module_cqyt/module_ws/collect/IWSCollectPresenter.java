package com.richfit.module_cqyt.module_ws.collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectPresenter;

import java.util.Map;

/**
 * Created by monday on 2017/9/21.
 */

public interface IWSCollectPresenter extends IBaseCollectPresenter<IWSCollectView> {
    /**
     * 获取数据采集界面的缓存
     * @param bizType：业务类型
     * @param materialNum：物资编码
     * @param userId：用户id
     * @param workId：发出工厂id
     * @param recWorkId：接收工厂id
     * @param recInvId：接收库存点id
     * @param batchFlag：发出批次
     */
    void getTransferSingleInfo(String bizType, String materialNum, String userId, String workId,
                               String invId, String recWorkId, String recInvId, String batchFlag,
                               String refDoc, int refDocItem);

    /**
     * 获取报检单列表
     * @param bizType
     * @param materialNum
     * @param userId
     * @param workCode
     * @param extraMap
     */
    void getInspectionInfo(String bizType,String materialNum,String userId,String workCode,Map<String,Object> extraMap);
}
