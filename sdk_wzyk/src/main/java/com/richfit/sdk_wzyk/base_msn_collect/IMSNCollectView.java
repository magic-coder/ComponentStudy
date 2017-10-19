package com.richfit.sdk_wzyk.base_msn_collect;


import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectView;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.ReferenceEntity;
import com.richfit.domain.bean.ResultEntity;
import com.richfit.domain.bean.SimpleEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2016/11/20.
 */

public interface IMSNCollectView extends IBaseCollectView {
    /**
     * 显示发出库位
     * @param invs
     */
    void showSendInvs(List<InvEntity> invs);
    void loadSendInvsFail(String message);

    /**
     * 显示库存
     * @param list
     */
    void showInventory(List<InventoryEntity> list);
    void loadInventoryFail(String message);
    void loadInventoryComplete();

    /**
     * 显示接收库存
     * */
    void showRecLocations(List<String> recLocations);
    void loadRecLocationsFail(String message);
    void loadRecLocationsComplete();

    /**
     * 输入物料获取缓存后，刷新界面
     * @param refData
     * @param batchFlag
     */
    void bindCommonCollectUI(ReferenceEntity refData, String batchFlag);
    void loadTransferSingleInfoFail(String message);
    void loadTransferSingleInfoComplete();

    void checkLocationFail(String message);
    void checkLocationSuccess(String batchFlag, String location);

    void loadDictionaryDataSuccess(Map<String,List<SimpleEntity>> data);
    void loadDictionaryDataFail(String message);
    /**
     * 检查ERP仓库号是否一致
     */
    void checkWareHouseSuccess();
    void checkWareHouseFail(String message);

    void getDeviceInfoSuccess(ResultEntity result);
    void getDeviceInfoFail(String message);
    void getDeviceInfoComplete();
}
