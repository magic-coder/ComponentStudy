package com.richfit.sdk_wzrk.base_as_head;

import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadPresenter;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.List;


/**
 * 物资入库抬头标准接口
 * Created by monday on 2016/11/11.
 */

public interface IASHeadPresenter extends IBaseHeadPresenter<IASHeadView> {

    void getReference(String refNum, String refType, String bizType,
                      String moveType, String refLineId, String userId);

    /**
     * 获取整单缓存
     *
     * @param refData：抬头界面获取的单据数据
     * @param refCodeId：单据id
     * @param bizType:业务类型
     * @param refType：单据类型
     */
    void getTransferInfo(ReferenceEntity refData, String refCodeId, String bizType, String refType);

    /**
     * 删除整单缓存
     *
     * @param refNum:单据号
     * @param transId:缓存抬头id
     * @param bizType:业务类型
     */
    void deleteCollectionData(String refNum, String transId, String refCodeId, String refType, String bizType,
                              String userId, String companyCode);

    void getInvsByWorkId(String workId, int flag);


}
