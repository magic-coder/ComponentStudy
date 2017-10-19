package com.richfit.sdk_wzrs.base_rsn_collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectView;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.SimpleEntity;

import java.util.List;
import java.util.Map;

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
     * 加载上架仓位
     */
    void loadLocationList(boolean isDropDown);

    void loadInventoryFail(String message);
    void loadInventoryComplete(boolean isDropDown);
    void showInventory(List<String> list);

    //检查上架仓位
    void checkLocationFail(String message);
    void checkLocationSuccess(String batchFlag, String location);

    /**
     * 输入物料获取缓存后，刷新界面
     * @param refData
     * @param batchFlag
     */
    void bindCommonCollectUI(ReferenceEntity refData, String batchFlag);
    void loadTransferSingleInfoFail(String message);

    void loadDictionaryDataSuccess(Map<String,List<SimpleEntity>> data);
    void loadDictionaryDataFail(String message);
}
