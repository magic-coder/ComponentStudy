package com.richfit.sdk_wzrk.base_asn_collect;

import com.richfit.common_lib.lib_base_sdk.base_collect.IBaseCollectView;
import com.richfit.domain.bean.InvEntity;
import com.richfit.domain.bean.ReferenceEntity;

import java.util.List;

/**
 * Created by monday on 2016/11/27.
 */

public interface IASNCollectView extends IBaseCollectView {
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
    void loadLocationList(String keyWord, boolean isDropDown);

    void getLocationListFail(String message);
    void getLocationListSuccess(List<String> list, boolean isDropDown);

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
}
