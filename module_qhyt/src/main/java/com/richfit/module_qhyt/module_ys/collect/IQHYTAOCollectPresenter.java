package com.richfit.module_qhyt.module_ys.collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectPresenter;
import com.richfit.domain.bean.ResultEntity;

/**
 * Created by monday on 2017/2/28.
 */

public interface IQHYTAOCollectPresenter extends IBaseCollectPresenter<IQHYTAOCollectView> {


    /**
     * 获取单条缓存。
     *
     * @param refCodeId：单据id
     * @param refType：单据类型
     * @param bizType：业务类型
     * @param refLineId：单据行id
     * @param batchFlag:批次
     * @param location：仓位
     */
    void getTransferInfoSingle(String refCodeId, String refType, String bizType, String refLineId,
                               String batchFlag, String location, String refDoc, int refDocItem, String userId);

    void getInvsByWorkId(String workId, int flag);

}
