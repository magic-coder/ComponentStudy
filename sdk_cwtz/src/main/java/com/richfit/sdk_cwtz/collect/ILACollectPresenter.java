package com.richfit.sdk_cwtz.collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectPresenter;

import java.util.Map;

/**
 * Created by monday on 2017/2/7.
 */

public interface ILACollectPresenter extends IBaseCollectPresenter<ILACollectView> {

    void getMaterialInfo(String queryType, String materialNum,String workId);

    void getInventoryInfo(String queryType, String workId, String invId,
                          String workCode, String invCode, String storageNum,
                          String materialNum, String materialId, String materialGroup,
                          String materialDesc, String batchFlag,
                          String location, String specialInvFlag, String specialInvNum,
                          String invType,Map<String,Object> extraMap);

    void getDeviceInfo(String deviceId);
}
