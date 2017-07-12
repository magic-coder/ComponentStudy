package com.richfit.module_cqyt.module_xj.collect;

import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.common_lib.lib_mvp.IPresenter;
import com.richfit.domain.bean.LocationInfoEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;

import java.util.List;

/**
 * Created by monday on 2017/7/3.
 */

public interface CQYTXJCollectContract {
    interface View extends BaseView {

        void saveCollectedDataSuccess();
        void saveCollectedDataFail(String message);

        void showLocations(List<LocationInfoEntity> locations);
        void getTransferInfoFail(String message);
    }

    interface Presenter extends IPresenter<View> {
        /**
         * 保存本次采集的数据
         *
         * @param result:用户采集的数据(json格式)
         */
        void uploadCollectionDataSingle(ResultEntity result);

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
    }
}
