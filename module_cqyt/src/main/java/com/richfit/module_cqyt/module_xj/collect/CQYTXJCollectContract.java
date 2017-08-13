package com.richfit.module_cqyt.module_xj.collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectPresenter;
import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectView;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.List;

/**
 * Created by monday on 2017/7/3.
 */

public interface CQYTXJCollectContract {
    interface View extends IBaseCollectView {
        /**
         * 获取巡检路线缓存成功
         * @param locations
         */
        void showLocations(List<LocationInfoEntity> locations);

        /**
         * 获取巡检路线缓存失败
         * @param message
         */
        void getTransferInfoFail(String message);

        /**
         * 删除子节点成功
         * @param position：节点在明细列表的位置
         */
        void deleteNodeSuccess(int position);

        /**
         * 删除子节点失败
         * @param message
         */
        void deleteNodeFail(String message);
    }

    interface Presenter extends IBaseCollectPresenter<View> {
        /**
         * 获取整单缓存。这里给出的是有参考和无参考两大类获取整单缓存的接口。
         *
         * @param refData：抬头界面获取的单据数据
         * @param refCodeId：单据id
         * @param bizType:业务类型
         * @param refType：单据类型
         */
        void getTransferInfo(ReferenceEntity refData, String refCodeId, String bizType, String refType,
                             String userId, String workId, String invId, String recWorkId, String recInvId);

        /**
         * 删除一个子节点
         *
         * @param lineDeleteFlag：是否删除整行(Y/N)
         * @param transId：缓存头id
         * @param transLineId:缓存行id
         * @param locationId:缓存仓位id
         * @param bizType:业务类型
         * @param position：该子节点在所有节点中的真实位置
         */
        void deleteNode(String lineDeleteFlag, String transId, String transLineId, String locationId,
                        String refType, String bizType, int position, String companyCode);

    }
}
