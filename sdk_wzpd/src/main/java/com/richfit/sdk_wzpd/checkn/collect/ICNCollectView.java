package com.richfit.sdk_wzpd.checkn.collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectView;
import com.richfit.domain.bean.InventoryEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by monday on 2017/3/3.
 */

public interface ICNCollectView extends IBaseCollectView {



    void getCheckTransferInfoSingle(String materialNum, String location);

    void loadInventorySuccess(List<InventoryEntity> list);

    void setupRefLineAdapter(ArrayList<String> refLines);
    void loadInventoryComplete();

    void loadInventoryFail(String message);

    void bindCommonCollectUI();

}
