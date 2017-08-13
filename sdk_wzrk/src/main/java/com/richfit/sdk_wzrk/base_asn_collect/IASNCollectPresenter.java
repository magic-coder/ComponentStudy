package com.richfit.sdk_wzrk.base_asn_collect;


import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectPresenter;
import com.richfit.domain.bean.ResultEntity;

/**
 * Created by monday on 2016/11/27.
 */

public interface IASNCollectPresenter extends IBaseCollectPresenter<IASNCollectView> {

    void getLocationList(String workId, String workCode, String invId, String invCode, String keyWord, int defaultItemNum, int flag,
                         boolean isDropDown);
    /**
     * 获取库存地点列表
     *
     * @param workId
     */
    void getInvsByWorks(String workId, int flag);
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
}
