package com.richfit.sdk_xxcx.inventory_query_n.detail;

import com.richfit.common_lib.lib_base_sdk.base_detail.IBaseDetailView;
import com.richfit.domain.bean.InventoryEntity;

import java.util.List;

/**
 * Created by monday on 2017/5/25.
 */

public interface IInvNQueryDetailView extends IBaseDetailView<InventoryEntity> {
    /**
     * 显示库存
     * @param list
     */
    void showInventory(List<InventoryEntity> list);
}
