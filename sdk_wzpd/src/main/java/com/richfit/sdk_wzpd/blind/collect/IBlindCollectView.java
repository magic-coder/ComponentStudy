package com.richfit.sdk_wzpd.blind.collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectView;
import com.richfit.domain.bean.MaterialEntity;

/**
 * Created by monday on 2017/3/3.
 */

public interface IBlindCollectView extends IBaseCollectView {

    void getCheckTransferInfoSingle(String materialNum, String location);

    void loadMaterialInfoSuccess(MaterialEntity data);
    void loadMaterialInfoFail(String message);
}
