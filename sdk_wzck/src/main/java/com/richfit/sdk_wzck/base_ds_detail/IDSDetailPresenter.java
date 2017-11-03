package com.richfit.sdk_wzck.base_ds_detail;


import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailPresenter;

import java.util.Map;

/**
 * Created by monday on 2016/11/20.
 */

public interface IDSDetailPresenter extends IBaseDetailPresenter<IDSDetailView> {
    /**
     * 寄售转自有业务。
     *
     * @param transId
     * @param bizType
     * @param refType
     * @param userId
     * @param voucherDate
     * @param transToSAPFlag
     * @param extraHeaderMap
     * @param submitFlag
     */
    void turnOwnSupplies(String transId, String bizType, String refType, String userId, String voucherDate,
                         String transToSAPFlag, Map<String, Object> extraHeaderMap, int submitFlag);

}
