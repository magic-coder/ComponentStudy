package com.richfit.sdk_wzck.base_dsn_detail;


import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailPresenter;

import java.util.Map;

/**
 * Created by monday on 2017/2/23.
 */

public interface IDSNDetailPresenter extends IBaseDetailPresenter<IDSNDetailView> {
    /**
     * 寄售转自有业务。
     *
     * @param transId
     * @param bizType
     * @param refType
     * @param userId
     * @param voucherDate
     * @param transToSapFlag
     * @param extraHeaderMap
     * @param submitFlag
     */
    void turnOwnSupplies(String transId, String bizType, String refType, String userId, String voucherDate,
                         String transToSapFlag, Map<String, Object> extraHeaderMap, int submitFlag);
}
