package com.richfit.sdk_cwtz.collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectView;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.MaterialEntity;
import com.richfit.domain.bean.SimpleEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by monday on 2017/2/7.
 */

public interface ILACollectView extends IBaseCollectView {

    void getMaterialInfoSuccess(MaterialEntity materialEntity);
    void getMaterialInfoFail(String message);

    void getInventorySuccess(List<InventoryEntity> inventoryEntity);
    void getInventoryFail(String message);
    void loadDictionaryDataSuccess(Map<String,List<SimpleEntity>> data);
    void loadDictionaryDataFail(String message);

}
