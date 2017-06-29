package com.richfit.module_xngd.module_ys.detail;

import com.richfit.sdk_wzrk.base_as_detail.IASDetailPresenter;

import java.util.Map;

/**
 * Created by monday on 2017/6/29.
 */

public interface IXNGDAODetailPresenter extends IASDetailPresenter {
    /**
     * 过账验收数据
     *
     * @param refNum
     * @param refCodeId
     * @param transId
     * @param bizType
     * @param refType
     * @param inspectionType
     * @param userId
     * @param isLocal
     * @param voucherDate
     * @param transToSapFlag
     */
    void transferCollectionData(String refNum, String refCodeId, String transId, String bizType, String refType,
                                int inspectionType, String userId, boolean isLocal, String voucherDate,
                                String transToSapFlag, Map<String, Object> extraHeaderMap);
}
