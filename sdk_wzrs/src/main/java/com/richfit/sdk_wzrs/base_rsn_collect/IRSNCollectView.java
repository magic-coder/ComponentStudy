package com.richfit.sdk_wzrs.base_rsn_collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectView;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.List;

/**
 * Created by monday on 2017/3/2.
 */

public interface IRSNCollectView extends IBaseCollectView {

    /**
     * 显示库存地点
     *
     * @param invs
     */
    void showInvs(List<InvEntity> invs);

    void loadInvsFail(String message);

    /**
     * 输入物料获取缓存后，刷新界面
     * @param refData
     * @param batchFlag
     */
    void bindCommonCollectUI(ReferenceEntity refData, String batchFlag);
    void loadTransferSingleInfoFail(String message);
    /**
     * 加载上架仓位
     */
    void loadLocationList(boolean isDropDown);
    void showInventory(List<String> list);
    void loadInventoryComplete(boolean isDropDown);
    void loadInventoryFail(String message);
}
