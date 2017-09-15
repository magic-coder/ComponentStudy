package com.richfit.module_cqyt.module_whxj.head;

import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadPresenter;
import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadView;

import java.util.Map;

/**
 * Created by monday on 2017/7/3.
 */

public interface CQYTWHXJHeadContract {
    interface View extends IBaseHeadView {
        void closeComplete();
        void closeFail(String message);
    }

    interface Presenter extends IBaseHeadPresenter<View> {
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
}
