package com.richfit.module_qhyt.module_as.as_ww_component.collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectPresenter;
import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectView;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.RefDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/3/10.
 */

public interface QHYTASWWCCollectContract {
    interface QingHaiWWCCollectView extends IBaseCollectView {
        /**
         * 初始化单据行适配器
         */
        void setupRefLineAdapter();
        /**
         * 为数据采集界面的UI绑定数据
         */
        void bindCommonCollectUI();

        /**
         * 显示库存
         * @param list
         */
        void showInventory(List<InventoryEntity> list);
        void loadInventoryFail(String message);

        /**
         * 通过缓存刷新界面
         * @param cache
         * @param batchFlag
         * @param location
         */
        void onBindCache(RefDetailEntity cache, String batchFlag, String location);

        /**
         * 获取缓存成功
         */
        void loadCacheSuccess();

        /**
         * 未获取到缓存
         * @param message
         */
        void loadCacheFail(String message);
    }

    interface QingHaiWWCCollectPresenter extends IBaseCollectPresenter<QingHaiWWCCollectView> {
        /**
         * 获取库存信息
         * @param workId:工厂id
         * @param invId：库存地点id
         * @param materialId：物料id
         * @param location：仓位
         * @param batchFlag:批次
         * @param invType：库存类型
         */
        void getInventoryInfo(String queryType, String workId, String invId, String workCode, String invCode,
                              String storageNum, String materialNum, String materialId, String location, String batchFlag,
                              String specialInvFlag, String specialInvNum, String invType,Map<String,Object> extraMap);
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
    }
}
