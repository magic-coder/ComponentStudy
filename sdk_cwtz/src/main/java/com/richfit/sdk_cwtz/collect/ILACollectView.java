package com.richfit.sdk_cwtz.collect;

import com.richfit.common_lib.lib_mvp.BaseView;
import com.richfit.domain.bean.InventoryEntity;
import com.richfit.domain.bean.MaterialEntity;

import java.util.List;

/**
 * Created by monday on 2017/2/7.
 */

public interface ILACollectView extends BaseView {

    void getMaterialInfoSuccess(MaterialEntity materialEntity);
    void getMaterialInfoFail(String message);

    void getInventorySuccess(List<InventoryEntity> inventoryEntity);
    void getInventoryFail(String message);

    void saveCollectedDataSuccess(String message);
    void saveCollectedDataFail(String message);

}
