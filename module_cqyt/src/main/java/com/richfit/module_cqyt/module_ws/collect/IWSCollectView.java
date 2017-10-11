package com.richfit.module_cqyt.module_ws.collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectView;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.List;

/**
 * Created by monday on 2017/9/21.
 */

public interface IWSCollectView extends IBaseCollectView{

    /**
     * 输入物料获取缓存后，刷新界面
     * @param refData
     * @param batchFlag
     */
    void bindCommonCollectUI(ReferenceEntity refData, String batchFlag);
    void loadTransferSingleInfoFail(String message);
    void loadTransferSingleInfoComplete();


    /**
     * 显示库存
     * @param list
     */
    void showInventory(List<InventoryEntity> list);
    void loadInventoryFail(String message);
    void loadInventoryComplete();
}
