package com.richfit.sdk_wzck.base_dsn_collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectView;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/2/23.
 */

public interface IDSNCollectView extends IBaseCollectView {
    /**
     * 显示发出库位
     * @param invs
     */
    void showInvs(List<InvEntity> invs);
    void loadInvsFail(String message);

    /**
     * 显示库存
     * @param list
     */
    void showInventory(List<InventoryEntity> list);
    void loadInventoryFail(String message);
    void loadInventoryComplete();

    /**
     * 输入物料获取缓存后，刷新界面
     * @param refData
     * @param batchFlag
     */
    void bindCommonCollectUI(ReferenceEntity refData, String batchFlag);
    void loadTransferSingleInfoFail(String message);
    void loadTransferSingleInfoComplete();
}
