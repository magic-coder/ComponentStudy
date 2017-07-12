package com.richfit.module_cqyt.module_vs;

import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadPresenter;
import com.richfit.common_lib.lib_base_sdk.base_head.IBaseHeadView;
import com.richfit.domain.bean.RefDetailEntity;

import java.util.List;

/**
 * Created by monday on 2017/7/3.
 */

public interface CQYTVSContract {
    interface View extends IBaseHeadView {

        /**
         * 请求单据数据，清除抬头必要的控件信息
         */
        void clearAllUI();

        void showDataFail(String message);
        void showNodes(List<RefDetailEntity> allNodes);

    }

    interface Presenter extends IBaseHeadPresenter<View> {
        /**
         * 获取单据数据
         *
         * @param refNum：单号
         * @param refType:单据类型
         * @param bizType：业务类型
         * @param moveType:移动类型
         * @param userId：用户loginId
         */
        void getReference(String refNum, String refType, String bizType, String moveType,
                          String refLineId, String userId);

    }
}
